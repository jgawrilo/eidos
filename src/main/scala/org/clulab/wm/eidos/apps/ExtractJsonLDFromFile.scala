package org.clulab.wm.eidos.apps

import org.clulab.serialization.json.stringify
import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.serialization.json.JLDCorpus
import org.clulab.wm.eidos.utils.{FileUtils, Timer}

object ExtractJsonLDFromFile extends App {
  val inputFile = args(0)
  val outputFile = if (args.size > 1) args(1) else args(0) + ".jsonld"
  lazy val reader = new EidosSystem()

  Timer.time("Prime the pump") {
    reader.extractFromText("This is a test.") // Just get all loading taken care of
  }

  // 1. Get the input file contents
  val text = FileUtils.getTextFromFile(inputFile)
  // 2. Open corresponding output file
  val pw = FileUtils.printWriterFromFile(outputFile)
  // 3. Extract causal mentions from the text
  val annotatedDocument = Timer.time("Just extraction") {
    reader.extractFromText(text)
  }
  // 4. Convert to an object that can be serialized as desired
  val corpus = new JLDCorpus(Seq(annotatedDocument), reader)
  // 5. Convert to JSON
  val mentionsJSONLD = corpus.serialize()
  // 6. Write to output file and close it
  pw.println(stringify(mentionsJSONLD, pretty = true))
  pw.close()
}

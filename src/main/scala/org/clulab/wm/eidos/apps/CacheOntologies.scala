package org.clulab.wm.eidos.apps

import java.io.File

import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.clulab.wm.eidos.EidosSystem
import org.clulab.wm.eidos.groundings.CompactDomainOntology.CompactDomainOntologyBuilder
import org.clulab.wm.eidos.groundings.EidosOntologyGrounder.{FAO_NAMESPACE, MESH_NAMESPACE, UN_NAMESPACE, WDI_NAMESPACE}
import org.clulab.wm.eidos.groundings._

object CacheOntologies extends App {

  val config = ConfigFactory.load("eidos")
      .withValue("EidosSystem.useW2V", ConfigValueFactory.fromAnyRef(false, "Don't use vectors when caching ontologies."))
  val reader = new EidosSystem(config)
  val loadableAttributes = reader.LoadableAttributes
  val cacheDir: String = loadableAttributes.cacheDir

  new File(cacheDir).mkdirs()

  val ontologies: Seq[String] = loadableAttributes.ontologies
  if (ontologies.isEmpty)
    throw new RuntimeException("No ontologies were specified, please check the config file.")
  else {
//    val proc = reader.proc
//
//    println(s"Saving ontologies to $cacheDir...")
//    ontologies.foreach { domainOntology =>
//      val serializedPath = DomainOntologies.serializedPath(domainOntology, cacheDir)
//
//      val ontology: DomainOntology = domainOntology match {
//        case   UN_NAMESPACE =>   UNOntology(loadableAttributes.unOntologyPath,   serializedPath, proc, loadSerialized = false)
//        case  WDI_NAMESPACE =>  WDIOntology(loadableAttributes.wdiOntologyPath,  serializedPath, proc, loadSerialized = false)
//        case  FAO_NAMESPACE =>  FAOOntology(loadableAttributes.faoOntologyPath,  serializedPath, proc, loadSerialized = false)
//        case MESH_NAMESPACE => MeshOntology(loadableAttributes.meshOntologyPath, serializedPath, proc, loadSerialized = false)
//        case _ => throw new IllegalArgumentException("Ontology " + domainOntology + " is not recognized.")
//      }
//      val treeDomainOntology = ontology.asInstanceOf[TreeDomainOntology]
//      val compactDomainOntology = new CompactDomainOntologyBuilder(treeDomainOntology).build()
//
//      compactDomainOntology.save(serializedPath)
//    }
//    println(s"Finished serializing ${ontologies.length} ontologies.")
  }

  val filenameIn = loadableAttributes.wordToVecPath
  val filenameOut = EidosWordToVec.makeCachedFilename(cacheDir, loadableAttributes.wordToVecPath)
  println(s"Saving vectors to $filenameOut...")
  val word2Vec = CompactWord2Vec(filenameIn, resource = true, cached = false)
  word2Vec.save(filenameOut)
  println(s"Finished serializing vectors.")
}

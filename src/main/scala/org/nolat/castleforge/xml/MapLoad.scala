package org.nolat.castleforge.xml

import javax.xml.validation.SchemaFactory
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import java.io.File
import org.apache.commons.io.FileUtils

import java.io.InputStream;

object MapLoad {
  def loadMap(file: File, schema: InputStream): Castle = {
    val source = FileUtils.openInputStream(file)
    loadMap(source, schema)
  }
  def loadMap(stream: InputStream, xsdStream: InputStream): Castle = {
    val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val schema = factory.newSchema(new StreamSource(xsdStream))

    val xml = new SchemaAwareFactoryAdapter(schema).load(stream)

    scalaxb.fromXML[Castle](xml)
  }
}
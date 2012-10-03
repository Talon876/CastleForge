package org.nolat.castleforge.xml

import org.xml.sax.InputSource
import scala.xml.parsing.NoBindingFactoryAdapter
import scala.xml.{ TopScope, Elem }
import javax.xml.parsers.{ SAXParserFactory, SAXParser }
import javax.xml.validation.Schema

/**
 * From @Horst Dehmer http://stackoverflow.com/questions/1627111/how-does-one-validate-the-schema-of-an-xml-file-using-scala
 */
class SchemaAwareFactoryAdapter(schema: Schema) extends NoBindingFactoryAdapter {
  override def loadXML(source: InputSource, parser: SAXParser) = {
    val reader = parser.getXMLReader()
    val handler = schema.newValidatorHandler()
    handler.setContentHandler(this)
    reader.setContentHandler(handler)

    scopeStack.push(TopScope)
    reader.parse(source)
    scopeStack.pop
    rootElem.asInstanceOf[Elem]
  }

  override def parser: SAXParser = {
    val factory = SAXParserFactory.newInstance()
    factory.setNamespaceAware(true)
    factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true)
    factory.newSAXParser()
  }
}
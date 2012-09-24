package org.nolat.castleforge

import java.io.File
import java.io.IOException
import java.net.URLDecoder
import java.util.jar.JarFile

import scala.collection.JavaConversions.enumerationAsScalaIterator

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

object NativeExtractor {
  def extractNatives(): String = {
    val nativeDirPath = System.getProperty("user.home") + "/." + App.APPNAME.toLowerCase + "/natives/" + App.VERSION + "/"
    val nativeDir = new File(nativeDirPath)
    nativeDir.mkdirs()
    val pathToJar = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()

    try {
      val decodedPathToJar = URLDecoder.decode(pathToJar, "UTF-8")
      println("Loading jar at " + decodedPathToJar)
      val self = new JarFile(decodedPathToJar)
      self.entries().foreach { entry =>
        if (entry.getName.startsWith("natives/")) {
          try {
            val tokens = entry.getName.split("/")
            val fileName = tokens(tokens.length - 1)
            extractFromJar("/natives/" + fileName, nativeDirPath + fileName)
          } catch {
            case e: IOException => e.printStackTrace()
          }
        }
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
    nativeDirPath
  }

  private def extractFromJar(jarPath: String, fsPath: String) = {
    try {
      val in = this.getClass().getResourceAsStream(jarPath)
      val fileOut = new File(fsPath)
      if (!fileOut.exists) { //only copy if the file doesn't already exist
        println("Moving " + jarPath + " from jar to " + fsPath)
        val out = FileUtils.openOutputStream(fileOut)
        IOUtils.copy(in, out)
        in.close()
        out.close()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
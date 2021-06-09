package muddler.mudlet.items
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.Echo

abstract class Item {
  def e

  def Item(Map options) {
    this.e = new Echo()
    this.name = options.name
    if (options.isActive == null) {
      this.isActive = "yes"
    } else {
      this.isActive = this.truthiness(options.isActive)
    }
    this.isFolder = this.truthiness(options.isFolder)
    this.path = options.path ?: ""
    this.packageName = options.packageName ?: ""
    this.children = []
    options.children.each { item ->
      item.path = this.path
      this.children.add(newItem(item))
    }
  }
  
  abstract def newItem(Map options)

  def truthiness(thing) {
    if (thing?.toBoolean() || thing == "yes") {
      return "yes"
    } else {
      return "no"
    }
  }  

  def readScripts(String itemType) {
    if (this.script == "" && this.isFolder != "yes" ) {
      def fullPath = "build/filtered/src${File.separator}$itemType${File.separator}${this.path}${File.separator}${this.name.replaceAll(" ", "_")}.lua"
      def scriptFile = new File(fullPath)
      if (scriptFile.exists()) {
        def fname = "$scriptFile" - "build/filtered/"
        def itype = "${itemType[0..-2]}"
        itype = (itype == "aliase") ? "alias" : itype
        e.echo("Using script from $fname for $itype '${this.name}'")
        this.script = scriptFile.text.normalize()
      }
    }
    this.script = XmlUtil.escapeXml(this.script)
  }
}

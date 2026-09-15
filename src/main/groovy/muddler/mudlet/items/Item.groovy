package muddler.mudlet.items
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.Echo

abstract class Item {
  def e
  // True when this item was synthesised from a directory name rather than
  // declared in a json file. Synthesised folders carry only defaults, so a
  // real declaration for the same folder must win when the two are merged.
  boolean synthetic = false

  def Item(Map options) {
    this.e = new Echo()
    this.synthetic = options.synthetic ?: false
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
    if (this.script == "") {
      def path = this.path
      def fullPath = "build${File.separator}filtered${File.separator}src${File.separator}$itemType${File.separator}$path${File.separator}${this.name.replaceAll(" ", "_")}.lua"
      def scriptFile = new File(fullPath)
      if (scriptFile.exists()) {
        def fname = "$scriptFile" - "build${File.separator}filtered${File.separator}"
        def itype = "${itemType[0..-2]}"
        itype = (itype == "aliase") ? "alias" : itype
        e.echo("Using script from $fname for $itype '${this.name}'")
        this.script = scriptFile.text.normalize()
      }
    }
    this.script = XmlUtil.escapeXml(this.script)
  }
}

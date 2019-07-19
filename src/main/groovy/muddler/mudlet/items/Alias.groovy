package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Alias extends Item {
  String isActive
  String isFolder
  String name
  String script
  String packageName
  String command
  String regex
  String path
  List children
  
  Alias(Map options) {
    super(options)
    this.command = options.command ?: ""
    this.regex = options.regex ?: ""
    this.script = options.script ?: ""
    super.readScripts("aliases")
  }

  def newItem(Map options) {
    return new Alias(options)
  }
  
  def toXML() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def childString = ""
    this.children.each {
      childString = childString + it.toXML()
    }
    def header = 'Alias'
    if (this.isFolder == "yes") {
      header = 'AliasGroup'
    }
    xml."$header" ( isActive : this.isActive, isFolder : this.isFolder ) {
      name this.name
      mkp.yieldUnescaped "<script>" + this.script + "</script>"
      command this.command 
      packageName ''
      regex this.regex
      mkp.yieldUnescaped childString
    }
    return writer.toString()
  }
}
package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Script extends Item {
  String isActive
  String isFolder
  String packageName
  String name
  String script
  String path
  List eventHandlerList
  List children

  Script(Map options) {
    super(options)
    this.eventHandlerList = options.eventHandlerList
    this.script = options.script ?: ""
    super.readScripts("scripts")
  }

  def newItem(Map options) {
    return new Script(options)
  }
  
  def toXML() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def eventListString = "\n<eventHandlerList>\n"
    this.eventHandlerList.each { event ->
      eventListString = eventListString  + "<string>$event</string>\n"
    }
    eventListString = eventListString + "</eventHandlerList>"
    def childXML = ""
    this.children.each {
      childXML = childXML + it.toXML()
    }
    def header = 'Script'
    if (this.isFolder == "yes") {
      header = 'ScriptGroup'
    }
    xml."$header" ( isActive : this.isActive, isFolder : this.isFolder ) {
      name this.name
      mkp.yieldUnescaped "<script>" + this.script + "</script>"
      packageName ''
      mkp.yieldUnescaped eventListString
      mkp.yieldUnescaped childXML
    }
    return writer.toString()
  }
}

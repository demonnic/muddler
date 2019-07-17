package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Timer extends Item {
  String isActive
  String isFolder
  String isTempTimer = "no"
  String isOffsetTimer = "no"
  String name
  String path
  String script
  String command
  String packageName
  String hours
  String minutes
  String seconds
  String milliseconds
  String time
  List children

  Timer(Map options) {
    super(options)
    this.command = options.command ?: ""
    this.time = options.time ?: ""
    this.hours = options.hours ?: 0
    this.minutes = options.minutes ?: 0
    this.seconds = options.seconds ?: 0
    this.milliseconds = options.milliseconds ?: 0
    if (this.time == "") {
      this.time = String.format("%02d:%02d:%02d:%03d",this.hours.toInteger(),this.minutes.toInteger(), this.seconds.toInteger() , this.milliseconds.toInteger())
    }
    this.script = options.script ?: ""
    super.readScripts("timers")
  }

  def newItem(Map options) {
    return new Timer(options)
  }
  
  def toXML() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def childString = ""
    this.children.each {
      childString = childString + it.toXML()
    }
    def header = "Timer"
    if (this.isFolder == "yes") {
      header = "TimerGroup"
    }
    xml."$header"( isActive : this.isActive, isFolder : this.isFolder, isTempTimer: this.isTempTimer, isOffsetTimer: this.isOffsetTimer) {
      name this.name
      mkp.yieldUnescaped "<script>" + this.script + "</script>"
      command this.command 
      packageName ''
      time this.time
      mkp.yieldUnescaped childString
    }
    return writer.toString()
  }
}
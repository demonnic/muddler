package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Button extends Item {
  String isActive
  String isFolder
  String isPushButton
  String isFlatButton
  String useCustomLayout
  String name
  String path
  String packageName
  String script
  String css
  String commandButtonUp
  String commandButtonDown
  String icon
  String orientation
  String location
  String posX
  String posY
  String mButtonState
  String sizeX
  String sizeY
  String buttonColumn
  String buttonRotation
  String buttonColor
  List children

  def Button(Map options) {
    super(options)
    this.script = options.script ?: ''
    super.readScripts("buttons")

    this.isPushButton    = super.truthiness(options.pushdown ?: options.isPushButton)
    this.isFlatButton    = super.truthiness(options.flat ?: options.isFlatButton)
    this.useCustomLayout = super.truthiness(options.customLayout ?: options.useCustomLayout)

    this.css               = options.css ?: ''
    this.commandButtonUp   = options.commandButtonUp ?: ''
    this.commandButtonDown = options.commandButtonDown ?: ''
    this.icon              = options.icon ?: ''
    this.buttonColor       = options.buttonColor ?: ''

    // Mudlet stores these as bare integers and is unforgiving about their
    // absence, so every one gets the value a freshly made button carries.
    // A toolbar (isFolder) and a button on it default differently only in
    // orientation, which Mudlet writes as 0 for the toolbar itself.
    this.orientation    = "${options.orientation    ?: (this.isFolder == 'yes' ? '0' : '1')}"
    this.location       = "${options.location       ?: '0'}"
    this.posX           = "${options.posX           ?: '0'}"
    this.posY           = "${options.posY           ?: '0'}"
    this.mButtonState   = "${options.mButtonState   ?: '1'}"
    this.sizeX          = "${options.sizeX          ?: '0'}"
    this.sizeY          = "${options.sizeY          ?: '0'}"
    this.buttonColumn   = "${options.buttonColumn   ?: '1'}"
    this.buttonRotation = "${options.buttonRotation ?: '0'}"
  }

  def newItem(Map options) {
    return new Button(options)
  }

  def toXML() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def childString = ""
    this.children.each {
      childString = childString + it.toXML()
    }
    def header = "Action"
    if (this.isFolder == "yes") {
      header = "ActionGroup"
    }
    xml."$header" (isActive: this.isActive, isFolder: this.isFolder,
                   isPushButton: this.isPushButton, isFlatButton: this.isFlatButton,
                   useCustomLayout: this.useCustomLayout) {
      name this.name
      packageName ''
      mkp.yieldUnescaped "<script>" + this.script + "</script>"
      css this.css
      commandButtonUp this.commandButtonUp
      commandButtonDown this.commandButtonDown
      icon this.icon
      orientation this.orientation
      location this.location
      posX this.posX
      posY this.posY
      mButtonState this.mButtonState
      sizeX this.sizeX
      sizeY this.sizeY
      buttonColumn this.buttonColumn
      buttonRotation this.buttonRotation
      // Only written when asked for. Mudlet 3.x era packages have no such
      // element, and emitting an empty one on every button would change every
      // imported package for no gain.
      if (this.buttonColor) { buttonColor this.buttonColor }
      mkp.yieldUnescaped childString
    }
    return writer.toString()
  }
}

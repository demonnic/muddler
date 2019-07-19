package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Button extends Item {
  String isActive
  String ifFolder
  String isPushButton
  String isFlatButton
  String useCustomLayout
  String name
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
  List buttons

  def Button(Map options) {
    super(options)
    super.readScripts("buttons")
  }

  def newItem(Map options) {
    return new Button(options)
  }

}
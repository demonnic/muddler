package muddler.mudlet.packages
import muddler.mudlet.packages.Package
import muddler.mudlet.items.Button

class ActionPackage extends Package {

  ActionPackage() {
    super('buttons')
  }

  def toXML() {
    return super.toXML('ActionPackage')
  }

  def newItem(Map options) {
    return new Button(options)
  }

  def findFiles() {
    return super.findFiles("buttons.json")
  }

}

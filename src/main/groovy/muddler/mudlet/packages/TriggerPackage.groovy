package muddler.mudlet.packages
import muddler.mudlet.packages.Package
import muddler.mudlet.items.Trigger

class TriggerPackage extends Package {

  TriggerPackage() {
    super('triggers')
  }

  def toXML() {
    return super.toXML('TriggerPackage')
  }
  def newItem(Map options) {
    return new Trigger(options)
  }

  def findFiles() {
    return super.findFiles("triggers.json")
  }
  
}
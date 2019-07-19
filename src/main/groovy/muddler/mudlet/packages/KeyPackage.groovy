package muddler.mudlet.packages
import muddler.mudlet.packages.Package
import muddler.mudlet.items.Key

class KeyPackage extends Package {

  KeyPackage() {
    super('keys')
  }

  def toXML() {
    return super.toXML('KeyPackage')
  }

  def newItem(Map options) {
    return new Key(options)
  }

  def findFiles() {
    return super.findFiles("keys.json")
  }
  
}
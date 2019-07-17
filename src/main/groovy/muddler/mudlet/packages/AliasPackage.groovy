package muddler.mudlet.packages
import muddler.mudlet.packages.Package
import muddler.mudlet.items.Alias

class AliasPackage extends Package {

  AliasPackage() {
    super('aliases')
  }

  def toXML() {
    return super.toXML('AliasPackage')
  }
  def newItem(Map options) {
    return new Alias(options)
  }

  def findFiles() {
    return super.findFiles("aliases.json")
  }
}
package muddler.mudlet.packages
import muddler.mudlet.packages.Package
import muddler.mudlet.items.Script

class ScriptPackage extends Package {

  ScriptPackage() {
    super('scripts')
  }

  def toXML() {
    return super.toXML('ScriptPackage')
  }
  def newItem(Map options) {
    return new Script(options)
  }

  def findFiles() {
    return super.findFiles("scripts.json")
  }
  
}
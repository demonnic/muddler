package muddler.mudlet.packages
import muddler.mudlet.packages.Package
import muddler.mudlet.items.Timer

class TimerPackage extends Package {

  TimerPackage() {
    super('timers')
  }

  def toXML() {
    return super.toXML('TimerPackage')
  }
  def newItem(Map options) {
    return new Timer(options)
  }

  def findFiles() {
    return super.findFiles("timers.json")
  }
  
}
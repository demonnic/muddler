package muddler.mudlet
import groovy.transform.ToString

@ToString
class Script {
  String isActive
  String isFolder
  String name
  String script
  List eventHandlerList
  List scripts
}
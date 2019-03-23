package muddler.mudlet
import groovy.transform.ToString

@ToString
class Script {
  String isActive
  String isFolder
  String name
  String script
  String eventHandlerList
  List scripts
}
package muddler.mudlet.items
import groovy.transform.ToString

@ToString
class Keybinding {
  String isActive
  String isFolder
  String name
  String script
  String command
  String keyCode
  String keyModifier
  List keys
}
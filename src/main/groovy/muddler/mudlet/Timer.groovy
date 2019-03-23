package muddler.mudlet
import groovy.transform.ToString

@ToString
class Timer {
  String isActive
  String isFolder
  String isTempTimer
  String isOffsetTimer
  String name
  String script
  String command
  String packageName
  String time
  List timers
}
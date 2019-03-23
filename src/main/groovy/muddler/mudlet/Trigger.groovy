package muddler.mudlet
import groovy.transform.ToString

@ToString
class Trigger {
  String isActive
  String isFolder
  String isTempTrigger
  String isMultiline
  String isPerlSlashGOption
  String isColorizerTrigger
  String isFilterTrigger
  String isSoundTrigger
  String isColorTrigger
  String isColorTriggerFg
  String isColorTriggerBg
  String name
  String script
  String triggerType
  String conditonLineDelta
  String mStayOpen
  String mCommand
  String mFgColor
  String mBgColor
  String mSoundFile
  String colorTriggerBgColor
  String colorTriggerFgColor
  List regexCodes
  List regexCodeProperties
  List triggers
}
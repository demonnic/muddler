package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Trigger extends Item {
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
  String path
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
  String packageName
  List regexCodeList
  List regexCodePropertyList
  List patterns
  List children

  Trigger(Map options) {
    super(options)
    this.script = options.script ?: ""
    super.readScripts("triggers")
    this.mCommand = options.command ?: ""
    this.triggerType = 0
    this.isMultiline = super.truthiness(options.multiline)
    this.conditonLineDelta = "0"
    if (this.isMultiline == "yes") {
      this.conditonLineDelta = options.multilineDelta ?: this.conditonLineDelta
    }
    this.isPerlSlashGOption = super.truthiness(options.matchall)
    this.isFilterTrigger = super.truthiness(options.filter)
    this.mStayOpen = options.fireLength ?: "0"
    this.mSoundFile = ""
    this.isSoundTrigger = "no"
    if (options.soundFile) {
      this.mSoundFile = options.soundFile
      this.isSoundTrigger = "yes"
    }
    this.isColorizerTrigger = super.truthiness(options.highlight)
    this.mFgColor = "#ff0000"
    this.mBgColor = "#ffff00"
    if (this.isColorizerTrigger == "yes") {
      this.mFgColor = options.highlightFG ?: this.mFgColor
      this.mBgColor = options.highlightBG ?: this.mBgColor
    }
    this.colorTriggerBgColor = "#000000"
    this.colorTriggerFgColor = "#000000"
    this.patterns = options.patterns ?: []
    this.regexCodeList = []
    this.regexCodePropertyList = []
    this.patterns.each { pattern ->
      def patternTypeNumber = patternTypeToNumber(pattern.type) // this sets a default if it wasn't given
      if (patternTypeNumber == '6') { // 6 is the number for color trigger. 
        this.isColorTrigger = "yes"
        def colorArray = pattern.pattern.split(",")
        def fg = colorArray[0]
        def bg = colorArray[1]
        this.isColorTriggerBg = "yes"
        this.isColorTriggerFg = "yes"
        if (fg == "IGNORE") { this.isColorTriggerFg = "no" }
        if (bg == "IGNORE") { this.isColorTriggerBg = "no" }
        this.regexCodeList.add("<string>ANSI_COLORS_F{$fg}_B{$bg}</string>")
        this.regexCodePropertyList.add("<integer>$patternTypeNumber</integer>")
      } else if (patternTypeNumber == '7') { // prompt triggers have empty string for regexCode
        this.regexCodeList.add("<string></string>")
        this.regexCodePropertyList.add("<integer>$patternTypeNumber</integer>")
      } else {
        this.regexCodeList.add("<string>${XmlUtil.escapeXml(pattern.pattern)}</string>")
        this.regexCodePropertyList.add("<integer>$patternTypeNumber</integer>")
      }
    }
  }

  def newItem(Map options) {
    return new Trigger(options)
  }

  def toXML() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def childString = ""
    this.children.each {
      childString = childString + it.toXML()
    }
    def regexCodeString = "<regexCodeList>\n" + this.regexCodeList.join("\n") + "</regexCodeList>"
    def regexCodePropertyListString = "<regexCodePropertyList>\n" + this.regexCodePropertyList.join("\n") + "</regexCodePropertyList>"
    def header = "Trigger"
    if (this.isFolder == "yes") {
      header = "TriggerGroup"
    }
    xml."$header" (isActive: this.isActive, isFolder: this.isFolder, isMultiline: this.isMultiline, isPerlSlashGOption: this.isPerlSlashGOption, isColorizerTrigger: this.isColorizerTrigger, isFilterTrigger: this.isFilterTrigger, isColorTrigger: this.isColorTrigger, isColorTriggerFg: this.isColorTriggerFg, isColorTriggerBg: this.isColorTriggerBg) {
      name this.name
      mkp.yieldUnescaped "<script>${this.script}</script>"
      triggerType this.triggerType
      conditonLineDelta this.conditonLineDelta
      mStayOpen this.mStayOpen
      mCommand this.mCommand
      packageName ''
      path this.path
      mFgColor this.mFgColor
      mBgColor this.mBgColor
      mSoundFile this.mSoundFile
      colorTriggerFgColor this.colorTriggerFgColor
      colorTriggerBgColor this.colorTriggerBgColor
      mkp.yieldUnescaped regexCodeString
      mkp.yieldUnescaped regexCodePropertyListString
      mkp.yieldUnescaped childString
    }
    return writer.toString()
  }

  def patternTypeToNumber(String patternType) {
    def typeMap = [
      substring: '0',
      regex: '1',
      startOfLine: '2',
      exactMatch: '3',
      lua: '4',
      spacer: '5',
      color: '6',
      colour: '6',
      prompt: '7'
    ]
    return typeMap[patternType] ?: '0'
  }
}
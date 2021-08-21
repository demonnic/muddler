package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Key extends Item {
  String isActive
  String isFolder
  String name
  String path
  String packageName
  String script
  String command
  String keyCode
  String keyModifier
  List children
  
  Key(Map options) {
    super(options)
    this.script = options.script ?: ''
    super.readScripts('keys')
    this.command = options.command ?: ''
    extractKeyAndModifierCodes(options.keys ?: '')
    if (this.isFolder == "yes") {
      this.keyCode = this.keyCode ?: "33554431" // copied from a freshly made key group in mudlet 4.12
      this.keyModifier = this.keyModifier ?: "0" // ^this
    }
  }

  def newItem(Map options) {
    return new Key(options)
  }

  def toXML() {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def childString = ""
    this.children.each {
      childString = childString + it.toXML()
    }
    def header = "Key"
    if (this.isFolder == "yes") {
      header = "KeyGroup"
    }
    xml."$header" (isActive: this.isActive, isFolder: this.isFolder) {
      name this.name
      packageName ''
      mkp.yieldUnescaped "<script>" + this.script + "</script>"
      command this.command 
      keyCode this.keyCode
      keyModifier this.keyModifier
      mkp.yieldUnescaped childString
    }
    return writer.toString()
  }
  
  def extractKeyAndModifierCodes(String keyString) {
    def requiresShift = [
      '"',
      ':',
      '>',
      '<',
      '!',
      '@',
      '#',
      '$',
      '%',
      '^',
      '&',
      '*',
      '(',
      ')',
      '_',
      '+'
    ]
    def hasShift = [
      '1': '!',
      '2': '@',
      '3': '#',
      '4': '$',
      '5': '%',
      '6': '^',
      '7': '&',
      '8': '*',
      '9': '(',
      '0': ')',
      '-': '_',
      '=': '+',
      ';': ':',
      ',': '<',
      '.': '>',
      '/': '?',
      '`': '~',
    ]
    def keyParts = keyString.split(/\+/)
    def modifiers = []
    def key = ""
    keyParts.each { part -> 
      part = part.toLowerCase()
      if (part == 'shift' || part == 'ctl' || part == 'alt' || part == 'ctrl' || part == 'keypad' ) {
        if (part == 'ctl') { part = 'ctrl' }
        modifiers.add(part)
      } else {
        key = part
      }
    }
    if (key == 'plus') { key =  '+' }
    if (key == 'minus') { key = '-' }
    if (key == 'slash') { key = "/" }
    if (key == "asterisk") { key = "*" }
    if (! modifiers.contains('keypad') ) { 
      if (requiresShift.contains(key) && ! modifiers.contains('keypad') ) {
        modifiers.add('shift')
      }
      if (modifiers.contains('shift')) {
        key = hasShift[key] ?: key
      }
    }
    this.keyCode = keyToKeyCode(key)
    this.keyModifier = generateModifierCode(modifiers)
  }
  
def generateModifierCode(ArrayList modifiers) {
    def ctrl = modifiers.contains('ctrl')
    def shift = modifiers.contains('shift')
    def alt = modifiers.contains('alt')
    def keypad = modifiers.contains('keypad')
    // I should rewrite this, but I forgot about keypads when I wrote it the first time
    // so... for now I just adjusted the returns I already had. Left side is if it's 
    // on the keypad, right side is if not.
    if (shift) {
      if (ctrl) {
        if (alt) { 
          return keypad ? '771751936' : '234881024' // shift+ctrl+alt
        } else {
          return keypad ? '637534208' : '100663296' // shift+ctrl
        }
      } else {
        if (alt) {
          return keypad ? '704643072' : '167772160' // shift+alt
        } else {
          return keypad ? '570425344' : '33554432' // only shift
        }
      }
    } else {
      if (ctrl) {
        if (alt) {
          return keypad ? '738197504' : '201326592' // ctrl+alt
        } else {
          return keypad ? '603979776' : '67108864' // ctrl
        }
      } else {
        if (alt) {
          return keypad ? '671088640' : '134217728' // alt
        } else {
          return keypad ? '536870912' : '0' // none
        }
      }
    } 
  }
   def keyToKeyCode(String key) {
    def keyCodes = [
      'backspace': '16777219',
      'return': '16777220',
      'enter': '16777221',
      'ins': '16777222',
      'del': '16777223',
      'print': '16777225',
      'clear': '16777227',
      'home': '16777232',
      'end': '16777233',
      'left': '16777234',
      'up': '16777235',
      'right': '16777236',
      'down': '16777237',
      'pgup': '16777238',
      'pgdn': '16777239',
      'caps': '16777252',
      'num': '16777253',
      'scroll': '16777254',
      'f1': '16777264',
      'f2': '16777265',
      'f3': '16777266',
      'f4': '16777267',
      'f5': '16777268',
      'f6': '16777269',
      'f7': '16777270',
      'f8': '16777271',
      'f9': '16777272',
      'f10': '16777273',
      'f11': '16777274',
      'f12': '16777275',
      'f13': '16777276',
      'f14': '16777277',
      'f15': '16777278',
      'f16': '16777279',
      'f17': '16777280',
      'f18': '16777281',
      '!': '33',
      '"': '34',
      '#': '35',
      '$': '36',
      '%': '37',
      '&': '38',
      "'": '39',
      '(': '40',
      ')': '41',
      '*': '42',
      '+': '43',
      ',': '44',
      '-': '45',
      '.': '46',
      '/': '47',
      '0': '48',
      '1': '49',
      '2': '50',
      '3': '51',
      '4': '52',
      '5': '53',
      '6': '54',
      '7': '55',
      '8': '56',
      '9': '57',
      ':': '58',
      ';': '59',
      '<': '60',
      '=': '61',
      '>': '62',
      '?': '63',
      '@': '64',
      'a': '65',
      's': '66',
      'c': '67',
      'd': '68',
      'e': '69',
      'f': '70',
      'g': '71',
      'h': '72',
      'i': '73',
      'j': '74',
      'k': '75',
      'l': '76',
      'm': '77',
      'n': '78',
      'o': '79',
      'p': '80',
      'q': '81',
      'r': '82',
      's': '83',
      't': '84',
      'u': '85',
      'v': '86',
      'w': '87',
      'x': '88',
      'y': '89',
      'z': '90',
      '[': '91',
      '\\': '92',
      ']': '93',
      '^': '94',
      '_': '95',
      '`': '96',
      '~': '126'
    ]
    return keyCodes[key]
  }
}

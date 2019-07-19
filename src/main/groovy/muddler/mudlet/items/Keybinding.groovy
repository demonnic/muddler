package muddler.mudlet.items
import groovy.transform.ToString
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import muddler.mudlet.items.Item

@ToString
class Keybinding extends Item {
  String isActive
  String isFolder
  String name
  String script
  String command
  String keyCode
  String keyModifier
  List keys
  
  Keybinding(Map options) {
    super(options)
    super.readScripts("keys")
  }

  def newItem(Map options) {
    return new Keybinding(options)
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
      if (part == "shift" || part == "ctl" || part == "alt" || part == "ctrl") {
        if (part == "ctl") { part = "ctrl" }
        modifiers.add(part)
      } else {
        key = part
      }
    }
    if (key == "plus") { key =  "+" }
    if (requiresShift.contains(key)) {
      modifiers.add("shift")
    }
    if (modifiers.contains("shift")) {
      key = hasShift[key] ?: key
    }
    this.keyCode = keyToKeyCode(key)
    this.keyModifier = generateModifierCode(modifiers)
  }
  
def generateModifierCode(ArrayList modifiers) {
    def ctrl = modifiers.contains('ctrl')
    def shift = modifiers.contains('shift')
    def alt = modifiers.contains('alt')
    if (shift) {
      if (ctrl) {
        if (alt) { 
          return "234881024" // shift+ctrl+alt
        } else {
          return "100663296" // shift+ctrl
        }
      } else {
        if (alt) {
          return "167772160"
        } else {
          return "33554432" // only shift
        }
      }
    } else {
      if (ctrl) {
        if (alt) {
          return "201326592" // ctrl+alt
        } else {
          return "67108864" // ctrl
        }
      } else {
        if (alt) {
          return "134217728" // alt
        } else {
          return '0' // none
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
      'F1': '16777264',
      'F2': '16777265',
      'F3': '16777266',
      'F4': '16777267',
      'F5': '16777268',
      'F6': '16777269',
      'F7': '16777270',
      'F8': '16777271',
      'F9': '16777272',
      'F10': '16777273',
      'F11': '16777274',
      'F12': '16777275',
      'F13': '16777276',
      'F14': '16777277',
      'F15': '16777278',
      'F16': '16777279',
      'F17': '16777280',
      'F18': '16777281',
      '!': '33',
      '"': '34',
      '#': '35',
      '$': '36',
      "%": '37',
      "&": '38',
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
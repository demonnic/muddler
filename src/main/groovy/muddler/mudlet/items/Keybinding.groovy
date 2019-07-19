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

  
  def extractKeyAndModifierCodes() {
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
      'A': '65',
      'B': '66',
      'C': '67',
      'D': '68',
      'E': '69',
      'F': '70',
      'G': '71',
      'H': '72',
      'I': '73',
      'J': '74',
      'K': '75',
      'L': '76',
      'M': '77',
      'N': '78',
      'O': '79',
      'P': '80',
      'Q': '81',
      'R': '82',
      'S': '83',
      'T': '84',
      'U': '85',
      'V': '86',
      'W': '87',
      'X': '88',
      'Y': '89',
      'Z': '90',
      '[': '91',
      '\\': '92',
      ']': '93',
      '_': '95',
      '`': '96',
      '~': '126'
    ]
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
  }
}
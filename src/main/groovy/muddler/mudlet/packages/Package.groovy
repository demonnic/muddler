package muddler.mudlet.packages
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import static groovy.io.FileType.*
import java.util.regex.Pattern


abstract class Package {
  File baseDir
  List files
  List children

  abstract def newItem(Map options)

  def Package(String packageType ) {
    this.baseDir = new File("src/$packageType")
    this.children = []
    if (baseDir.exists()) {
      this.files = this.findFiles()
      this.createItems()
    }
  }

  def toXML(packageName) {
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def childString = ""
    this.children.each {
      childString = childString + it.toXML()
    }
    childString = childString + "\n"
    xml."$packageName" {
      mkp.yieldUnescaped childString
    }
    return writer.toString()
  }
  def createItems() {
    def fullItemsAsArrays = []
    this.files.each {
      def fileArray = "${it}".split(Pattern.quote(File.separator)).toList()
      def directoriesInPath = fileArray[2..-2]
      def filePath = directoriesInPath.join(File.separator)
      def itemPayload = []
      def itemArray = []
      def jsonItems = new JsonSlurper().parse(it)
      jsonItems.each {
        it.path = filePath
        itemPayload.add(newItem(it))
      }
      directoriesInPath.each {
        def properties = [:]
        properties.isFolder = "yes"
        properties.name = it
        itemArray.add(newItem(properties))
      }
      itemArray.add(itemPayload)
      fullItemsAsArrays.add(itemArray)
    }
    fullItemsAsArrays.each {
      def testData = it
      def currentData = testData.removeLast()
      this.children.add listToItems(testData, currentData)
    }
    this.children = fullMerge(this.children)
  }

  def listToItems(theList, currentData) {
    def newItem = theList.removeLast()
    newItem.children.addAll currentData
    if (theList.size() == 0) {
      return newItem
    } else {
      return listToItems(theList, newItem)
    }
  }

  def mergeDown(ArrayList mergeFrom, ArrayList mergeInto = []) {
    if (mergeFrom.empty) {
      return mergeInto
    } else {
      def objectToMergeInto = mergeFrom.removeAt(0)
      def mergedList = mergeFrom.collect {
        if (it.name == objectToMergeInto.name) {
          objectToMergeInto.children = objectToMergeInto.children + it.children
          return
        } else {
          return it
        }
      }
      mergeInto.add objectToMergeInto
      mergedList.removeAll([null])
      if (mergedList.size == 0 ) {
        return mergeInto
      } else {
        return mergeDown(mergedList, mergeInto)            
      }
    }
  }

  def fullMerge(ArrayList toMerge) {
    def mergedList = mergeDown(toMerge)
    mergedList.collect {
      if (it.children.size > 1) {
        def newItems = fullMerge(it.children)
        it.children = newItems
        return it
      } else {
        return it
      }
    }
    return mergedList
  }
  def findFiles(fileName) {
    def fileList = []
    this.baseDir.eachFileRecurse FILES, {
      if (it.name == fileName) { fileList << it }
    }
    return fileList
  }

}
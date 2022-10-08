package muddler.mudlet.packages
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import static groovy.io.FileType.*
import java.util.regex.Pattern
import muddler.Echo


abstract class Package {
  String basePath
  File baseDir
  List files
  List children
  def e

  abstract def newItem(Map options)

  def Package(String packageType ) {
    this.e = new Echo()
    e.echo("Scanning for $packageType")
    this.basePath = "build/filtered/src/$packageType"
    this.baseDir = new File(this.basePath)
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
      // We don't want to include build/filtered/src/ in the path, so remove
      // the basePath prefix from each filename
      def quotedBasePath = Pattern.quote("${this.basePath}${File.separator}")
      def relativeToBase = "${it}".replaceFirst("^${quotedBasePath}" , "")
      def relativePath = relativeToBase.split(Pattern.quote(File.separator)).toList()

      def directoriesInPath = relativePath[0..<-1]
      def filePath =  directoriesInPath.join(File.separator)
      def fileName = relativePath.join(File.separator)

      def itemPayload = []
      def itemArray = []
      def jsonItems
      try {
        jsonItems = new JsonSlurper().parse(it)
      } catch (groovy.json.JsonException ex) {
        e.error("There was an error reading the json file ./$fileName:", ex)
      }
      jsonItems.each {
        it.path = filePath
        itemPayload.add(newItem(it))
      }

      if (directoriesInPath.isEmpty()) {
        this.children.addAll(itemPayload)
      } else {
        directoriesInPath.each {
          def properties = [:]
          properties.isFolder = "yes"
          properties.name = it
          itemArray.add(newItem(properties))
        }

        itemArray.add(itemPayload)
        fullItemsAsArrays.add(itemArray)
      }
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
      if (mergedList.size() == 0 ) {
        return mergeInto
      } else {
        return mergeDown(mergedList, mergeInto)            
      }
    }
  }

  def fullMerge(ArrayList toMerge) {
    def mergedList = mergeDown(toMerge)
    mergedList.collect {
      if (it.children.size() > 1) {
        def newItems = fullMerge(it.children)
        it.children = newItems
        return it
      } else {
        return it
      }
    }
    return mergedList
  }

  def fileToRelativePath(file) {
    return "${file}".split(Pattern.quote(File.separator)).toList()[2..-1].join(File.separator)
  }
  
  def findFiles(fileName) {
    def fileList = []
    this.baseDir.eachFileRecurse FILES, {
      if (it.name == fileName) { 
        e.echo("Found ./${fileToRelativePath(it)}")
        fileList << it 
      }
    }
    return fileList
  }

}

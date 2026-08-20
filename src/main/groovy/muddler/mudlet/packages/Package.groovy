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
    this.basePath = "build${File.separator}filtered${File.separator}src${File.separator}$packageType${File.separator}"
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
      def quotedBasePath = Pattern.quote("${this.basePath}")
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
          properties.path = filePath
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
        fileList << it
      }
    }
    // Shallowest first, so a directory's own json is always read before those
    // of its subdirectories. createItems() adds items in the order it reads
    // them and mergeDown() keeps the position of the first occurrence, so a
    // group declared in its parent's json only holds its declared position if
    // that json was read first. Left in filesystem order, a subdirectory read
    // earlier would place the group by name instead, silently reordering
    // siblings.
    //
    // Ties are broken by path so the result does not depend on the filesystem.
    // eachFileRecurse returns directory entries in whatever order the platform
    // gives: alphabetical on NTFS, effectively arbitrary on ext4. A group that
    // no parent json declares takes its position from read order, so without a
    // total order here the same sources build a differently ordered package on
    // a different machine.
    fileList = fileList.sort { a, b ->
      def depth = { "${it}".split(Pattern.quote(File.separator)).size() }
      depth(a) <=> depth(b) ?: "${a}" <=> "${b}"
    }
    fileList.each { e.echo("Found ${fileToRelativePath(it)}") }
    return fileList
  }

}

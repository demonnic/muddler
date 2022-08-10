package muddler
import muddler.Echo

class Generator {
  def console = System.console()
  def e = new Echo()
  def projectDir
  def projectName
  def projectVersion
  def projectAuthor
  def projectTitle
  def needScript
  def needAlias
  def needTrigger
  def needTimer
  def needKey
  def needIgnore

  def create(allDefaults = false) {
    if (!allDefaults) {
      gatherInfo()
      reviewInfo()
    } else {
      projectName = "TemplateProject"
      projectDir = new File("./$projectName/")
      projectVersion = "1.0.0"
      projectAuthor = "ShrinkingViolet"
      projectTitle = "$projectName by $projectAuthor"
      needScript = "y"
      needAlias = "y"
      needTrigger = "y"
      needTimer = "y"
      needKey = "y"
      needIgnore = "y"
    }

    if (projectDir.exists()) projectDir.deleteDir()
    projectDir.mkdirs()
    writeMFile()
    writeReadme()
    if (needIgnore == "y") writeIgnoreFile()
    if (needScript == "y") writeScript()
    if (needAlias == "y") writeAlias()
    if (needTrigger == "y") writeTrigger()
    if (needTimer == "y") writeTimer()
    if (needKey == "y") writeKey()

  }

  def gatherInfo() {
    println "Please provide the following information to help us generate your project skeleton."
    println "The defaults will be provided in [], if you enter nothing the default will be used.\n"
    projectName = getValue("Project name")
    if (projectName == "") {
      println "You must provide a project name, as it is used as the root directory and the package name in your generated project."
      System.exit(42)
    }
    projectDir = new File("./$projectName/")
    if (projectDir.exists()) {
      def startOver = getValue("File or directory $projectName already exists, cast it into the void (delete it) and start from scratch? y/n [N]")
      if (startOver.toLowerCase() != "y") {
        println "Preserving the existing file or directory and exiting."
        System.exit(42)
      }
      println "Will remove the old project directory and creating new template."
    }

    projectVersion = getValue("What version would you like to start at? [1.0.0]")
    projectAuthor = getValue("By what name do you wish to be known? [ShrinkingViolet]")
    projectTitle = getValue("One sentence description of your package? [$projectName by $projectAuthor]")
    println "\nFor the following questions, answer 'y' if you want the category setup with an example item of its type, and 'n' if you do not. You can always add them later if you need them. Default shown in []"

    println "Would you like:"
    needScript = getValue("Scripts [y]")
    needAlias = getValue("Aliases? [n]")
    needTrigger = getValue("Triggers? [n]")
    needKey = getValue("Keybindings? [n]")
    needTimer = getValue("Timers? [n]")
    needIgnore = getValue("\nA .gitignore file for muddler generated files? [y]")
  }

  def reviewInfo() {
    println "Now then, to review:"
    println """
Name: $projectName
Version: $projectVersion
Author: $projectAuthor
Title: $projectTitle

Will be created with default examples for the following types:
Script  : $needScript
Alias   : $needAlias
Trigger : $needTrigger
Key     : $needKey
Timer   : $needTimer

"""
    def confirmation = getValue("Sure you want to generate the package? y/n [y]")

    if (confirmation.toLowerCase() != "y") {
      println "Understood, doing a dry run. Next time... next time..."
      System.exit(42)
    }
  }

  def writeMFile() {
    def mfileText = """{
  "package": "$projectName",
  "version": "$projectVersion",
  "author": "$projectAuthor",
  "title": "$projectTitle",
  "outputFile": true
}
"""
    writeFile(projectDir, 'mfile', mfileText)
  }

  def writeIgnoreFile() {
    def ignoreText = """build/
.output
"""
    writeFile(projectDir, '.gitignore', ignoreText)
  }

  def writeReadme() {
    def text = """# $projectName

## $projectTitle

This is a template project created by muddler. It's meant to give you the basic skeleton to get started.
It is not a complete project, nor does it provide an example of every type of trigger scenario or keybinding corner case. It would make it even more difficult to clear out to make way for your own items.
It **will** properly muddle and create an mpackage, however.
For more detailed information on describing your triggers, scripts, etc in the json files, please see the [muddler wiki](https://github.com/demonnic/muddler/wiki)

This space is where I would normally put the description of my package and what it does/why I made it. But if you have a README format you already like, feel free to ignore all this.

## Installation

It's a good idea to provide installation instructions. I like to include a command they can copy/paste into the Mudlet commandline. Like

`lua uninstallPackage("packageName") installPackage("https://somedomain.org/path/to/my/package/packageName.mpackage")`

## Usage

Brief introduction to the overall usage. Then break it down to specifics

### Aliases

* `alias1 <param1>`
  * description of what the alias does, and what param1 is if it exists
    * example usage1
    * optional example usage2, etc
* `alias 2`
  * and so on, and so forth

### API

* `functionName(param1, param2)
  * Then, do the same thing for any Lua API which you want them to be able to use.
  * This part can be skipped if you have separate API documentation, but keep in mind the README.md file is accessible from the package manage in Mudlet, so this allows you to provide documentation within Mudlet, to a degree.

## Final thoughts, how to contribute, thanks, things like that

I like to put anything which doesn't fit with the above stuff here, at the end. It keeps the documentation like stuff at the top.
"""
    writeFile(projectDir, "README.md", text)
  }

  def writeScript() {
    def scriptJson = """[
  {
    "name": "${projectName}_example_script",
    "eventHandlerList": [
      "sysInstall"
    ]
  }
]
"""
    def scriptFileString = """-- define ${projectName}_example_script() for use as an event handler
function ${projectName}_example_script(event, ...)
  echo("Received event " .. event .. " with arguments:\\n")
  display(...)
end
"""
    writeCategory("scripts", scriptJson, scriptFileString, "${projectName}_example_script.lua")
  }

  def writeAlias() {
    def aliasJson = """[
  {
    "name": "$projectName",
    "regex": "^$projectName\$"
  }
]
"""

    def aliasLua = """echo([[
  A circus performer named Brian
  Once smiled as he rode on a lion
  They came back from the ride,
  But with Brian inside,
  And the smile on the face of the lion!
]])
"""
    writeCategory("aliases", aliasJson, aliasLua)
  }

  def writeTimer() {
    def timerJson = """[
  {
    "name": "$projectName anti idle",
    "minutes": "15"
  }
]
"""
    def timerLua = """if not hasFocus() then
  send("ql")
end
"""
    writeCategory("timers", timerJson, timerLua, "${projectName}_anti_idle.lua")
  }

  def writeKey() {
    def keyJson = """[
  {
    "name": "Clearscreen",
    "keys": "ctrl+shift+alt+c"
  }
]
"""
    def keyLua = "clearWindow()"
    writeCategory("keys", keyJson, keyLua, "Clearscreen.lua")
  }

  def writeTrigger() {
    def triggerJSON = """[
  {
    "name": "Rats",
    "isFolder": "yes",
    "children": [
      {
        "name": "Rat in",
        "patterns": [
          {
            "pattern": "^With a squeak, an? .*rat darts into the room, looking about wildly.\$",
            "type": "regex"
          },
          {
            "pattern": "^Your eyes are drawn to an? .*rat that darts suddenly into view.\$",
            "type": "regex"
          },
          {
            "pattern": "^An? .*rat wanders into view, nosing about for food.\$",
            "type": "regex"
          },
          {
            "pattern": "^An? .*rat noses its way cautiously out of the shadows.\$",
            "type": "regex"
          }
        ],
        "script": "echo('rat entered!')"
      },
      {
        "name": "Rat out",
        "patterns": [
          {
            "pattern": "^An? .*rat darts into the shadows and disappears.\$",
            "type": "regex"
          },
          {
            "pattern": "^An? .*rat wanders back into its warren where you may not follow.\$",
            "type": "regex"
          },
          {
            "pattern": "^With a flick of its small whiskers, an? .*rat dashes out of view.\$",
            "type": "regex"
          }
        ]
      }
    ]
  },
  {
    "name": "Equilibrium",
    "highlight": "yes",
    "highlightFG": "#00aa7f",
    "highlightBG": "#aa00ff",
    "patterns":[
      {
        "pattern": "Regained Equilibrium",
        "type": "substring"
      }
    ]
  },
  {
    "name": "Everything green",
    "patterns": [
      {
        "pattern": "2,0",
        "type": "color"
      }
    ]
  }
]
"""
    def triggerLua = "echo('Rat out!')"
    def eqLua = "cecho('<cyan>Equilibirum has been regained!\\n')"
    def greenLua = 'debugc("A green line came in! We are tracking that as part of project @PKGNAME@")'
    writeCategory("triggers", triggerJSON, triggerLua, "Rat_out.lua")
    e.echo("Writing additional trigger lua files")
    def folder = new File(projectDir, "src/triggers/$projectName")
    writeFile(folder, "Equilibrium.lua", eqLua)
    writeFile(folder, "Everything_green.lua", greenLua)
    e.echo("Extra trigger lua files written")
  }

  def writeCategory(catName, jsonString, luaString, luaFileName = "") {
    if (luaFileName == "") luaFileName = "${projectName}.lua"
    e.echo "Writing out example $catName template"
    def folder = new File(projectDir, "src/$catName/$projectName")
    writeFile(folder, "${catName}.json", jsonString)
    writeFile(folder, luaFileName, luaString)
    e.echo "$catName generation completed successfully"
  }

  def writeFile(folder, fileName, contents) {
    if (!folder.exists()) folder.mkdirs()
    try {
      new File(folder, fileName).withWriter { writer ->
        writer.write(contents)
        writer.flush()
      }
    } catch (Exception ex) {
      e.error("Error writing $folder/$fileName:", ex)
    }
  }

  def getValue(prompt) {
    def defaultValue = (prompt =~ /.* \[(.+)\]$/).with { matches() ? it[0][1] : ""}
    def val = console.readLine("$prompt: ").trim()
    if (val == "") {
      return defaultValue
    }
    return val
  }
}
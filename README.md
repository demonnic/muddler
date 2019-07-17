# muddler
muddler is a build tool for Mudlet package developers. It aims to provide a development environment that will feel familiar for many developers and also be intuitive enough to pick up for those who have primarily done scripting for Mudlet.

## Goals and/or features
* Convention over configuration
  * Provide a standard directory and file structure for 'compiling' files into a Mudlet package
  * Allow the editing of your project and code in the editor of your choice, while still producing Mudlet objects
* Provide for the description of all standard mudlet objects in json format
* In particular allow for describing triggers in a clear Parent<->child manner, with all the options available in the UI in a json file. This is something I find it particularly onerous to do in pure Mudlet lua.
* provide a file which can be distributed across the platforms Mudlet is available for (Windows/OSX/Linux) and act as a build tool similar to maven or gradle. Currently I am creating an uber jar. 

## Usage
muddler largely relies upon adherence to the muddler convention. For instance, the filetree for animated timers looks like this:

```
C:\USERS\DMONOGUE\GITBOX\MUDDLER\TESTMUDDLE\ANITIMERS
│   mfile
│
└───src
    ├───aliases
    │   └───AnimatedTimers
    │           aliases.json
    │           anitimer_demo.lua
    │
    ├───resources
    │       test1.jpg
    │       test5.jpg
    │
    ├───scripts
    │   └───AnimatedTimers
    │           code.lua
    │           scripts.json
    │
    └───timers
        └───AnimatedTimers
                animate.lua
                timers.json
```
Once your project is configured, simply `muddle` in the root directory and it will create a build directory, inside of which will be the .xml and .mpackage files it created.

There's more information in the wiki (if there isn't, sorry), be sure to check it out for better documentation.

## Currently working
* Scripts
* Triggers
* Aliases
* Timers

## To-do
* Buttons
* Keybindings
* Option to pull mudlet package apart and produce muddler project
* I'm sure plenty of bug fixes

## Contributing
### Prerequisites
* Java JDK 8
* A Text Editor
* And a dream!
  * Ok maybe some groovy and/or java knowledge would help

### Building
`./gradlew clean shadowJar` produces an executable jar file with all of the depenendencies. This can be run using `java -jar </path/to/jarfile`

### Pull Requests
TODO: probably just don't be a dick and make sure you include information on how you tested it

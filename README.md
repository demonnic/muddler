# muddler
muddler is a build tool for Mudlet package developers. It aims to provide a development environment that will feel familiar for many developers and also be intuitive enough to pick up for those who have primarily done scripting for Mudlet.

## Goals and/or features
* Convention over configuration
  * Provide a standard directory and file structure for 'compiling' files into a Mudlet XML package file
  * Allow the editing of your project and code in the editor of your choice, while still producing Mudlet objects
  * Provide for the description of all standard mudlet objects in json format
  * In particular allow for describing triggers in a clear Parent<->child manner, with all the options available in the UI in a json or yaml file. This is something I find it particularly onerous to do in pure lua
* provide a file which can be distributed across the platforms Mudlet is available for (Windows/OSX/Linux) and act as a build tool similar to maven or gradle. This should allow you to run `muddler build` to compile your project into a .XML file and `muddler clean` to remove all generated files at a minimum.
* Code will be unit tested using examples exported from multiple versions of Mudlet in order to do our best to ensure maximum compatibility

## Usage
muddler largely relies upon adherence to the muddler convention. Your project should include a `src` folder, and inside this `src` folder you should include the folders you wish to include in your Mudlet package. For instance, if you wish to create a script object 
containing the code in my_script.lua inside package named MyPackage, in a folder named SuperCoolPackage, you would setup your file 
structure like so:

```
src
--MyPackage
  --SuperCoolPackage
    --my_script.lua
```

The script object name will match the filename. In this case my_script. If you wanted to have some triggers in a folder called SuperCoolPackage as well, then you would describe those triggers in a file called either triggers.yml or triggers.json, and your filestructure would look like this:

```
src
--MyPackage
  --SuperCoolPackage
    --my_script.lua
      triggers.json
```

And so on and so forth with Aliases, Timers, Buttons, and Keybindings.

## Contributing
### Prerequisites
* Java JDK 8
* A Text Editors
* And a dream!
  * Ok maybe some groovy and/or java knowledge would help

### Building
`./gradlew clean test shadowJar` produces an executable jar file with all of the depenendencies. 
TODO: take that jar and embed it into a script and/or executable for ease of installation/use
### Pull Requests
TODO: probably just don't be a dick and make sure you include information on how you tested it

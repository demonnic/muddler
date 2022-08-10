# What is muddler?

muddler is a build tool for Mudlet package developers. It aims to provide a development environment that will feel familiar for many developers and also be intuitive enough to pick up for those who have primarily done scripting for Mudlet.

## Ok, but what does that even mean?

It means you can edit your Mudlet scripts in the IDE or text editor of your choice with all your usual tools and shortcuts, and muddler will then take the lua and json files from your project layout, and turn them into a Mudlet mpackage.

Other solutions for editing Mudlet scripts inside your own editor have tended to involve either creating and managing a ton of tempTrigger/Aliases/etc instead of creating them as permanent items in the Mudlet editor, or watching/reading a lua files contents into a script/trigger/alias/etc in the Mudlet editor. Muddler attempts to be indistinguishable from items created in the Mudlet script editor once imported into Mudlet, while still providing access to all the usual options available for your triggers and the like inside Mudlet itself. All while giving you the full capabilities of your favorite code editor.

This also produces cleaner diffs for managing a project in git, whether for collaboration or just source code/version management.

## Goals and/or features

More complete documentation is available on the [wiki](https://github.com/demonnic/muddler/wiki), but a rundown of the basics follows:

* Convention over configuration
  * Provide a standard directory and file structure for 'compiling' files into a Mudlet package
  * Allow the editing of your project and code in the editor of your choice, while still producing Mudlet objects
* Provide for the description of all standard mudlet objects in json format
* In particular allow for describing triggers in a clear Parent<->child manner, with all the options available in the UI in a json file. This is something I find it particularly onerous to do in pure Mudlet lua.
* provide a file which can be distributed across the platforms Mudlet is available for (Windows/OSX/Linux) and act as a build tool similar to maven or gradle. This is being accomplished using the [gradle shadow plugin](https://github.com/johnrengelman/shadow) to create a fat jar during testing and distribution archives containing start scripts for multiple platforms.

## Usage

First, if you need help come find me on my discord: <https://discord.gg/SRRBpbxe34>
It is probably the fastest way to get ahold of me and get a resolution to your issue.
Second, starting with muddler 0.13 you can now use the `--generate` switch to interactively create a file tree, or `--default` to create a full, default file tree to start with. You should still familiarize yourself with the following information though.

muddler largely relies upon adherence to the muddler convention. For instance, the filetree for animated timers looks like this:

```txt
C:\ANITIMERS
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

## Currently working

* Scripts
* Triggers
* Aliases
* Timers
* Keybindings

## To-do

* Package uploads (with Mudlet package for accessing/searching the uploaded packages)
* Option to pull mudlet package apart and produce muddler project
* Variables? (is anyone using this in distribution packages?)
* Buttons
* I'm sure plenty of bug fixes

## Contributing

### Prerequisites

* Java JDK 8
* A Text Editor
* And a dream!
  * Ok maybe some groovy and/or java knowledge would help

I build using JDK8, but I have tested using jdk 8, 11, 17, and 18.

### Building

`./gradlew clean shadowJar` produces an executable jar file with all of the depenendencies. This can be run using `java -jar </path/to/jarfile`

Alternately, `./gradlews clean dockerTest` will create the demonnic/muddler:test docker image locally for you to use in testing.

### Pull Requests

TODO: probably just don't be a dick and make sure you include information on how you tested it

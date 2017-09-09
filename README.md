# GoldRushChicken
Bukkit/Spigot MC Plugin to utilize own AI (MC 1.12.1)
Using NMSUtils from jetp250 (https://www.spigotmc.org/members/jetp250.105311/)

## Getting Spigot MC
```bash
wget "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar" -O BuildTools.jar
java -jar BuildTools.jar --rev 1.12.1
```
Place generated `spigot-1.12.1.jar` in `spigotmc` folder. See below for directory structure for use with IntelliJ IDE.

## IntelliJ
If you don't use IntelliJ simply ignore the folder `.idea` and the `GoldRushChicken.iml` project file. The project uses spigot jar and java sdk as module dependencies.

The directory structure for the IntelliJ project:
```
$HOME/Documents/GoldRushChicken:
  * out:           IntelliJ build files
  * server:        place spigot jar here for execution
  * server/plugin: generated plugin jar goes here by calling "Build artifacts"
  * spigotmc:      generate spigot jar here by executing the build tools, used as module dependency
  * mc:            place mc client jar here for execution
  * src:           source files
```
`$HOME` is detected on both windows and linux.

## Debugging plugin
Start the server with the following command for remote debugging:
```
java -Xmx2G -Xms2G -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar ./spigot-1.12.1.jar
```

## Usage ingame
command `/grc` spawns a gold rush chicken near the player

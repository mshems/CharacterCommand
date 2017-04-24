# CharacterCommand
### A Command-Line D&D Character Manager.
---
#### Features:
* Inventory Management
* Equipment
* Skills
* Spells & Spell Casting
* Import/Export Characters

---
#### How to Launch:
Run from source files:
> java -cp ./bin app.CCommand

Run from .JAR file:
> java -jar CCommand.jar
---
#### Overview:
Everything is done by entering certain commands. "stats" displays basic info about your character, "inv" displays your inventory, etc...
Many commands also have additional arguments. 
For example:
> inv get item Potion \*3

This command adds three items named "Potion" to your inventory. 

Entering "help" will display an abbrevieated guide. 
The file COMMANDS.txt contains a full list of commands and their arguments and usages. 

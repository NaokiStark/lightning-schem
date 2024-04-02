##Lightning Schematic

I made this plugin to help a friend with a server

###Explain your plugin kid

Ok, this paste Schematics (Sponge v1 & v2) using Bukkit Scheduler and a Queue (like AsyncWorldEdit queue)

--ok kid but exist AsyncWorldEdit to do that

Yessss but AsyncWorldEdit use async Task, the problem with this is `BlockDataInfo.update()`  can be only called in the main thread. 

LS uses the main thread to place blocks and uses Task Timer to place `x` of blocks, this improves time (because don't need to access to main thread on another thread) and the other methods (open file and process) are on another thread

###I want to test this gbge

This use Spigot 1.16.R3 because NBT (can be changed in Schematic.java)
Use Intellij to build

###Commands

`/golpear <schem> <x y z> <world> <endMsg>`

- `<shem>` schematic file without extension
- `<x y z>` coords
- `<world>` world used to place blocks
- `<endMsg>` send a msg when blocks are placed entirely (to be used by skript??)

###I have an Issue

I upload this repo in order to be archived, I don't accept any issue.

If you want this updated running on your server, please contact me to nikumi[💥]hotmail.com
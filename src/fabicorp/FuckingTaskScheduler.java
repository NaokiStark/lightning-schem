package fabicorp;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class FuckingTaskScheduler extends BukkitRunnable {

    @Override
    public void run() {
        LSTaskQueue lst = LSTaskQueue.getInstance();

        SchemTask currentTask = Schematic.getInstance().currentTask;

        // First time in queue
        if(currentTask == null){
            if(lst.schemTasks.size() > 0){
                // ADD A NEW TASKO to wORK
                //
                Schematic.getInstance().workTask(lst.schemTasks.poll());
            }
        }
        else{
            // WE LOVE SPAGHETTI CODE
            if(currentTask.ended){
                // IN CASE OF WEIRD BUG O9MG
                if(lst.schemTasks.size() > 0){
                    Bukkit.getServer().getLogger().info("[LightningSchem] Task "+ currentTask.schemFile.getName() +" done!");
                    if(lst.schemTasks.size() < 1){
                        Bukkit.getServer().getLogger().info("[LightningSchem] Starting place queued blocks");
                    }
                    else{
                        // ADD A NEW TASKO to wORK
                        //
                        Schematic.getInstance().workTask(lst.schemTasks.poll());
                    }
                }
            }
        }
    }
}

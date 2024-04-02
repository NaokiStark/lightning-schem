package fabicorp;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LSTaskQueue {

    private static LSTaskQueue instance;

    private static int interval = 1;

    private FuckingTaskScheduler fuckingTaskScheduler;

    public static LSTaskQueue getInstance(){
        if(instance == null) {
            instance = new LSTaskQueue();
        }
        return instance;
    }

    Queue<SchemTask> schemTasks = new LinkedList<SchemTask>();

    public LSTaskQueue(){
        fuckingTaskScheduler = new FuckingTaskScheduler();
        startQueue();
    }

    public void AddTask(SchemTask task){
        Bukkit.getServer().getLogger().info("[LightningSchem] Adding task: " + task.schemFile.getName());

        schemTasks.add(task);
    }

    private void startQueue(){

        // Burkit thread handled timing because is more "safer & shittier"
        fuckingTaskScheduler.runTaskTimer(LightningSchem.Instance,0, 20 * interval);
    }

    // shutDOWN on server shutDOWN
    public void stopQueue(){
        fuckingTaskScheduler.cancel();
    }


}

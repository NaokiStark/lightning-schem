package fabicorp;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LightningSchem extends JavaPlugin{
	
	public final static Server s = Bukkit.getServer();
	public final static Logger l = s.getLogger();
	public Schematic sc;
	public static FileConfiguration config;

	public static long TickRate = 4l;
	public static int  blockXTick = 150000;

	public static LightningSchem Instance;
	
	public LightningSchem() {
		Instance = this;
	}
	
	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		config = this.getConfig();
		sc = new Schematic(s);
		l.info("Cargado.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
		LSTaskQueue.getInstance().stopQueue();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("golpear")) {
			if(sender.hasPermission("lightningschem.golpear") || !(sender instanceof Player)) {
				
				if(args.length < 5) {
					l.warning("Usage: \n\n /golpear <schem> <x y z> <world> <endMsg>");
					return false;
				}
				
				World wrld = s.getWorld(args[4]);
				
				if(wrld == null) {
					l.warning("World not found: " + args[7]);
					return false;
				}
				
				Location loc = new Location(wrld, Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]));
				
				String thisPath = "." + File.separator + this.getDataFolder() + File.separator + "schem" + File.separator;
				thisPath += args[0] + ".schematic";
				
				File f = new File(thisPath);
				if(!f.exists()) {
					l.warning("Schematic not found: " + thisPath);
					return false;
				}
				
				//sc.pasteSchematicAsync(f, loc);

				// Queued task
				String endCommand = "";

				if(args.length > 5){
					endCommand = args[5];
				}

				LSTaskQueue.getInstance().AddTask(new SchemTask(f, loc, endCommand));
				
				return true;
			}
		}
		
		return false;
	}
}

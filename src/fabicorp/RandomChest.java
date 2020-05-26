package fabicorp;

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

public class RandomChest extends JavaPlugin{
	
	public final static Server s = Bukkit.getServer();
	public final static Logger l = s.getLogger();
	public ChestWorker cw;
	public static FileConfiguration config;
	
	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		config = this.getConfig();
		cw = new ChestWorker(s);
		l.info("Cargado.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("rellenarcofres")) {
			if(sender.hasPermission("randomchest.rellenarcofres") || !(sender instanceof Player)) {
				
				if(args.length < 8) {
					l.warning("Uso incorrecto. \n /rellenarcofres Coord1X Coord1Y Coord1Z Coord2X Coord2Y Coord2Z Nivel_Cofres World");				
					return false;
				}
				
				World wrld = s.getWorld(args[7]);
				
				if(wrld == null) {
					l.warning("El mundo no existe o no está cargado: " + args[7]);
					return false;
				}
				
				Location loc = new Location(wrld, Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]));
				Location loc2 = new Location(wrld, Double.valueOf(args[3]), Double.valueOf(args[4]), Double.valueOf(args[5]));
				
				cw.fillChests(loc, loc2, args[6]);
				
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("chestreload")) {
			if(sender.hasPermission("randomchest.rellenarcofres") || !(sender instanceof Player)) {
				this.reloadConfig();
				config = this.getConfig();
				return true;
			}
		}
		
		return false;
	}
	
}

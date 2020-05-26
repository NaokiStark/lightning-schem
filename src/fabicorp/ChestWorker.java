package fabicorp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestWorker {

	public Server s;
	
	public ChestWorker(Server server) {
		s = server;
	}	
	
	public void fillChests(Location loc, Location loc2, String level) {
		
		s.getLogger().info("Llenando cofres con nivel " + level + " en el mundo " + loc.getWorld().getName());
		
		List<String> items = getItemsForLevel(level);
		
		List<Block> blocks = getBlocks(loc, loc2);
		
		Random random = new Random();
		
		int minLimit = RandomChest.config.getInt("minLimit");
		int maxLimit = RandomChest.config.getInt("maxLimit");
		
		for(Block block : blocks) {
			BlockState state = block.getState();
			
			if(state instanceof DoubleChest) {				
				DoubleChest dbch = (DoubleChest)state;

				int rndCount = random.ints(minLimit, Math.min(maxLimit, dbch.getInventory().getSize() - 1)).findFirst().getAsInt();
				int rndLoc = random.ints(0, dbch.getInventory().getSize() - 1).findFirst().getAsInt();
				dbch.getInventory().clear();
				
				int rndItem = random.ints(0,items.size() - 1).findFirst().getAsInt();
				for(int a = 0; a < rndCount; a++) {
					List<String> itemData = parseItem(items.get(rndItem));
					Material mtr = Material.getMaterial(itemData.get(0));
					
					try {
						ItemStack item = new ItemStack(mtr);
						for(int b = 1; b < itemData.size(); b++) {
							String[] parts = itemData.get(b).split(";");
							for(String part : parts) {
								String[] values = part.split(",");
								
								@SuppressWarnings("deprecation")
								Enchantment ench = Enchantment.getByName(values[0]);
								
								item.addEnchantment(ench, Integer.valueOf(values[1]));
								if(values.length > 2) {
									ItemMeta meta = item.getItemMeta();
									meta.setDisplayName(values[2]);
																		
									if(values.length > 3) {
										List<String> lore = new ArrayList<String>();
										lore.add(values[3]);
										meta.setLore(lore);
									}			
									item.setItemMeta(meta);
								}
							}
						}
						while(dbch.getInventory().getItem(rndLoc) != null) {
							rndLoc = random.ints(0, dbch.getInventory().getSize() - 1).findFirst().getAsInt();
						}
						dbch.getInventory().setItem(rndLoc, item);
					}
					catch(Exception ex) {
						s.getLogger().warning(ex.getMessage());
						s.getLogger().warning(itemData.get(0));
					}					
					rndItem = random.ints(0,items.size()).findFirst().getAsInt();
				}
			}
			else if(state instanceof Chest) {
				Chest ch = (Chest)state;
				int rndCount = random.ints(minLimit, Math.min(maxLimit, ch.getInventory().getSize() - 1)).findFirst().getAsInt();
				ch.getInventory().clear();
				
				int rndItem = random.ints(0,items.size() - 1).findFirst().getAsInt();
				int rndLoc = random.ints(0, ch.getInventory().getSize() - 1).findFirst().getAsInt();
				
				for(int a = 0; a < rndCount; a++) {
					List<String> itemData = parseItem(items.get(rndItem));
					Material mtr = Material.getMaterial(itemData.get(0));
					try {
																		
						ItemStack item = new ItemStack(mtr);
						for(int b = 1; b < itemData.size(); b++) {
							String[] parts = itemData.get(b).split(";");
							for(String part : parts) {
								String[] values = part.split(",");
								
								@SuppressWarnings("deprecation")
								Enchantment ench = Enchantment.getByName(values[0]);
								
								item.addEnchantment(ench, Integer.valueOf(values[1]));
								if(values.length > 2) {
									ItemMeta meta = item.getItemMeta();
									meta.setDisplayName(values[2]);
																		
									if(values.length > 3) {
										List<String> lore = new ArrayList<String>();
										lore.add(values[3]);
										meta.setLore(lore);
									}			
									item.setItemMeta(meta);
								}
							}
						}
						while(ch.getInventory().getItem(rndLoc) != null) {
							rndLoc = random.ints(0, ch.getInventory().getSize() - 1).findFirst().getAsInt();
						}
						ch.getInventory().setItem(rndLoc, item);
					}
					catch(Exception ex) {
						s.getLogger().warning(ex.getMessage());
						s.getLogger().warning(itemData.get(0));
					}		
					
					rndItem = random.ints(0,items.size()).findFirst().getAsInt();
				}
			}
		}
	}
	
	public List<String> getItemsForLevel(String level){
		return RandomChest.config.getStringList("chest."+level);
	}
	
	
	public List<Block> getBlocks(Location pos1, Location pos2)
	{
		s.getLogger().info("Buscando");

	  
	  World world = pos1.getWorld();
	  List<Block> blocks = new ArrayList<>();
	  int x1 = pos1.getBlockX();
	  int y1 = pos1.getBlockY();
	  int z1 = pos1.getBlockZ();

	  int x2 = pos2.getBlockX();
	  int y2 = pos2.getBlockY();
	  int z2 = pos2.getBlockZ();

	  int lowestX = Math.min(x1, x2);
	  int lowestY = Math.min(y1, y2);
	  int lowestZ = Math.min(z1, z2);

	  int highestX = (lowestX == x1) ? x2 : x1;
	  int highestY = (lowestY == y1) ? y2 : y1;
	  int highestZ = (lowestZ == z1) ? z2 : z1;
	  
	  s.getLogger().info(lowestX + ":" + highestX);
	  s.getLogger().info(lowestY + ":" + highestY);
	  s.getLogger().info(lowestZ + ":" + highestZ);

	  for(int x = lowestX; x <= highestX; x++) {
	    for(int y = lowestY; y <= highestY; y++) {
	      for(int z = lowestZ; z <= highestZ; z++) 
	      {
	    	Block bk = world.getBlockAt(x, y, z);	 
	    	BlockState state = bk.getState();
	    	if(state instanceof Chest || state instanceof DoubleChest) {
	    		s.getLogger().info("Cofre en " + x + " " + y+ " " + z);
	    		blocks.add(bk);
	    	}
	      }
	    }
	  }
	  s.getLogger().info(blocks.size()+" ");
	  return blocks;
	}
	
	public List<String> parseItem(String item){
		
		List<String> result = new ArrayList<String>();
		
		String[] parts = item.split(":");
		String mId = parts[0];
		result.add(mId);
		if(parts.length > 1) {
			String[] enchants = parts[1].split(";");
			
			for(String enchant : enchants) {
				result.add(enchant);
			}		
		}
		
		return result;
	}
}

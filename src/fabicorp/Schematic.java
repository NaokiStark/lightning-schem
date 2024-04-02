package fabicorp;

import java.io.File;
import java.io.FileInputStream;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;

//import net.minecraft.server.v1_16_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
//import net.minecraft.server.v1_16_R1.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class Schematic {

	private static Server s;

	private StopWatch stw;
	// Oh my god
	private static Material[] byId;

	private boolean inAsyncTask = false;

	public SchemTask currentTask = null;

	private static Schematic instance;

	public static Schematic getInstance(){
		if(instance == null) {
			instance = new Schematic(Bukkit.getServer());
		}
		return instance;
	}

	public Schematic(Server server) {
		s = server;
		// OH MY MY GOD
		//loadMaterials();
		stw = new StopWatch();
	}

	/*
	 * tELl mE wHy
	 */
	public void loadMaterials() {
		byId = new Material[0];
		for (Material material : Material.values()) {
			if (byId.length > material.getId()) {
				byId[material.getId()] = material;
			} else {
				byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
				byId[material.getId()] = material;
			}
		}
	}

	/*
	 * XD
	 */
	public static Material getMaterial(final int id) {
		if (byId.length > id && id >= 0) {
			return byId[id];
		} else {
			return null;
		}
	}

	public void workTask(SchemTask task){
		Bukkit.getServer().getLogger().info("[LightningSchem] Woking on "+ task.schemFile.getName() +"! owo");

		currentTask = task;

		if(task.isAsync){
			pasteSchematicAsync(task.schemFile, task.location, task.endMessage);

		}
		else{
			pasteSchematic(task.schemFile, task.location, task.endMessage);
			currentTask.ended = true;
		}


	}

	public void pasteSchematicAsync(final File f, final Location loc, String endMessage) {
		s.getLogger().info("[LightningSchem] Starting second thread");
		s.getScheduler().runTaskAsynchronously(LightningSchem.Instance, new Runnable() {
			@Override
			public void run() {
				inAsyncTask = true;
				pasteSchematic(f, loc, endMessage);
				inAsyncTask = false;
				s.getLogger().info("[LightningSchem] Colocando bloques al queue");
				currentTask.ended = true;
			}
		});
	}

	public List<Location> pasteSchematic(File f, Location loc, String endMessage) {

		s.getLogger().info("[LightningSchem] Starting to place blocks");
		stw.start();
		List<Location> locations = new ArrayList<Location>();

		try {
			FileInputStream fis = new FileInputStream(f);
			NBTTagCompound nbt = NBTCompressedStreamTools.a(fis);

			short width = nbt.getShort("Width");
			short height = nbt.getShort("Height");
			short length = nbt.getShort("Length");

			byte[] blocks = nbt.getByteArray("Blocks");
			byte[] data = nbt.getByteArray("Data");

			byte[] blockData = nbt.getByteArray("BlockData");
			NBTTagCompound palette = nbt.getCompound("Palette");

			s.getLogger().info("W: " + width + ", H: " + height + ", z: " + length);
			s.getLogger().info("b: " + blocks.length);

			fis.close();

			// paste
			if (blocks.length > 0) {
				placeV1(width, height, length, blocks, data, loc);
			} else {
                //Location lof = getFuckingOffset(nbt, loc);
			    //s.getLogger().info("[LGSCH] OFFSET: " + lof.toString());
				placeV2(width, height, length, blockData, palette, loc, endMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		stw.stop(new StopWatchCallback() {
			@Override
			public void run(long milis, double seconds) {
				s.getLogger().info("End Time milliseconds: " + milis + "ms");
				s.getLogger().info("End Time milliseconds: " + seconds + "s");
			}
		});
		return locations;
	}

	public void placeV1(short width, short height, short length, byte[] blocks, byte[] data, Location loc) {
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				for (int z = 0; z < length; ++z) {

					if (inAsyncTask) {
						// Thread.sleep(2);
					}
					int index = y * width * length + z * width + x;
					final Location l = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ());
					int b = blocks[index] & 0xFF;// make the block unsigned, so that blocks with an id over 127,

					final Block block = l.getBlock();

					// s.getLogger().info("MatId: " + b);
					if (inAsyncTask) {
						s.getScheduler().runTask(LightningSchem.Instance, new Runnable() {
							@Override
							public void run() {
								block.setType(getMaterial(b));
								if (block instanceof MaterialData) {
									((MaterialData) block).setData(data[index]);
								}
							}
						});
					} else {
						block.setType(getMaterial(b));
						if (block instanceof MaterialData) {
							((MaterialData) block).setData(data[index]);
						}
					}
				}
			}
		}
	}

	private Location getFuckingOffset(NBTTagCompound tags, Location InitLoc){

	    int[] offsetParts = tags.getIntArray("Offset");

	    if(offsetParts == null){
	        offsetParts = new int[]{0, 0, 0};
        }

	    int WEOffsetX = 0;
	    int WEOffsetY = 0;
        int WEOffsetZ = 0;


        WEOffsetX = tags.getInt("WEOffsetX");
        WEOffsetY = tags.getInt("WEOffsetY");
        WEOffsetZ = tags.getInt("WEOffsetZ");

        s.getLogger().info("" + WEOffsetX +", "+ WEOffsetY + ", " + WEOffsetZ + " ||" + offsetParts.toString());

        int sumX = offsetParts[0] + WEOffsetX;
        int sumY = offsetParts[1] + WEOffsetY;
        int sumZ = offsetParts[2] + WEOffsetZ;

        return new Location(InitLoc.getWorld(), sumX, sumY, sumZ);
    }


	public void placeV2(short width, short height, short length, byte[] blockData, NBTTagCompound palette,
			Location loc, final String endMessage) {

		Map<Integer, BlockData> blocksMap = new HashMap<>();

		palette.getKeys().forEach(rawState -> {
			int id = palette.getInt(rawState);

			try {
				if(rawState.contains("wall")){
					rawState = convertWalls(rawState);
				}
				BlockData bdata = Bukkit.createBlockData(rawState);
				blocksMap.put(id, bdata);
			} catch (Exception ex) {
				ex.printStackTrace();
				s.getLogger().info(
						"[LightningSchem] minecraft:__reserved__ detected, changed with air, I dunno why occurs this but...");
				BlockData bdata = Bukkit.createBlockData("minecraft:air");
				blocksMap.put(id, bdata);
			}
		});

		int i = 0;
		int value;
		int varintLength;
		int index = 0;
		final Queue<BlockDataInfo> blockList = new LinkedList<BlockDataInfo>();
		while (i < blockData.length) {
			value = 0; // --> this will contain the *real* palette id
			varintLength = 0;

			while (true) {
				value |= (blockData[i] & 127) << (varintLength++ * 7);
				if (varintLength > 5) {
					s.getLogger().severe("Schematic corrupted");
				}
				if ((blockData[i] & 128) != 128) {
					i++;
					break;
				}
				i++;
			}

			int y = index / (width * length);
			int z = (index % (width * length)) / width;
			int x = (index % (width * length)) % width;

			final Location l = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ());
			final Block block = l.getBlock();
			BlockData bData = blocksMap.get(value); // --> Here

			blockList.add(new BlockDataInfo(block, bData));/*
			if (inAsyncTask) {
				BlockData finalBData = bData;
				s.getScheduler().runTask(LightningSchem.Instance, new Runnable() {
					@Override
					public void run() {

						if (finalBData == null) {
							block.setType(Material.AIR);
						} else {
							block.setBlockData(finalBData);
							//block.getState().update();
						}
					}
				});
			} else {

				if (bData == null) {
					block.setType(Material.AIR);
				} else {
					block.setBlockData(bData);
					//block.getState().update();
				}
			}*/
			index++;
		}
		s.getLogger().info("[LightningSchem] Colocando bloques ok espera");
		new BukkitRunnable() {
			@Override
			public void run() {
				//s.getLogger().info("[LightningSchem] Colocando 30000 bloques, " + blockList.size() + " restantes");
				int bxt = LightningSchem.Instance.getConfig().getInt("blockXTick");
				int roexb = LightningSchem.Instance.getConfig().getInt("reportOnEveryXBlocks");
				for(int a = 0; a < bxt; a++){
					if(blockList.size() % roexb == 0){
						s.getLogger().info("[LightningSchem] bloques, "+ blockList.size() + " restantes");
					}
					BlockDataInfo blockDataInfo = blockList.poll();
					if(blockDataInfo == null){
						s.getLogger().info("[LightningSchem] La wea terminada");
						//s.getLogger().info(endMessage);
						if(endMessage.length() > 0){
							s.dispatchCommand(s.getConsoleSender(), endMessage);
						}
						else{
							s.getLogger().info("No se envió ningun comando");
						}

						this.cancel();
						return; //cancel this shit
					}
					if (blockDataInfo.blockData == null) {
						blockDataInfo.block.setType(Material.AIR);
					} else {
						blockDataInfo.update();
					}
				}
			}
		}.runTaskTimer(LightningSchem.Instance,0l,LightningSchem.Instance.getConfig().getLong("tickrate"));
	}

	// WHY, TELL ME WY
	// I HATE SO MUCH MINECRAFT
	/**
	 * Converts walls to 1.16.+
	 * @param blockData
	 * @return new wall blockdata
	 */
	public String convertWalls(String blockData)
	{
		//s.getLogger().info("Converting fucking wall to 1.16.+");
		// Wall BlockData example:
		// minecraft:prismarine_wall[east=false,waterlogged=false,south=true,north=true,west=false,up=true]
		// New fixed example:
		// minecraft:prismarine_wall[east=none,waterlogged=false,south=low,north=low,west=none,up=true]

		// We assume *low* walls, because my balls want it

		String out = blockData
				// falses
				.replaceAll("east=false", "east=none")
				.replaceAll("south=false","south=none")
				.replaceAll("north=false","north=none")
				.replaceAll("west=false","west=none")
				//the trues
				.replaceAll("east=true","east=low")
				.replaceAll("south=true","south=low")
				.replaceAll("north=true","north=low")
				.replaceAll("west=true","west=low")
				;
		//s.getLogger().info(out);
		return out;
	}

}

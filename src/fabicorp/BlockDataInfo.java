package fabicorp;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class BlockDataInfo {
    public Block block;
    public BlockData blockData;
    public BlockDataInfo(Block _block, BlockData _blockData){
        block = _block;
        blockData = _blockData;
    }

    public void update(){
        this.block.setBlockData(blockData);
    }
}

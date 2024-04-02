package fabicorp;

import org.bukkit.Location;

import java.io.File;

public class SchemTask {

    public File schemFile = null;

    public Location location = null;

    public boolean isAsync = true;

    public boolean ended = false;

    public String endMessage = "";

    public SchemTask(File f, Location loc, String _endMessage) {
        this.schemFile = f;
        location = loc;
        isAsync = true;
        endMessage = _endMessage;
    }
}

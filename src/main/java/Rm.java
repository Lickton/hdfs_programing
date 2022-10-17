import org.apache.hadoop.fs.*;

import java.io.IOException;

public class Rm extends Command {
    public static final String NAME = "rm";
    public static final String USAGE = "HShell -rm [-r] <target>";
    public static final String OPTIONS = "r";
    private FileSystem fs;
    private Boolean deleteDirectory = false;
    private String[] argv;

    public Rm(FileSystem fs) {
        this.fs = fs;
    }

    public void remove(String[] argv) throws IOException {
        Command cf = new Command();
        cf.parse(argv);
        cf.checkIllegalArguments(1, (cf.getOpt("-r") ? 2 : 1), argv);
        this.deleteDirectory = cf.getOpt("-r");
        removeFile(argv[argv.length - 1]);
    }

    public void removeFile(String target) throws IOException {
        Path path = new Path(target);

        if (!this.fs.exists(path)) {
            throw new PathNotFoundException(target);
        }

        if (this.fs.isDirectory(path) && !deleteDirectory) {
            throw new PathIsDirectoryException(target);
        }

        if (!this.fs.delete(path, deleteDirectory)) {
            throw new PathIOException(target);
        } else {
            System.out.println("Deleted " + target);
        }
    }
}

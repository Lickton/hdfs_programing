import org.apache.hadoop.fs.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Cp extends Command {
    public static final String NAME = "cp";
    public static final String USAGE = "HShell -cp [-r] <local> <dst>";
    private FileSystem fs;
    private String[] argv;
    public Boolean uploadDirectory = false;

    public Cp(FileSystem fs) {
        this.fs = fs;
    }

    public void upload(String[] argv) throws IOException {
        Command cf = new Command();
        cf.parse(argv);
        cf.checkIllegalArguments(2, (cf.getOpt("-r") ? 3 : 2), argv);
        this.uploadDirectory = cf.getOpt("-r");
        uploadFile(argv[argv.length - 2], argv[argv.length - 1]);
    }

    public void uploadFile(String src, String dst) throws IOException {
        Path local = new Path(src);
        File FindLocal = new File(src);
        Path remote = new Path(dst);

        if (!FindLocal.exists()) {
            System.out.println("File or directory: `" + src + "' does not exists");
            throw new FileNotFoundException();
        }

        if (FindLocal.isDirectory() && !uploadDirectory) {
            throw new PathIsDirectoryException(src);
        }

        if (!this.fs.exists(remote)) {
            this.fs.mkdirs(remote);
        }

        if (this.fs.exists(remote)) {
            throw new FileAlreadyExistsException(dst + FindLocal.getName());
        }

        if (!this.fs.isDirectory(remote)) {
            throw new PathIsNotDirectoryException(dst);
        }

        this.fs.copyFromLocalFile(local, remote);
    }
}

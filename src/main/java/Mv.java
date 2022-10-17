import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathIsDirectoryException;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Mv extends Command {
    private static final String NAME = "mv";
    private static final String OPTIONS = null;
    private static final String USAGE = "HShell -mv <src> <dst>";
    private final FileSystem fs;
    public String[] argv;

    public Mv(FileSystem fs) {
        this.fs = fs;
    }

    public void move(String[] argv) throws IOException {
        this.argv = argv;
        Command cf = new Command();
        cf.parse(argv);
        cf.checkIllegalArguments(2, 2, argv);
        String src = argv[argv.length - 2];
        String dst = argv[argv.length - 1];
        moveFile(src, dst);
    }

    public void moveFile(String source, String destination) throws IOException {
        Path src = new Path(source);
        Path dst = new Path(destination);

        checkIllegal(src, dst);

        this.fs.rename(src, dst);
    }

    public void checkIllegal(Path src, Path dst) throws IOException {
        if (!this.fs.exists(src)) {
            displayErrorNoSuchFileOrDir(src.toString());
            throw new FileNotFoundException(src.toString());
        }

        if (this.fs.isDirectory(src)) {
            throw new PathIsDirectoryException(src.toString());
        }

        if (this.fs.exists(dst) && !this.fs.isDirectory(dst)) {
            displayErrorFileExists(dst.toString());
            throw new FileExistsException(dst.toString());
        }
    }

    public void displayErrorNoSuchFileOrDir(String target) {
        System.out.println(NAME + ": " + "`" + target + "': " + "No such file");
    }

    public void displayErrorFileExists(String target) {
        System.out.println(NAME + ": " + "`" + target + "': " + "File exists");
    }
}

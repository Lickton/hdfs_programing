import org.apache.hadoop.fs.*;

import java.io.IOException;

public class Find extends Command {
    private static final String NAME = "find";
    private static final String OPTIONS = null;
    private static final String USAGE = "HShell -find <src>";
    private FileSystem fs;
    public String[] argv;

    public Find(FileSystem fs) {
        this.fs = fs;
    }

    public void find(String[] argv) throws IOException {
        this.argv = argv;
        Command cf = new Command();
        cf.checkIllegalArguments(1, 1, argv);
        cf.parse(argv);
        String path = argv[argv.length - 1];

        if (!this.fs.exists(new Path(path))) {
            displayErrorNoSuchFileOrDir(path);
            throw new PathNotFoundException(path);
        }

        System.out.println(path);

        if (this.fs.isDirectory(new Path(path))) {
            findFile(path);
        }
    }

    public void findFile(String father) throws IOException {
        Path path = new Path(father);

        RemoteIterator<FileStatus> iterator = this.fs.listStatusIterator(path);

        while (iterator.hasNext()) {
            FileStatus fileStatus = iterator.next();
            String info = father + (father.endsWith("/") ? "" : "/") + fileStatus.getPath().getName();

            System.out.println(info);

            if (fileStatus.isDirectory()) {
                findFile(info + "/");
            }
        }
    }

    public void displayErrorNoSuchFileOrDir(String target) {
        System.out.println(NAME + ": " + "`" + target + "': " + "No such file or directory");
    }
}

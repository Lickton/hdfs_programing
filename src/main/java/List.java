import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class List extends Command {
    public static final String NAME = "list";
    public static final String OPTIONS = null;
    public static final String USAGE = "HShell -list <directory>";
    private final FileSystem fs;

    public List(FileSystem fs) {
        this.fs = fs;
    }

    public void list(String[] argv) throws IOException {
        Command cf = new Command();
        cf.checkIllegalArguments(3, 3, argv);
        cf.parse(argv);
        listFile(argv[argv.length - 1]);
    }

    public void listFile(String src) throws IOException {
        Path path = new Path(src);
        if (!this.fs.exists(path)) {
            displayErrorNoSuchFileOrDir(src);
            throw new PathNotFoundException(src);
        }

        RemoteIterator<FileStatus> iterator = this.fs.listStatusIterator(path);

        while (iterator.hasNext()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            FileStatus fileStatus = iterator.next();
            String status = (fileStatus.isDirectory() ? "d" : "-")
                    + fileStatus.getPermission() + "\t"
                    + (fileStatus.getPermission().getAclBit() ? "+" : " ")
                    + (fileStatus.isFile() ? fileStatus.getReplication() : "-") + " "
                    + fileStatus.getOwner() + "\t"
                    + fileStatus.getGroup() + "\t\t"
                    + fileStatus.getLen() + "\t"
                    + dateFormat.format(new Date(fileStatus.getModificationTime())) + "\t"
                    + src + (src.equals("/") ? "" : "/") + fileStatus.getPath().getName();
            System.out.println(status);
        }
    }

    public void displayErrorNoSuchFileOrDir(String target) {
        System.out.println(NAME + ": " + "`" + target + "': " + "No such file or directory");
    }
}

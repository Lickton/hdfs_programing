import com.sun.prism.image.CachingCompoundImage;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class HShell {
    private static FileSystem fs;
    private static Scanner sc;
    private final String NAME = "HSell";
    private static String USAGE = "HShell -[command] [options...] <src> <dst>";

    public static void main(String[] args) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException {
        init();
        String command = sc.nextLine();
        String[] argv = command.split(" ");

        switch (argv[1]) {
            case "-cp":
                Cp cp = new Cp(fs);
                cp.upload(argv);
                break;
            case "-rm":
                Rm rm = new Rm(fs);
                rm.remove(argv);
                break;
            case "-list":
                List ls = new List(fs);
                ls.list(argv);
                break;
            case "-find":
                Find find = new Find(fs);
                find.find(argv);
                break;
            case "-mv":
                Mv mv = new Mv(fs);
                mv.move(argv);
                break;
            default:
                System.out.println(argv[1] + ": Unknown command");
        }
    }

    static void init() throws URISyntaxException, IOException {
        fs = FileSystem.get(new URI("hdfs://master:9000"), new Configuration());
        sc = new Scanner(System.in);
    }

    public static Boolean check(String[] argv) {
        if (!argv[0].equals("HShell")) {
            System.out.println("Unknown Command:" + argv[0]);
            System.out.println(USAGE);
            return false;
        }

        return true;
    }
}
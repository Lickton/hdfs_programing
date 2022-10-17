import org.apache.hadoop.fs.shell.CommandFormat;

import java.util.HashMap;
import java.util.Map;

public class Command {
    private Map<String, Boolean> options;
    public Boolean isRecursion = false;
    Command() {
        this.options = new HashMap();
    }

    /**
     * Parsing Command
     */
    public void parse(String[] command) {
        int pos = 2;
        String opt = command[pos];

        while (pos < command.length) {
            if (opt.startsWith("-")) {
                options.put(opt, Boolean.TRUE);
                if (opt.equals("-r")) {
                    this.isRecursion = true;
                }
            }
            pos++;
        }
    }

    public Boolean getOpt(String opt) {
        return options.containsKey(opt) ? (Boolean) this.options.get(opt) : false;
    }

    public void checkIllegalArguments(int minPar, int maxPar, String[] argv) {
        if (argv.length < minPar) {
            throw new CommandFormat.NotEnoughArgumentsException(minPar, 0);
        }
        if (argv.length > maxPar) {
            throw new CommandFormat.TooManyArgumentsException(minPar, isRecursion ? argv.length - 3 : argv.length - 2);
        }
    }
}
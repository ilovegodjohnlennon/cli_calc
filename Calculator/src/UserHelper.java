// class to manage the "help" command.

import java.util.HashMap;
import java.util.Map;

public class UserHelper {
    private Map<String, HelpEntry> entries;

    public UserHelper(){
        entries = new HashMap<>();

        addEntry(
                "tree",
                "show the tree of an expression",
                "usage: tree <expression>\n\nfor example: tree sin PI * cos 1 - 57"
        );
        addEntry(
                "set",
                "set a variable",
                "usage: set <name> = <expression>\n\nfor example: set x = PI / 2"
        );
        addEntry(
                "help",
                "display this help",
                ""
        );
    }

    public void addEntry(String key, String summary, String description){
        HelpEntry newEntry = new HelpEntry(key, summary, description);

        entries.put(key, newEntry);
    }

    public void greet(){
        System.out.println("\n\n    Welcome to the calculator!!");
        System.out.println("\nInput an expression to evaluate!");
        System.out.println("type 'help' to learn some cool commands and features!\n\n");
    }

    public void genericHelp(){
        System.out.println("\n\n    Here is a summary of all features!");
        System.out.println("    Type 'help <name>' to learn more about a specific feature!\n");

        for(HelpEntry e: entries.values()){
            e.displayBrief();
        }

        System.out.println("\n\n");
    }

    public void showEntry(String key){
        if(!entries.containsKey(key)){
            System.out.println("No such entry!");
            return;
        }

        System.out.println("\n");
        entries.get(key).displayVerbose();

    }
}

public class HelpEntry {
    String key;
    String summary;
    String description;

    public HelpEntry(String key, String summary, String description){
        this.key = key;
        this.summary = summary;
        this.description = description;
    }

    public void displayBrief(){
        System.out.println("    " + key + " - " + summary);
    }

    public void displayVerbose(){
        displayBrief();

        System.out.println("\n" + description + "\n");
    }
}

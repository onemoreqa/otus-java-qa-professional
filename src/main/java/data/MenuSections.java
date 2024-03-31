package data;

public enum MenuSections {
    CHAT("Chat"),
    EXERCISE("Exercise"),
    GRAMMAR("Grammar"),
    STATS("Stats");

    private String name;

    MenuSections(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package data;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static String getArray() {
        System.out.println(MenuSections.values());
        return "";
        //return Stream.of(Arrays.stream(MenuSections.values()).map(el -> el::getName).collect(Collectors.toList()));
    }
}

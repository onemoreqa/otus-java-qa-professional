package data;

public enum WelcomePageItems {
    first("Next", "Chat to improve your English"),
    second("Next", "Learn new words and grammar"),
    third("Skip >", "7 days FREE"),
    fourth("OK", "Alert");

    private String buttonName;
    private String title;

    WelcomePageItems(String someButtonName, String somePageTitle) {
        this.buttonName = someButtonName;
        this.title = somePageTitle;
    }

    public String getButtonName() {
        return buttonName;
    }

    public String getPageTitle() {
        return title;
    }

}

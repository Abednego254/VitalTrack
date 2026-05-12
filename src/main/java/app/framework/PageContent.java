package app.framework;

public enum PageContent {

    CONTENT("");

    public String defaultContent;

    PageContent(String defaultContent){
        this.defaultContent = defaultContent;
    }

    public String getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }
}

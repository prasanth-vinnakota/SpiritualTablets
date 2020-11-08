package developer.prasanth.spiritualtablets.models;

public class DataItem {

    private String title;
    private String category;
    private int thumbnail;


    public DataItem(String title, String category, int thumbnail) {
        this.title = title;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getThumbnail() {
        return thumbnail;
    }
}

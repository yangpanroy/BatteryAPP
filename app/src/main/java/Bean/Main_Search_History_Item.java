package Bean;

public class Main_Search_History_Item {
    public String title;
    public String module_num;
    public String produce_date;
    public String producer;
    public String latest_logistics_date;
    public String latest_logistics_place;
    public String module_image;

    public Main_Search_History_Item() {
    }

    public Main_Search_History_Item(String title, String module_num, String produce_date, String producer, String latest_logistics_date, String latest_logistics_place, String module_image) {
        this.title = title;
        this.module_num = module_num;
        this.produce_date = produce_date;
        this.producer = producer;
        this.latest_logistics_date = latest_logistics_date;
        this.latest_logistics_place = latest_logistics_place;
        this.module_image = module_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModule_num() {
        return module_num;
    }

    public void setModule_num(String module_num) {
        this.module_num = module_num;
    }

    public String getProduce_date() {
        return produce_date;
    }

    public void setProduce_date(String produce_date) {
        this.produce_date = produce_date;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getLatest_logistics_date() {
        return latest_logistics_date;
    }

    public void setLatest_logistics_date(String latest_logistics_date) {
        this.latest_logistics_date = latest_logistics_date;
    }

    public String getLatest_logistics_place() {
        return latest_logistics_place;
    }

    public void setLatest_logistics_place(String latest_logistics_place) {
        this.latest_logistics_place = latest_logistics_place;
    }

    public String getModule_image() {
        return module_image;
    }

    public void setModule_image(String module_image) {
        this.module_image = module_image;
    }

    @Override
    public String toString() {
        return "Main_Search_History_Item{" +
                "title='" + title + '\'' +
                ", module_num='" + module_num + '\'' +
                ", produce_date='" + produce_date + '\'' +
                ", producer='" + producer + '\'' +
                ", latest_logistics_date='" + latest_logistics_date + '\'' +
                ", latest_logistics_place='" + latest_logistics_place + '\'' +
                ", module_image='" + module_image + '\'' +
                '}';
    }
}

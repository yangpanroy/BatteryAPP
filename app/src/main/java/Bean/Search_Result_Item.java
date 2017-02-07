package Bean;

public class Search_Result_Item {
    String title;
    String date;
    String text1;
    String text2;
    String text3;
    String service_phone;

    public Search_Result_Item() {
    }

    public Search_Result_Item(String title, String date, String text1, String text2, String text3, String service_phone) {
        this.title = title;
        this.date = date;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.service_phone = service_phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    @Override
    public String toString() {
        return "Search_Result_Item{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", text1='" + text1 + '\'' +
                ", text2='" + text2 + '\'' +
                ", text3='" + text3 + '\'' +
                ", service_phone='" + service_phone + '\'' +
                '}';
    }
}

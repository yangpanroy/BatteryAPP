package Bean;

/**
 * Created by Administrator on 2017/3/31.
 */
public class Branch {
    private String name;
    private String province;
    private String city;
    private String dom;
    private double longitude;
    private double latitude;
    private String contact;
    private String phone;
    private int status=1;

    public Branch() {
    }

    public Branch(String name, String province, String city, String dom, double longitude, double latitude, String contact, String phone, int status) {
        this.name = name;
        this.province = province;
        this.city = city;
        this.dom = dom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.contact = contact;
        this.phone = phone;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", dom='" + dom + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                '}';
    }
}

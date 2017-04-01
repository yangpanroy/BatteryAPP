package Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */
public class Company {
    String id;
    private int companyType;
    private String companyName;
    private String province;
    private String city;
    private String district;
    private String dom;
    private double longitude;
    private double latitude;
    private String creditCode;
    private String vendorCode;
    private String contact;
    private String phone;
    private String email;
    private String licensePicture;
    List<Branch> branches = new ArrayList<>();
    private int status = 0;
    private String tradeSK;
    private String productSK;
    private String createTime;
    private String formatTime;// 将申请时间转化为yyyy-MM-ss的形式

    public Company() {
    }

    public Company(String id, int companyType, String companyName, String province, String city, String district, String dom, double longitude, double latitude, String creditCode, String vendorCode, String contact, String phone, String email, String licensePicture, List<Branch> branches, int status, String tradeSK, String productSK, String createTime, String formatTime) {
        this.id = id;
        this.companyType = companyType;
        this.companyName = companyName;
        this.province = province;
        this.city = city;
        this.district = district;
        this.dom = dom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.creditCode = creditCode;
        this.vendorCode = vendorCode;
        this.contact = contact;
        this.phone = phone;
        this.email = email;
        this.licensePicture = licensePicture;
        this.branches = branches;
        this.status = status;
        this.tradeSK = tradeSK;
        this.productSK = productSK;
        this.createTime = createTime;
        this.formatTime = formatTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicensePicture() {
        return licensePicture;
    }

    public void setLicensePicture(String licensePicture) {
        this.licensePicture = licensePicture;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTradeSK() {
        return tradeSK;
    }

    public void setTradeSK(String tradeSK) {
        this.tradeSK = tradeSK;
    }

    public String getProductSK() {
        return productSK;
    }

    public void setProductSK(String productSK) {
        this.productSK = productSK;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    @Override
    public String toString() {
        return "Company{" +
//                "id='" + id + '\'' +
                ", companyType=" + companyType +
                ", companyName='" + companyName + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", dom='" + dom + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", creditCode='" + creditCode + '\'' +
                ", vendorCode='" + vendorCode + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", licensePicture='" + licensePicture + '\'' +
                ", branches=" + branches +
                ", status=" + status +
                ", tradeSK='" + tradeSK + '\'' +
                ", productSK='" + productSK + '\'' +
                ", createTime=" + createTime +
                ", formatTime='" + formatTime + '\'' +
                '}';
    }
}

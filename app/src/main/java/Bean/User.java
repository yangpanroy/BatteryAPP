package Bean;

/**
 * Created by Administrator on 2017/3/23.
 */
public class User {

    private String userName, password;
    private int companyType;
    String companyName, companyId, token;
    int state;
    String createDate;

    public User() {
    }

    public User(String userName, String password, int companyType, String companyName, String companyId, String token, int state, String createDate) {
        this.userName = userName;
        this.password = password;
        this.companyType = companyType;
        this.companyName = companyName;
        this.companyId = companyId;
        this.token = token;
        this.state = state;
        this.createDate = createDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", companyType=" + companyType +
                ", companyName='" + companyName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", token='" + token + '\'' +
                ", state=" + state +
                ", createDate=" + createDate +
                '}';
    }
}

package Bean;

/**
 * Created by Administrator on 2017/3/23.
 */
public class User {

    private String userName, password;
    Company company;
    String token, id;
    int state;
    String createDate;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String id, String userName, String password, Company company, String token, int state, String createDate) {
        this.userName = userName;
        this.password = password;
        this.company = company;
        this.token = token;
        this.id = id;
        this.state = state;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
                ", company=" + company +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                ", state=" + state +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}

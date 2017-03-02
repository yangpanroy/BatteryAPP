package Bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Package {

    String id;
    String _created;
    String _modified;
    String packageSpec;
    String manufacturer;
    String phone;
    String vin;
    ArrayList<Module> modules;
    Timestamp timestamp;

    public Package() {
    }

    public Package(String id, ArrayList<Module> modules) {
        this.id = id;
        this.modules = modules;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String get_created() {
        return _created;
    }

    public void set_created(String _created) {
        this._created = _created;
    }

    public String get_modified() {
        return _modified;
    }

    public void set_modified(String _modified) {
        this._modified = _modified;
    }

    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "Package{" +
                "id='" + id + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", phone='" + phone + '\'' +
                ", vin='" + vin + '\'' +
                ", modules=" + modules +
                ", timestamp=" + timestamp +
                ", _created='" + _created + '\'' +
                ", _modified='" + _modified + '\'' +
                ", packageSpec='" + packageSpec + '\'' +
                '}';
    }
}

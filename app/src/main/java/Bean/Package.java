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
    String carId;
    ArrayList<Module> modules;
    Timestamp timestamp;

    public Package() {
    }

    public Package(String id, String _created, String _modified, String packageSpec, String manufacturer, ArrayList<Module> modules, Timestamp timestamp, String phone, String carId) {
        this.id = id;
        this._created = _created;
        this._modified = _modified;
        this.packageSpec = packageSpec;
        this.manufacturer = manufacturer;
        this.modules = modules;
        this.timestamp = timestamp;
        this.phone = phone;
        this.carId = carId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return "Package{" +
                "id='" + id + '\'' +
                ", _created='" + _created + '\'' +
                ", _modified='" + _modified + '\'' +
                ", packageSpec='" + packageSpec + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", phone='" + phone + '\'' +
                ", carId='" + carId + '\'' +
                ", modules=" + modules +
                ", timestamp=" + timestamp +
                '}';
    }
}

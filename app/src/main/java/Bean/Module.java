package Bean;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Module {

    String id, _created, _modified, packageId, moduleSpec, manufacturer;
    Timestamp timestamp;

    public Module() {
    }

    public Module(String id) {
        this.id = id;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getModuleSpec() {
        return moduleSpec;
    }

    public void setModuleSpec(String moduleSpec) {
        this.moduleSpec = moduleSpec;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id='" + id + '\'' +
                ", _created='" + _created + '\'' +
                ", _modified='" + _modified + '\'' +
                ", packageId='" + packageId + '\'' +
                ", moduleSpec='" + moduleSpec + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

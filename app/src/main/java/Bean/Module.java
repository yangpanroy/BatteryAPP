package Bean;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Module {

    String moduleId, moduleSpec, manufacturer;
    Timestamp timestamp;

    public Module() {
    }

    public Module(String moduleId, String moduleSpec, String manufacturer, Timestamp timestamp) {
        this.moduleId = moduleId;
        this.moduleSpec = moduleSpec;
        this.manufacturer = manufacturer;
        this.timestamp = timestamp;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
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
                "moduleId='" + moduleId + '\'' +
                ", moduleSpec='" + moduleSpec + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

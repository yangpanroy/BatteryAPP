package Bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/1.
 */
public class Car {

    String id, carSpec, manufacturer;
    ArrayList<Package> packages;
    Timestamp timestamp;

    public Car() {
    }

    public Car(ArrayList<Package> packages, String manufacturer) {
        this.packages = packages;
        this.manufacturer = manufacturer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarSpec() {
        return carSpec;
    }

    public void setCarSpec(String carSpec) {
        this.carSpec = carSpec;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ArrayList<Package> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

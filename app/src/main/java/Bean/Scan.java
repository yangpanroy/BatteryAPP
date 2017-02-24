package Bean;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Scan {
    String id;
    String _created;
    String _modified;
    String scannerId;
    String scanner;
    String scanBranch;
    String barcode;
    Timestamp timestamp;
    double longitude, latitude;

    public Scan() {
    }

    public Scan(String id, String _created, String _modified, String scannerId, String scanner, String scanBranch, String barcode, Timestamp timestamp, double longitude, double latitude) {
        this.id = id;
        this._created = _created;
        this._modified = _modified;
        this.scannerId = scannerId;
        this.scanner = scanner;
        this.scanBranch = scanBranch;
        this.barcode = barcode;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getScannerId() {
        return scannerId;
    }

    public void setScannerId(String scannerId) {
        this.scannerId = scannerId;
    }

    public String getScanner() {
        return scanner;
    }

    public void setScanner(String scanner) {
        this.scanner = scanner;
    }

    public String getScanBranch() {
        return scanBranch;
    }

    public void setScanBranch(String scanBranch) {
        this.scanBranch = scanBranch;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "Scan{" +
                "id='" + id + '\'' +
                ", _created='" + _created + '\'' +
                ", _modified='" + _modified + '\'' +
                ", scannerId='" + scannerId + '\'' +
                ", scanner='" + scanner + '\'' +
                ", scanBranch='" + scanBranch + '\'' +
                ", barcode='" + barcode + '\'' +
                ", timestamp=" + timestamp +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}

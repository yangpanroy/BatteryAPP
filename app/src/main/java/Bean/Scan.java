package Bean;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Scan {
    String id;
    String _created;
    String _modified;
    public String createTime;
    String scannerId;
    String scanner;
    String scanBranch;
    String barcode;
    Timestamp timestamp;
    double longitude, latitude;

    public Scan() {
    }

    public Scan(String scannerId, String scanner, String scanBranch, String barcode) {
        this.scannerId = scannerId;
        this.scanner = scanner;
        this.scanBranch = scanBranch;
        this.barcode = barcode;
    }

    public String getId() {
        if (id == null){
            id = "";
        }
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
        if (scannerId == null){
            scannerId = "";
        }
        return scannerId;
    }

    public void setScannerId(String scannerId) {
        this.scannerId = scannerId;
    }

    public String getScanner() {
        if (scanner == null){
            scanner = "";
        }
        return scanner;
    }

    public void setScanner(String scanner) {
        this.scanner = scanner;
    }

    public String getScanBranch() {
        if (scanBranch == null){
            scanBranch = "";
        }
        return scanBranch;
    }

    public void setScanBranch(String scanBranch) {
        this.scanBranch = scanBranch;
    }

    public String getBarcode() {
        if (barcode == null){
            barcode = "";
        }
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
                ", createTime='" + createTime + '\'' +
                ", scannerId='" + scannerId + '\'' +
                ", scanner='" + scanner + '\'' +
                ", scanBranch='" + scanBranch + '\'' +
                ", barcode='" + barcode + '\'' +
                ", timestamp=" + timestamp +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", _created='" + _created + '\'' +
                ", _modified='" + _modified + '\'' +
                '}';
    }
}

package Bean;

/**
 * Created by Administrator on 2017/2/7.
 */
public class Import_Export_Item {

    public String item_module_id, logistics_source, logistics_destination;

    public Import_Export_Item() {
    }

    public Import_Export_Item(String item_module_id, String logistics_source, String logistics_destination) {
        this.item_module_id = item_module_id;
        this.logistics_source = logistics_source;
        this.logistics_destination = logistics_destination;
    }

    public String getItem_module_id() {
        return item_module_id;
    }

    public void setItem_module_id(String item_module_id) {
        this.item_module_id = item_module_id;
    }

    public String getLogistics_source() {
        return logistics_source;
    }

    public void setLogistics_source(String logistics_source) {
        this.logistics_source = logistics_source;
    }

    public String getLogistics_destination() {
        return logistics_destination;
    }

    public void setLogistics_destination(String logistics_destination) {
        this.logistics_destination = logistics_destination;
    }

    @Override
    public String
    toString() {
        return "Import_Export_Item{" +
                "item_module_id='" + item_module_id + '\'' +
                ", logistics_source='" + logistics_source + '\'' +
                ", logistics_destination='" + logistics_destination + '\'' +
                '}';
    }
}

package Bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Trade {

    String id;
    Date _created;
    Date _modified;
    String fromId;
    String from;
    String fromBranch;
    String fromSignature;
    String to;
    String toId;
    String toBranch;
    String toSignature;
    String attachment; //存储Base64图片字符串
    double longitude, latitude;
    ArrayList<Package> packages;
    Timestamp timestamp;
    Block block;
    Transcation transcation;

    public Trade() {
    }

    public Trade(String fromId, String from, String fromBranch, String fromSignature,
                 String toId, String to, String toBranch, String toSignature,
                 String attachment, ArrayList<Package> packages) {
        this.fromId = fromId;
        this.from = from;
        this.fromBranch = fromBranch;
        this.fromSignature = fromSignature;
        this.to = to;
        this.toId = toId;
        this.toBranch = toBranch;
        this.toSignature = toSignature;
        this.attachment = attachment;
        this.packages = packages;
//        this.block = block;
    }

    public String get_created() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = "";
        if (this._created != null){
            date = df.format(this._created);
        }
        return date;
    }

    public void set_created(Date _created) {
        this._created = _created;
    }

    public Date get_modified() {
        return _modified;
    }

    public void set_modified(Date _modified) {
        this._modified = _modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromBranch() {
        return fromBranch;
    }

    public void setFromBranch(String fromBranch) {
        this.fromBranch = fromBranch;
    }

    public String getFromSignature() {
        return fromSignature;
    }

    public void setFromSignature(String fromSignature) {
        this.fromSignature = fromSignature;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToBranch() {
        return toBranch;
    }

    public void setToBranch(String toBranch) {
        this.toBranch = toBranch;
    }

    public String getToSignature() {
        return toSignature;
    }

    public void setToSignature(String toSignature) {
        this.toSignature = toSignature;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public void setAttachment(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.attachment = result;
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

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Transcation getTranscation() {
        return transcation;
    }

    public void setTranscation(Transcation transcation) {
        this.transcation = transcation;
    }

    public Bitmap attachmentToBitmap(){

        byte[] decode = Base64.decode(attachment, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }


    @Override
    public String toString() {
        return "Trade{" +
                "id='" + id + '\'' +
                ", _created=" + _created +
                ", _modified=" + _modified +
                ", fromId='" + fromId + '\'' +
                ", from='" + from + '\'' +
                ", fromBranch='" + fromBranch + '\'' +
                ", fromSignature='" + fromSignature + '\'' +
                ", to='" + to + '\'' +
                ", toId='" + toId + '\'' +
                ", toBranch='" + toBranch + '\'' +
                ", toSignature='" + toSignature + '\'' +
                ", attachment='" + attachment + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", packages=" + packages +
                ", timestamp=" + timestamp +
                ", block=" + block +
                ", transcation=" + transcation +
                '}';
    }
}

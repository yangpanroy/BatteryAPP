package Bean;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Trade {

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
    ArrayList<String> productIds;
    Timestamp timestamp;
    Block block;
    Transcation transcation;

    public Trade() {
    }

    public Trade(String fromId, String from, String fromBranch, String fromSignature,
                 String toId, String to, String toBranch, String toSignature,
                 String attachment, ArrayList<String> productIds) {
        this.fromId = fromId;
        this.from = from;
        this.fromBranch = fromBranch;
        this.fromSignature = fromSignature;
        this.to = to;
        this.toId = toId;
        this.toBranch = toBranch;
        this.toSignature = toSignature;
        this.attachment = attachment;
        this.productIds = productIds;
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

    public ArrayList<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(ArrayList<String> productIds) {
        this.productIds = productIds;
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

    @Override
    public String toString() {
        return "Trade{" +
                "fromId='" + fromId + '\'' +
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
                ", productIds=" + productIds +
                ", timestamp=" + timestamp +
                ", block=" + block +
                ", transcation=" + transcation +
                '}';
    }
}

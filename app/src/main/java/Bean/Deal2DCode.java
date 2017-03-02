package Bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/26.
 */
public class Deal2DCode {

    String to;
    String toId;
    String toBranch;
    String toSignature;
    ArrayList<Package> packages;

    public Deal2DCode() {
    }

    public Deal2DCode(String to, String toId, String toBranch, String toSignature, ArrayList<Package> packages) {
        this.to = to;
        this.toId = toId;
        this.toBranch = toBranch;
        this.toSignature = toSignature;
        this.packages = packages;
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

    public ArrayList<Package> getpackages() {
        return packages;
    }

    public void setpackages(ArrayList<Package> packages) {
        this.packages = packages;
    }

    @Override
    public String toString() {
        return "Deal2DCode{" +
                "to='" + to + '\'' +
                ", toId='" + toId + '\'' +
                ", toBranch='" + toBranch + '\'' +
                ", toSignature='" + toSignature + '\'' +
                ", packages=" + packages +
                '}';
    }
}

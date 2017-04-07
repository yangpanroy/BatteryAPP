package Bean;

/**
 * Created by Administrator on 2017/2/26.
 */
public class Deal2DCode {

    String creditCode;
    String toSignature;
    String packageListMD5Code;

    public Deal2DCode() {
    }

    public Deal2DCode(String creditCode, String toSignature, String packageListMD5Code) {
        this.creditCode = creditCode;
        this.toSignature = toSignature;
        this.packageListMD5Code = packageListMD5Code;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getToSignature() {
        return toSignature;
    }

    public void setToSignature(String toSignature) {
        this.toSignature = toSignature;
    }

    public String getPackageListMD5Code() {
        return packageListMD5Code;
    }

    public void setPackageListMD5Code(String packageListMD5Code) {
        this.packageListMD5Code = packageListMD5Code;
    }

    @Override
    public String toString() {
        return "Deal2DCode{" +
                "creditCode='" + creditCode + '\'' +
                ", toSignature='" + toSignature + '\'' +
                ", packageListMD5Code='" + packageListMD5Code + '\'' +
                '}';
    }
}

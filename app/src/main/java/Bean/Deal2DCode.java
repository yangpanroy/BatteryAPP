package Bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/26.
 */
public class Deal2DCode {

    String creditCode;
    String toSignature;
    Boolean isCommon;

    public Deal2DCode() {
    }

    public Deal2DCode(String creditCode, String toSignature, Boolean isCommon) {
        this.creditCode = creditCode;
        this.toSignature = toSignature;
        this.isCommon = isCommon;
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

    public Boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }

    @Override
    public String toString() {
        return "Deal2DCode{" +
                "creditCode='" + creditCode + '\'' +
                ", toSignature='" + toSignature + '\'' +
                ", isCommon=" + isCommon +
                '}';
    }
}

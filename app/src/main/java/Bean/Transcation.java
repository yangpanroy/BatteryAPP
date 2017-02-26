package Bean;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Transcation {

    String chaincodeId, chaincodeType, uuid, cert, payload, signature;

    public Transcation() {
    }

    public Transcation(String chaincodeId, String chaincodeType, String uuid, String cert, String payload, String signature) {
        this.chaincodeId = chaincodeId;
        this.chaincodeType = chaincodeType;
        this.uuid = uuid;
        this.cert = cert;
        this.payload = payload;
        this.signature = signature;
    }

    public String getChaincodeId() {
        return chaincodeId;
    }

    public void setChaincodeId(String chaincodeId) {
        this.chaincodeId = chaincodeId;
    }

    public String getChaincodeType() {
        return chaincodeType;
    }

    public void setChaincodeType(String chaincodeType) {
        this.chaincodeType = chaincodeType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Transcation{" +
                "chaincodeId='" + chaincodeId + '\'' +
                ", chaincodeType='" + chaincodeType + '\'' +
                ", uuid='" + uuid + '\'' +
                ", cert='" + cert + '\'' +
                ", payload='" + payload + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}

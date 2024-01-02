package org.rmj.g3appdriver.lib.Account.pojo;

public class ChangeUserAddress {
    private String cPrimary;
    private String cAddrssTp;
    private String sHouseNox;
    private String sAddressx;
    private String sTownIDx;
    private String sBrgyIdx;
    private String nLatitude;
    private String nLongitude;
    private String sRemarks;
    private String sRqstCd;
    private String sSourceCd;
    private String sSourceNo;
    private String message;

    public String getcPrimary() {
        return cPrimary;
    }
    public void setcPrimary(String cPrimary) {
        this.cPrimary = cPrimary;
    }
    public String getsHouseNox() {
        return sHouseNox;
    }
    public void setsHouseNox(String sHouseNox) {
        this.sHouseNox = sHouseNox;
    }
    public String getsAddressx() {
        return sAddressx;
    }
    public void setsAddressx(String sAddressx) {
        this.sAddressx = sAddressx;
    }
    public String getsTownIDx() {
        return sTownIDx;
    }
    public String getcAddrssTp() {
        return cAddrssTp;
    }
    public void setcAddrssTp(String cAddrssTp) {
        this.cAddrssTp = cAddrssTp;
    }
    public void setsTownIDx(String sTownIDx) {
        this.sTownIDx = sTownIDx;
    }
    public String getsBrgyIdx() {
        return sBrgyIdx;
    }
    public void setsBrgyIdx(String sBrgyIdx) {
        this.sBrgyIdx = sBrgyIdx;
    }
    public String getnLatitude() {
        return nLatitude;
    }
    public void setnLatitude(String nLatitude) {
        this.nLatitude = nLatitude;
    }
    public String getnLongitude() {
        return nLongitude;
    }
    public void setnLongitude(String nLongitude) {
        this.nLongitude = nLongitude;
    }
    public String getsRemarks() {
        return sRemarks;
    }
    public void setsRemarks(String sRemarks) {
        this.sRemarks = sRemarks;
    }
    public String getsRqstCd() {
        return sRqstCd;
    }
    public void setsRqstCd(String sRqstCd) {
        this.sRqstCd = sRqstCd;
    }
    public String getsSourceCd() {
        return sSourceCd;
    }
    public void setsSourceCd(String sSourceCd) {
        this.sSourceCd = sSourceCd;
    }
    public String getsSourceNo() {
        return sSourceNo;
    }
    public void setsSourceNo(String sSourceNo) {
        this.sSourceNo = sSourceNo;
    }

    private String getMessage(){
        return message;
    }

    public Boolean isDataValid(){
        if (sHouseNox.isEmpty()){
            message = "House No is required";
            return false;
        }
        if (sAddressx.isEmpty()){
            message = "Street Address is required";
            return false;
        }
        if (sBrgyIdx.isEmpty()){
            message = "Baranggay Id is required";
            return false;
        }
        if (sTownIDx.isEmpty()){
            message = "Town Id is required";
            return false;
        }
        if (cAddrssTp.isEmpty()){
            message = "Province is required";
            return false;
        }
        return true;
    }
}

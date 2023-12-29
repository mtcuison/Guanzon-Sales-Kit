package org.rmj.g3appdriver.lib.Account.pojo;

public class ChangeUserAddress {
    private String cAddrssTp;
    private String sHouseNox;
    private String sAddressx;
    private String sTownIDx;
    private String sBrgyIdx;
    private String message;

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
    private String getMessage(){
        return message;
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

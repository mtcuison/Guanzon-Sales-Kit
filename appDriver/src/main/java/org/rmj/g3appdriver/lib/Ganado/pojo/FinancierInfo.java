package org.rmj.g3appdriver.lib.Ganado.pojo;

public class FinancierInfo {

    private String sTransNox = "";
    private String sLastName = "";
    private String sFrstName = "";
    private String sMiddName = "";
    private String sSuffixNm = "";
    private String sAddressx = "";
    private String sCntryCde = "";
    private String sCntryNme = "";

    private String sMobileNo = "";
    private String sEmailAdd = "";
    private String sWhatsApp = "";
    private String sWeChatApp = "";
    private String sFbAccntx = "";
    private long sFIncomex = 0;
    private String sReltionx = "";

    private String message;

    public FinancierInfo() {
    }

    public String getMessage() {
        return message;
    }

    public String getsTransNox() {
        return sTransNox;
    }

    public void setsTransNox(String sTransNox) {
        this.sTransNox = sTransNox;
    }

    public String getLastName() {
        return sLastName;
    }

    public void setLastName(String sLastName) {
        this.sLastName = sLastName;
    }

    public String getFrstName() {
        return sFrstName;
    }

    public void setFrstName(String sFrstName) {
        this.sFrstName = sFrstName;
    }

    public String getMiddName() {
        return sMiddName;
    }

    public void setMiddName(String sMiddName) {
        this.sMiddName = sMiddName;
    }

    public String getSuffixNm() {
        return sSuffixNm;
    }

    public void setSuffixNm(String sSuffixNm) {
        this.sSuffixNm = sSuffixNm;
    }

    public String getAddressx() {
        return sAddressx;
    }

    public void setAddressx(String sAddressx) {
        this.sAddressx = sAddressx;
    }

    public String getCountryCode() {
        return sCntryCde;
    }

    public void setCountryCode(String sCntryCde) {
        this.sCntryCde = sCntryCde;
    }

    public String getCountryName() {
        return sCntryNme;
    }

    public void setCountryName(String sCntryNme) {
        this.sCntryNme = sCntryNme;
    }

    public String getMobileNo() {
        return sMobileNo;
    }

    public void setMobileNo(String sMobileNo) {
        this.sMobileNo = sMobileNo;
    }

    public String getEmailAdd() {
        return sEmailAdd;
    }

    public void setEmailAdd(String sEmailAdd) {
        this.sEmailAdd = sEmailAdd;
    }


    public String getsReltionx() {
        return sReltionx;
    }

    public void setsReltionx(String sReltionx) {
        this.sReltionx = sReltionx;
    }


    public String getsWhatsApp() {
        return sWhatsApp;
    }

    public void setsWhatsApp(String sWhatsApp) {
        this.sWhatsApp = sWhatsApp;
    }

    public String getsWeChatApp() {
        return sWeChatApp;
    }

    public void setsWeChatApp(String sWeChatApp) {
        this.sWeChatApp = sWeChatApp;
    }

    public String getsFbAccntx() {
        return sFbAccntx;
    }

    public void setsFbAccntx(String sFbAccntx) {
        this.sFbAccntx = sFbAccntx;
    }



    public long getRangeOfIncome() {
        if(sFIncomex == 0){
            return 0;
        }
        return sFIncomex;
    }
    public void setRangeOfIncome(long sFIncomex) {
        this.sFIncomex = sFIncomex;
    }

    public boolean isDataValid(){
        if(sTransNox.isEmpty()){
            message = "Transaction No. is empty.";
            return false;
        }

        if(sLastName.isEmpty()){
            message = "Please enter last name.";
            return false;
        }

        if(sFrstName.isEmpty()){
            message = "Please enter first name.";
            return false;
        }

        if(sWhatsApp.isEmpty() && sWeChatApp.isEmpty()){
            message = "Please enter atleast one account from WhatsApp or Wechat.";
            return false;
        }

        /*if(sAddressx.isEmpty()){
            message = "Please enter address.";
            return false;
        }

        if(sCntryCde.isEmpty()){
            message = "Please select country.";
            return false;
        }

        if(sMobileNo.isEmpty()){
            message = "Please enter mobile number.";
            return false;
        }

        if(sReltionx.isEmpty()){
            message = "Please select relationship.";
            return false;
        }*/

        return true;
    }
}

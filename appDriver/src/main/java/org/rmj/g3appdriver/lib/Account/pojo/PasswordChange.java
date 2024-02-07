package org.rmj.g3appdriver.lib.Account.pojo;

import java.util.regex.Pattern;

public class PasswordChange {
    private String sNewPswd;
    private String sConfPswd;
    private String message;

    public String getsNewPswd() {
        return sNewPswd;
    }
    public String getsConfPswd() {
        return sConfPswd;
    }

    public void setsNewPswd(String sNewPswd) {
        this.sNewPswd = sNewPswd;
    }

    public void setsConfPswd(String sConfPswd) {
        this.sConfPswd = sConfPswd;
    }

    public String getMessage() {
        return message;
    }
    public boolean isDataValid(){
        if(sNewPswd.isEmpty()){
            message = "Please enter new password";
            return false;
        }
        if(sConfPswd.isEmpty()){
            message = "Please confirm password";
            return false;
        }
        if(!sNewPswd.equalsIgnoreCase(sConfPswd)){
            message = "New password does not match";
            return false;
        }
        if(sNewPswd.length() < 8){
            message = "Please enter atleast 8 characters password";
            return false;
        }
        if(sNewPswd.length() > 16){
            message = "Please enter below 16 characters password";
            return false;
        }
        if(Pattern.matches("[a-z]+", sNewPswd)){
            message = "Password must have atleast 1 uppercase character";
            return false;
        }
        if(Pattern.matches("[A-Z]+", sNewPswd)){
            message = "Password must have atleast 1 uppercase character";
            return false;
        }
        if(Pattern.matches("[0-9]+", sNewPswd)){
            message = "Password must have atleast 1 numeric character";
            return false;
        }
        if(Pattern.matches("[a-zA-Z0-9]+", sNewPswd)){
            message = "Invalid password format. Please follow requirements above";
            return false;
        }
        return true;
    }
}

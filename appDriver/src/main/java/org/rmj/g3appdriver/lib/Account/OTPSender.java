package org.rmj.g3appdriver.lib.Account;

import android.app.Application;

import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.etc.AppConfigPreference;

public class OTPSender {
    private Application instance;
    private AppConfigPreference poConfig;
    private GCircleApi loApi;
    private HttpHeaders poHeaders;
    private String sMobile;
    private String OTPRequest;
    private String message;

    public OTPSender(Application instance){
        this.instance = instance;

        this.poConfig = AppConfigPreference.getInstance(instance);
        this.sMobile = poConfig.getMobileNo(); //based on user logged account
        this.loApi = new GCircleApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
    }
    public String getMessage(){
        return message;
    }
    public String GetOTP(){
        return OTPRequest;
    }
}

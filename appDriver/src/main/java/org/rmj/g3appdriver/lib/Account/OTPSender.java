package org.rmj.g3appdriver.lib.Account;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;

import android.app.Application;
import android.content.Context;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
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
    public Boolean SendOTP(){
        try {
            JSONObject params = new JSONObject();
            params.put("mobile_number", sMobile);

            String sResponse = WebClient.sendRequest(loApi.getUrlSendOtpMobile(), params.toString(), poHeaders.getHeaders());
            if(sResponse == null){
                message = SERVER_NO_RESPONSE;
                return false;
            }
            //CONVERT WEB SERVER RESPONSE TO JSON
            JSONObject loResponse = new JSONObject(sResponse);

            //GET ERROR RETURNED FROM SERVER
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            OTPRequest = loResponse.get("otp").toString();
            return true;
        }catch (Exception e){
            message = e.getMessage();
            return false;
        }
    }
    public Boolean VerifyOTP(String OTP){
        if (OTP.isEmpty()){
            message = "No submitted OTP";
            return false;
        }

        String OTPGenerated = OTPRequest;
        if (!OTPGenerated.equals(OTP)){
            message = "OTP not verified. Please check your One-Time-Password sent on your device";
            return false;
        }

        return true;
    }
}

package org.rmj.g3appdriver.lib.Account.gConnect.obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.g3appdriver.GConnect.Api.GConnectApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;
import org.rmj.g3appdriver.lib.Account.pojo.AccountInfo;

public class SignUp implements iAuth {
    private static final String TAG = SignUp.class.getSimpleName();

    private final Application instance;
    private final GConnectApi poApi;
    private final HttpHeaders poHeaders;

    private String message;

    public SignUp(Application instance) {
        this.instance = instance;
        this.poApi = new GConnectApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
    }

    @Override
    public int DoAction(Object args) {
        try{
            AccountInfo loInfo = (AccountInfo) args;
            if(!loInfo.isAccountInfoValid()){
                message = loInfo.getMessage();
                return 0;
            }

            JSONObject params = new JSONObject();
            params.put("name", loInfo.getFullName());
            params.put("mail", loInfo.getEmail());
            params.put("pswd", loInfo.getPassword());
            params.put("mobile", loInfo.getMobileNo());
//            params.put("cAgreeTnC", loInfo.getcAgreeTnC());

            String lsResponse = WebClient.sendRequest(
                    poApi.getRegisterAcountAPI(),
                    params.toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return 0;
            }
            Log.e(TAG, lsResponse);
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(!lsResult.equalsIgnoreCase("success")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return 0;
            }

            message = "An email has been sent to your inbox for account verification.";
            return 1;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return 0;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}

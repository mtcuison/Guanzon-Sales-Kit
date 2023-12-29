package org.rmj.g3appdriver.lib.Account.gCircle.obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Account.EmployeeSession;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;

public class ChangeMobile implements iAuth {
    private static final String TAG = ChangePassword.class.getSimpleName();
    private final Application instance;
    private final GCircleApi poApi;
    private final HttpHeaders poHeaders;
    private EmployeeSession poSession;
    private String message;
    public ChangeMobile(Application instance){
        this.instance = instance;
        this.poApi = new GCircleApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poSession = EmployeeSession.getInstance(instance);
    }

    @Override
    public int DoAction(Object params) {
        try{
            JSONObject loParams = new JSONObject();
            loParams.put("sClientID", poSession.getClientId());
            loParams.put("cReqstCDe", "");
            loParams.put("cReqstCDe", "");
            loParams.put("sMobileNo", params.toString());
            loParams.put("cPrimaryx", "");
            loParams.put("sRemarksx", "");
            loParams.put("sSourceCD", "");
            loParams.put("sSourceNo", "");

            String sResponse = WebClient.sendRequest(poApi.getUrlNewChangeMobile(), loParams.toString(), poHeaders.getHeaders());
            if(sResponse == null){
                message = SERVER_NO_RESPONSE;
                return 0;
            }

            JSONObject loResponse = new JSONObject(sResponse);
            String lsResult = loResponse.getString("result");
            if (lsResult.equalsIgnoreCase("error")) {
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                Log.e(TAG, message);
                return 0;
            }

            message = "Mobile updated successfully.";
            return 1;
        }catch (Exception e){
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

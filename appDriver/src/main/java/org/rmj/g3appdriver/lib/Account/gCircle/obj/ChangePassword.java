package org.rmj.g3appdriver.lib.Account.gCircle.obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.GConnect.Api.GConnectApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;
import org.rmj.g3appdriver.lib.Account.pojo.PasswordChange;

public class ChangePassword implements iAuth {
    private static final String TAG = ChangePassword.class.getSimpleName();
    private final Application instance;
    private final GConnectApi poApi;
    private final HttpHeaders poHeaders;
    private String message;
    private DClientInfo poUser;

    public ChangePassword(Application instance) {
        this.instance = instance;
        this.poApi = new GConnectApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poUser = GGC_GCircleDB.getInstance(instance).ClientDao();
    }

    @Override
    public int DoAction(Object args) {
        try{
            PasswordChange loInfo = (PasswordChange) args;
            if(!loInfo.isDataValid()){
                message = loInfo.getMessage();
                return 0;
            }

            JSONObject params = new JSONObject();
            params.put("newpswd", loInfo.getsNewPswd());
            params.put("mail", poUser.GetClientInfo().getEmailAdd());

            String lsResponse = WebClient.sendRequest(
                    poApi.getUrlNewChangePassword(),
                    params.toString(),
                    poHeaders.getHeaders());
            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return 0;
            }

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if (lsResult.equalsIgnoreCase("error")) {
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                Log.e(TAG, message);
                return 0;
            }

            message = "Password updated successfully.";
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

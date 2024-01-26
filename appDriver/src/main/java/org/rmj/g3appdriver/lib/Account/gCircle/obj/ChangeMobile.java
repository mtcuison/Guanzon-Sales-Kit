package org.rmj.g3appdriver.lib.Account.gCircle.obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DMobileRequest;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.GConnect.Api.GConnectApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChangeMobile implements iAuth {
    private static final String TAG = ChangePassword.class.getSimpleName();
    private final Application instance;
    private final GConnectApi poApi;
    private final HttpHeaders poHeaders;
    private ClientSession poSession;
    private DMobileRequest poDao;
    private DClientInfo poUser;
    private String message;
    public ChangeMobile(Application instance){
        this.instance = instance;
        this.poApi = new GConnectApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poSession = ClientSession.getInstance(instance);
        this.poDao = GGC_GCircleDB.getInstance(instance).MobileRequestDao();
        this.poUser = GGC_GCircleDB.getInstance(instance).ClientDao();
    }

    @Override
    public int DoAction(Object params) {
        try{
            JSONObject loParams = new JSONObject();
            loParams.put("sTransNox", CreateUniqueID());
            loParams.put("sClientID", poSession.getClientID());
            loParams.put("cReqstCDe", "");
            loParams.put("sMobileNo", params.toString());
            loParams.put("cPrimaryx", "");
            loParams.put("sRemarksx", "");
            loParams.put("sSourceCD", "");
            loParams.put("sSourceNo", "");
            loParams.put("mail", poUser.GetClientInfo().getEmailAdd());

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
    private String CreateUniqueID() {
        String lsUniqIDx = "";
        try {
            String lsBranchCd = "MX01";
            String lsCrrYear = new SimpleDateFormat("yy", Locale.getDefault()).format(new Date());
            StringBuilder loBuilder = new StringBuilder(lsBranchCd);
            loBuilder.append(lsCrrYear);

            int lnLocalID = poDao.GetRowsCountForID() + 1;
            String lsPadNumx = String.format("%05d", lnLocalID);
            loBuilder.append(lsPadNumx);
            lsUniqIDx = loBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, lsUniqIDx);
        return lsUniqIDx;
    }
    @Override
    public String getMessage() {
        return message;
    }
}

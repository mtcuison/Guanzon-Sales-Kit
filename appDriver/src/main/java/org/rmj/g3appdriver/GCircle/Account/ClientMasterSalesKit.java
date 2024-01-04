package org.rmj.g3appdriver.GCircle.Account;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;
import android.app.Application;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfoSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;

public class ClientMasterSalesKit {
    private static final String TAG = org.rmj.g3appdriver.GConnect.Account.ClientMaster.class.getSimpleName();
    private final DClientInfoSalesKit poDao;
    private final HttpHeaders poHeaders;
    private final GCircleApi poApi;
    private String message;

    public ClientMasterSalesKit(Application instance) {
        this.poDao = GGC_GCircleDB.getInstance(instance).ciSKDAO();
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poApi = new GCircleApi(instance);
    }
    public String getMessage() {
        return message;
    }
    public LiveData<EClientInfoSalesKit> GetClientDetail(){
        return poDao.getClientInfo();
    }

    public boolean CompleteClientInfo(EClientInfoSalesKit foClient){
        try {
            JSONObject param = new JSONObject();
            param.put("sUserIDxx", foClient.getUserIDxx());
            param.put("dTransact", new AppConstants().DATE_MODIFIED);
            param.put("sLastName", foClient.getLastName());
            param.put("sFrstName", foClient.getFrstName());
            param.put("sMiddName", foClient.getMiddName());
            param.put("sMaidenNm", foClient.getMaidenNm());
            param.put("sSuffixNm", foClient.getSuffixNm());
            param.put("cGenderCd", foClient.getGenderCd());
            param.put("cCvilStat", foClient.getCvilStat());
            param.put("sCitizenx", foClient.getCitizenx());
            param.put("dBirthDte", foClient.getBirthDte());
            param.put("sBirthPlc", foClient.getBirthPlc());
            param.put("sHouseNo1", foClient.getHouseNo1());
            param.put("sAddress1", foClient.getAddress1());
            param.put("sBrgyIDx1", foClient.getBrgyIDx1());
            param.put("sTownIDx1", foClient.getTownIDx1());
            param.put("sHouseNo2", foClient.getHouseNo2());
            param.put("sAddress2", foClient.getAddress2());
            param.put("sBrgyIDx2", foClient.getBrgyIDx2());
            param.put("sTownIDx2", foClient.getTownIDx2());

            //SAVE TO LOCAL DEVICE
            poDao.insert(foClient);

            //SEND TO SERVER
            String lsResponse = WebClient.sendRequest(
                    poApi.getUrlCompleteAccount(),
                    param.toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(!lsResult.equalsIgnoreCase("success")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }
}

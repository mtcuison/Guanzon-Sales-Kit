package org.rmj.g3appdriver.lib.Account.gCircle.obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Account.EmployeeSession;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DAddressRequest;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DAddressUpdate;
import org.rmj.g3appdriver.GCircle.room.Entities.EAddressUpdate;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.lib.Account.Model.iAuth;
import org.rmj.g3appdriver.lib.Account.pojo.ChangeUserAddress;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChangeAddress implements iAuth {
    private static final String TAG = ChangePassword.class.getSimpleName();
    private final Application instance;
    private final GCircleApi poApi;
    private final HttpHeaders poHeaders;
    private EmployeeSession poSession;
    private DAddressRequest poDao;
    private String message;
    public ChangeAddress(Application instance){
        this.instance = instance;
        this.poApi = new GCircleApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poSession = EmployeeSession.getInstance(instance);
        this.poDao = GGC_GCircleDB.getInstance(instance).AddressRequestDao();
    }
    @Override
    public int DoAction(Object params) {
        try {
            ChangeUserAddress poUserAddress = (ChangeUserAddress) params;

            String sTransNox = CreateUniqueID();
            if (sTransNox.isEmpty()){
                message = "No transaction no generated";
                return 0;
            }

            /*INSERT TO LOCAL*/
            EAddressUpdate addressUpdate = new EAddressUpdate();
            addressUpdate.setTransNox(sTransNox);
            addressUpdate.setClientID(poSession.getClientId());
            addressUpdate.setReqstCDe(poUserAddress.getsRqstCd());
            addressUpdate.setAddrssTp(poUserAddress.getcAddrssTp());
            addressUpdate.setHouseNox(poUserAddress.getsHouseNox());
            addressUpdate.setAddressx(poUserAddress.getsAddressx());
            addressUpdate.setTownIDxx(poUserAddress.getsTownIDx());
            addressUpdate.setBrgyIDxx(poUserAddress.getsBrgyIdx());
            addressUpdate.setPrimaryx(poUserAddress.getcPrimary());
            addressUpdate.setLatitude(poUserAddress.getnLatitude());
            addressUpdate.setLongitud(poUserAddress.getnLongitude());
            addressUpdate.setRemarksx(poUserAddress.getsRemarks());
            addressUpdate.setSendStat("0");
            addressUpdate.setSendDate(AppConstants.CURRENT_DATE());
            addressUpdate.setTranStat("0");
            addressUpdate.setModified(AppConstants.CURRENT_DATE());
            addressUpdate.setTimeStmp(AppConstants.CURRENT_TIME);

            poDao.insert(addressUpdate);

            /*UPLOAD TO SERVER*/
            JSONObject loParams = new JSONObject();
            loParams.put("sTransNox", sTransNox);
            loParams.put("sClientID", poSession.getClientId());
            loParams.put("cReqstCDe", poUserAddress.getsRqstCd());
            loParams.put("cAddrssTp", poUserAddress.getcAddrssTp());
            loParams.put("sHouseNox", poUserAddress.getsHouseNox());
            loParams.put("sAddressx", poUserAddress.getsAddressx());
            loParams.put("sTownIDxx", poUserAddress.getsTownIDx());
            loParams.put("sBrgyIDxx", poUserAddress.getsBrgyIdx());
            loParams.put("cPrimaryx", poUserAddress.getcPrimary());
            loParams.put("nLatitude", poUserAddress.getnLatitude());
            loParams.put("nLongitud", poUserAddress.getnLongitude());
            loParams.put("sRemarksx", poUserAddress.getsRemarks());
            loParams.put("sSourceCD", poUserAddress.getsSourceCd());
            loParams.put("sSourceNo", poUserAddress.getsSourceNo());

            String sResponse = WebClient.sendRequest(poApi.getUrlNewChangeAddress(), loParams.toString(), poHeaders.getHeaders());
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

            message = "Address updated successfully.";
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

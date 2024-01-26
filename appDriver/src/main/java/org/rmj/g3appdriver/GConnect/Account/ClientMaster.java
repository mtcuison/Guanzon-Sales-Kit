package org.rmj.g3appdriver.GConnect.Account;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DClientInfoSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.GConnect.Api.GConnectApi;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;

public class ClientMaster {
    private static final String TAG = ClientMaster.class.getSimpleName();

    private final DClientInfoSalesKit poDao;
    private final DClientInfo clientDao;
    private final HttpHeaders poHeaders;
    private final GConnectApi poApi;
    private final ClientSession poSession;
    private final ClientMasterSalesKit poSalesKit;


    private String message;

    public ClientMaster(Application instance) {
        this.poDao = GGC_GCircleDB.getInstance(instance).ciSKDAO();
        this.clientDao = GGC_GCircleDB.getInstance(instance).ClientDao();
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poApi = new GConnectApi(instance);
        this.poSession = ClientSession.getInstance(instance);
        this.poSalesKit = new ClientMasterSalesKit(instance);
    }

    public String getMessage() {
        return message;
    }

    public LiveData<EClientInfoSalesKit> GetClientDetail(){
        return poDao.getClientInfo();
    }
    public LiveData<EClientInfo> GetClientInfo(){
        return clientDao.getClientInfo();
    }

    public EClientInfo getClientInfo(){
        return clientDao.GetClientInfo();
    }

//    public LiveData<DClientInfo.ClientDetail> GetClientDetailForPreview(){
//        return poDao.GetClientDetailForPreview();
//    }

    public void LogoutUserSession(){
        poDao.DeleteProfile();
        poSalesKit.RemoveProfile();
        clientDao.RemoveSessions();
        poSession.LogoutUser();
    }
    public boolean ImportAccountInfo(){
        try{
            String lsResponse = WebClient.sendRequest(
                    poApi.getImportAccountInfoAPI(),
                    new JSONObject().toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = "Unable to retrieve server response.";
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(!lsResult.equalsIgnoreCase("success")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            EClientInfoSalesKit loDetail = poDao.GetUserInfo();
            loDetail.setClientID(loResponse.getString("sClientID"));
            loDetail.setLastName(loResponse.getString("sLastName"));
            loDetail.setFrstName(loResponse.getString("sFrstName"));
            loDetail.setMiddName(loResponse.getString("sMiddName"));
            loDetail.setSuffixNm(loResponse.getString("sSuffixNm"));
            loDetail.setMaidenNm(loResponse.getString("sMaidenNm"));
            loDetail.setGenderCd(loResponse.getString("cGenderCd"));
            loDetail.setCvilStat(loResponse.getString("cCvilStat"));
            loDetail.setBirthDte(loResponse.getString("dBirthDte"));
            loDetail.setBirthPlc(loResponse.getString("sBirthPlc"));
            loDetail.setHouseNo1(loResponse.getString("sHouseNo1"));
            loDetail.setAddress1(loResponse.getString("sAddress1"));
            loDetail.setBrgyIDx1(loResponse.getString("sBrgyIDx1"));
            loDetail.setTownIDx1(loResponse.getString("sTownIDx1"));
            loDetail.setHouseNo2(loResponse.getString("sHouseNo2"));
            loDetail.setAddress2(loResponse.getString("sAddress2"));
            loDetail.setBrgyIDx2(loResponse.getString("sBrgyIDx2"));
            loDetail.setTownIDx2(loResponse.getString("sTownIDx2"));
            loDetail.setMobileNo(loResponse.getString("sMobileNo"));
            loDetail.setEmailAdd(loResponse.getString("sEmailAdd"));
            loDetail.setVerified(loResponse.getInt("cVerified"));
            poDao.update(loDetail);
//                    loDetail.setImgeStat(loResponse.getString("cImgeStat"));
//                    loDetail.setImagePth(loResponse.getString("sImagePth"));
//                    loDetail.setImgeDate(loResponse.getString("dImgeDate"));
//                    AccountInfo loAcc = new AccountInfo(mContext);
//                    loAcc.setClientID(loResponse.getString("sClientID"));
//                    loAcc.setLastname(loResponse.getString("sLastName"));
//                    loAcc.setFirstName(loResponse.getString("sFrstName"));
//                    loAcc.setMiddlename(loResponse.getString("sMiddName"));
//                    loAcc.setSuffix(loResponse.getString("sSuffixNm"));
//                    loAcc.setGender(loResponse.getString("cGenderCd"));
//                    loAcc.setCivilStatus(loResponse.getString("cCvilStat"));
//                    loAcc.setBirthdate(loResponse.getString("dBirthDte"));
//                    loAcc.setBirthplace(loResponse.getString("sBirthPlc"));
//                    loAcc.setHouseNo(loResponse.getString("sHouseNo1"));
//                    loAcc.setAddress(loResponse.getString("sAddress1"));
//                    loAcc.setTownName(loResponse.getString("sTownIDx1"));
//                    loAcc.setBarangay(loResponse.getString("sBrgyIDx1"));

//                    String lsClient = loAcc.getClientID();
//                    String lsLastNm = loAcc.getLastName();
//                    String lsFrstNm = loAcc.getFirstName();
//                    String lsBirthD = loAcc.getBirthdate();
//                    String lsBirthP = loAcc.getBirthplace();
//                    if(lsClient.isEmpty()){
//                        if(lsLastNm.isEmpty() && lsFrstNm.isEmpty() &&
//                                lsBirthD.isEmpty() && lsBirthP.isEmpty()) {
//                            loAcc.setVerifiedStatus(0);
//                        } else {
//                            loAcc.setVerifiedStatus(2);
//                        }
//                    } else {
//                        loAcc.setVerifiedStatus(1);
//                    }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
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

            String lsResponse = WebClient.sendRequest(
                    poApi.getCreateNewClientAPI(),
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

    public boolean RetrieveShipAndBillAddress(){
        try{
            String lsResponse = WebClient.sendRequest(
                    poApi.getRetrieveProfilePictureAPI(),
                    new JSONObject().toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = "Unable to retrieve server response.";
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

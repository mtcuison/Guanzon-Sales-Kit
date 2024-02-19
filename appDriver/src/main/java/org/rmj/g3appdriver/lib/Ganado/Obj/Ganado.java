package org.rmj.g3appdriver.lib.Ganado.Obj;

import static org.rmj.g3appdriver.dev.Api.ApiResult.SERVER_NO_RESPONSE;
import static org.rmj.g3appdriver.dev.Api.ApiResult.getErrorMessage;
import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.Entities.ECountryInfo;
import org.rmj.g3appdriver.GCircle.room.Entities.EGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.Entities.ERelation;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.lib.Etc.Country;
import org.rmj.g3appdriver.lib.Etc.Relation;
import org.rmj.g3appdriver.lib.Ganado.pojo.ClientInfo;
import org.rmj.g3appdriver.lib.Ganado.pojo.FinancierInfo;
import org.rmj.g3appdriver.lib.Ganado.pojo.InquiryInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Ganado {
    private static final String TAG = Ganado.class.getSimpleName();

    private final Application instance;
    private final DGanadoOnline poDao;
    private final ClientSession poSession;
    private final GCircleApi poApi;
    private final HttpHeaders poHeaders;
    private final Relation poRelate;
    private final Country poCountry;


    private String message;


    private String nLatitude, nLongitude;

    private static final int REQUEST_LOCATION = 1;
    private LocationManager locManager;
    public Ganado(Application instance) {
        this.instance = instance;
        this.poDao = GGC_GCircleDB.getInstance(instance).ganadoDao();
        this.poSession = ClientSession.getInstance(instance);
        this.poApi = new GCircleApi(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
        this.poRelate = new Relation(instance);
        this.poCountry = new Country(instance);
        locManager = (LocationManager) instance.getSystemService(Context.LOCATION_SERVICE);
    }

    public String getMessage() {
        return message;
    }

    public LiveData<List<ERelation>> GetRelations(){
        return poRelate.GetRelations();
    }
    public LiveData<List<ECountryInfo>> GetCountry(){
        return poCountry.getAllCountryInfo();
    }

    public EGanadoOnline GetInquiry(String TransNox){
        return poDao.GetInquiry(TransNox);
    }
    public void RemoveInquiry(String TransNox){
        poDao.RemoveInquiry(TransNox);
    }

    public String CreateInquiry(InquiryInfo loInfo){
        try{
            InquiryInfo.InquiryInfoValidator loValid = new InquiryInfo.InquiryInfoValidator();
            if(!loValid.isDataValid(loInfo)){
                message = loValid.getMessage();
                return null;
            }

            EGanadoOnline loDetail = new EGanadoOnline();
            String lsTransNo = CreateUniqueID();
            loDetail.setTransNox(lsTransNo);

            loDetail.setTransact(AppConstants.CURRENT_DATE());
            loDetail.setGanadoTp(loInfo.getGanadoTp());
            loDetail.setPaymForm(loInfo.getPaymForm());

            JSONObject joProdct = new JSONObject();
            joProdct.put("sBrandIDx", loInfo.getBrandIDx());
            joProdct.put("sModelIDx", loInfo.getModelIDx());
            joProdct.put("sColorIDx", loInfo.getColorIDx());
            joProdct.put("nSelPrice", loInfo.getCashPrce());
            joProdct.put("dPricexxx", loInfo.getPricexxx());
            loDetail.setProdInfo(joProdct.toString());

            JSONObject joPayment = new JSONObject();
            joPayment.put("sTermIDxx", loInfo.getTermIDxx());
            joPayment.put("nDownPaym", loInfo.getDownPaym());
            joPayment.put("nMonthAmr", loInfo.getnMonthAmr());
            loDetail.setPaymInfo(joPayment.toString());

            JSONArray laJson = new JSONArray();

            JSONObject joInfo = new JSONObject();
            joInfo.put("", "");
            laJson.put(joInfo);

            laJson.toString();

            loDetail.setTargetxx(loInfo.getTargetxx());
            loDetail.setFollowUp("");
            loDetail.setRemarksx("");
            loDetail.setReferdBy(poSession.getUserID());
            loDetail.setRelatnID(loInfo.getRelatnID());
            loDetail.setCreatedx(AppConstants.DATE_MODIFIED());
            loDetail.setTranStat("0");
            loDetail.setSendStat("");
            loDetail.setModified(AppConstants.DATE_MODIFIED());
            loDetail.setLastUpdt("");
            loDetail.setBranchCD("");
            poDao.Save(loDetail);

            return lsTransNo;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return null;
        }
    }

    public boolean SaveClientInfo(ClientInfo loInfo){
        try {
            if(!loInfo.isDataValid()){
                message = loInfo.getMessage();
                return false;
            }

            EGanadoOnline loDetail = poDao.GetInquiry(loInfo.getsTransNox());

            if(loDetail == null){
                message = "Unable to find record to update.";
                return false;
            }

            JSONObject joClient = new JSONObject();
            joClient.put("sLastName", loInfo.getLastName());
            joClient.put("sFrstName", loInfo.getFrstName());
            joClient.put("sMiddName", loInfo.getMiddName());
            joClient.put("sSuffixNm", loInfo.getSuffixNm());
            joClient.put("sMaidenNm", loInfo.getMaidenNm());
            joClient.put("cGenderCd", loInfo.getGenderCd());
            joClient.put("dBirthDte", loInfo.getBirthDte());
            joClient.put("sBirthPlc", loInfo.getBirthPlc());
            joClient.put("sHouseNox", loInfo.getHouseNox());
            joClient.put("sAddressx", loInfo.getAddressx());
            joClient.put("sTownIDxx", loInfo.getTownIDxx());
            joClient.put("sMobileNo", loInfo.getMobileNo());
            joClient.put("sEmailAdd", loInfo.getEmailAdd());

            String lsClient = loInfo.getLastName() + ", " + loInfo.getFrstName();

            if(!loInfo.getMiddName().isEmpty()){
                lsClient = lsClient + " " + loInfo.getMiddName();
            }

            if(!loInfo.getSuffixNm().isEmpty()){
                lsClient = lsClient + " " + loInfo.getSuffixNm();
            }

            loDetail.setClientNm(lsClient);
            loDetail.setClntInfo(joClient.toString());
            loDetail.setRelatnID(loInfo.getsReltionx());
            poDao.Update(loDetail);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }


    public boolean SaveFinancierInfo(FinancierInfo loInfo){
        try {
            if(!loInfo.isDataValid()){
                message = loInfo.getMessage();
                return false;
            }

            EGanadoOnline loDetail = poDao.GetInquiry(loInfo.getsTransNox());

            if(loDetail == null){
                message = "Unable to find record to update.";
                return false;
            }

            JSONObject joFinancier = new JSONObject();
            joFinancier.put("sLastName", loInfo.getLastName());
            joFinancier.put("sFrstName", loInfo.getFrstName());
            joFinancier.put("sMiddName", loInfo.getMiddName());
            joFinancier.put("sSuffixNm", loInfo.getSuffixNm());
            joFinancier.put("sAddressx", loInfo.getAddressx());
            joFinancier.put("sCntryCde", loInfo.getCountryCode());
            joFinancier.put("sMobileNo", loInfo.getMobileNo());
            joFinancier.put("sWhatsApp", loInfo.getsWhatsApp());
            joFinancier.put("sWChatApp", loInfo.getsWeChatApp());
            joFinancier.put("sFbAccntx", loInfo.getsFbAccntx());
            joFinancier.put("sEmailAdd", loInfo.getEmailAdd());
            joFinancier.put("sFIncomex", loInfo.getRangeOfIncome());
            joFinancier.put("sReltionx", loInfo.getsReltionx());

            String loFinancier = joFinancier.toString();
            loDetail.setFinancex(loFinancier);
            poDao.Update(loDetail);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }

    public boolean SaveInquiry(String TransNox){
        try{
            EGanadoOnline loDetail = poDao.GetInquiry(TransNox);

            if(loDetail == null){
                message = "Unable to find record to update.";
                return false;
            }

            JSONObject params = new JSONObject();
            params.put("sClientNm", loDetail.getClientNm());
            params.put("cGanadoTp", loDetail.getGanadoTp());
            params.put("cPaymForm", loDetail.getPaymForm());
            params.put("dCreatedx", loDetail.getCreatedx());
            params.put("dTargetxx", loDetail.getTargetxx());
            params.put("sRelatnID", loDetail.getRelatnID());
//            params.put("nLatitude", 1.00);
//            params.put("nLongitud", 2.00);
            params.put("nLatitude", nLatitude);
            params.put("nLongitud", nLongitude);
            Log.e("nLatitude", String.valueOf(nLatitude));
            Log.e("nLongitud", String.valueOf(nLongitude));

            params.put("sReferdBy", poSession.getUserID());
            params.put("sClntInfo", loDetail.getClntInfo());
            params.put("sFinancex", loDetail.getFinancex());
            params.put("sProdInfo", loDetail.getProdInfo());
            params.put("sPaymInfo", loDetail.getPaymInfo());
            params.put("cSourcexx", "2");

            String lsResponse = WebClient.sendRequest(
                    poApi.getSubmitInquiry(),
                    params.toString(),
                    poHeaders.getHeaders());

            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return false;
            }
//            message = lsResponse;
            Log.e("lsResponse", lsResponse);
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            String lsTrasNox = loResponse.getString("sTransNox");
            poDao.UpdateSentInquiry(
                    loDetail.getTransNox(),
                    lsTrasNox);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e) + message;
            return false;
        }
    }

    public boolean ImportInquiries(){
        try{
            JSONObject params = new JSONObject();
            params.put("cSourcexx", "2");
//            EGanadoOnline loGanado = poDao.GetLatestData();

            /*if(loGanado != null){
                params.put("timestamp", loGanado.getTimeStmp());
            }*/

//            String lsResponse = WebClient.sendRequest(
//                    poApi.getDownloadInquiries(),
//                    params.toString(),
//                    poHeaders.getHeaders());

            String lsResponse = WebClient.sendRequest(
                    poApi.getUrlImportSKPerformance(),
                    params.toString(),
                    poHeaders.getHeaders());
            Log.e(TAG, poApi.getUrlImportSKPerformance());
            if(lsResponse == null){
                message = SERVER_NO_RESPONSE;
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = getErrorMessage(loError);
                return false;
            }

            JSONArray laJson = loResponse.getJSONArray("detail");
            for(int x = 0; x < laJson.length(); x++){
                JSONObject loJson = laJson.getJSONObject(x);

                String lsTransNo = loJson.getString("sTransNox");

                EGanadoOnline loDetail = poDao.GetInquiry(lsTransNo);

                if(loDetail == null){
                    EGanadoOnline loInfo = new EGanadoOnline();
                    loInfo.setTransNox(loJson.getString("sTransNox"));
                    loInfo.setTransact(loJson.getString("dTransact"));
                    loInfo.setGanadoTp(loJson.getString("cGanadoTp"));
                    loInfo.setPaymForm(loJson.getString("cPaymForm"));
                    loInfo.setClientNm(loJson.getString("sClientNm"));
                    loInfo.setClntInfo(loJson.getString("sCltInfox"));
                    loInfo.setFinancex(loJson.getString("sFinancex"));
                    loInfo.setProdInfo(loJson.getString("sPrdctInf"));
                    loInfo.setPaymInfo(loJson.getString("sPaymInfo"));
                    loInfo.setTargetxx(loJson.getString("dTargetxx"));
                    loInfo.setFollowUp(loJson.getString("dFollowUp"));
                    loInfo.setRemarksx(loJson.getString("sRemarksx"));
                    loInfo.setReferdBy(loJson.getString("sReferdBy"));
                    loInfo.setRelatnID(loJson.getString("sRelatnID"));
                    loInfo.setCreatedx(loJson.getString("dCreatedx"));
                    loInfo.setTranStat(loJson.getString("cTranStat"));
                    loInfo.setTimeStmp(loJson.getString("dTimeStmp"));
                    poDao.Save(loInfo);
                    Log.d(TAG, "Inquiry record has been saved!");
                } else {

                    loDetail.setTransNox(loJson.getString("sTransNox"));
                    loDetail.setTransact(loJson.getString("dTransact"));
                    loDetail.setGanadoTp(loJson.getString("cGanadoTp"));
                    loDetail.setPaymForm(loJson.getString("cPaymForm"));
                    loDetail.setClientNm(loJson.getString("sClientNm"));
                    loDetail.setClntInfo(loJson.getString("sCltInfox"));
                    loDetail.setFinancex(loJson.getString("sFinancex"));
                    loDetail.setProdInfo(loJson.getString("sPrdctInf"));
                    loDetail.setPaymInfo(loJson.getString("sPaymInfo"));
                    loDetail.setTargetxx(loJson.getString("dTargetxx"));
                    loDetail.setFollowUp(loJson.getString("dFollowUp"));
                    loDetail.setRemarksx(loJson.getString("sRemarksx"));
                    loDetail.setReferdBy(loJson.getString("sReferdBy"));
                    loDetail.setRelatnID(loJson.getString("sRelatnID"));
                    loDetail.setCreatedx(loJson.getString("dCreatedx"));
                    loDetail.setTranStat(loJson.getString("cTranStat"));
                    loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                    poDao.Update(loDetail);
                    Log.d(TAG, "Inquiry record has been updated!");

//                    Date ldDate1 = SQLUtil.toDate(loDetail.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
//                    Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
//
//                    if (!ldDate1.equals(ldDate2)) {
//                        loDetail.setTransNox(loJson.getString("sTransNox"));
//                        loDetail.setTransact(loJson.getString("dTransact"));
//                        loDetail.setGanadoTp(loJson.getString("cGanadoTp"));
//                        loDetail.setPaymForm(loJson.getString("cPaymForm"));
//                        loDetail.setClientNm(loJson.getString("sClientNm"));
//                        loDetail.setClntInfo(loJson.getString("sCltInfox"));
//                        loDetail.setFinancex(loJson.getString("sFinancex"));
//                        loDetail.setProdInfo(loJson.getString("sPrdctInf"));
//                        loDetail.setPaymInfo(loJson.getString("sPaymInfo"));
//                        loDetail.setTargetxx(loJson.getString("dTargetxx"));
//                        loDetail.setFollowUp(loJson.getString("dFollowUp"));
//                        loDetail.setRemarksx(loJson.getString("sRemarksx"));
//                        loDetail.setReferdBy(loJson.getString("sReferdBy"));
//                        loDetail.setRelatnID(loJson.getString("sRelatnID"));
//                        loDetail.setCreatedx(loJson.getString("dCreatedx"));
//                        loDetail.setTranStat(loJson.getString("cTranStat"));
//                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
//                        poDao.Update(loDetail);
//                        Log.d(TAG, "Inquiry record has been updated!");
//                    }
                }
            }

            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }

    public LiveData<List<EGanadoOnline>> GetInquiries(){
        return poDao.GetInquiries();
    }
    public LiveData<List<EGanadoOnline>> GetByAgentInquiries(String UserID){
        return poDao.GetByAgentInquiries(UserID);
    }

    private String CreateUniqueID(){
        String lsUniqIDx = "";
        try{
            String lsBranchCd = "MX01";
            String lsCrrYear = new SimpleDateFormat("yy", Locale.getDefault()).format(new Date());
            StringBuilder loBuilder = new StringBuilder(lsBranchCd);
            loBuilder.append(lsCrrYear);

            int lnLocalID = poDao.GetRowsCountForID() + 1;
            String lsPadNumx = String.format("%05d", lnLocalID);
            loBuilder.append(lsPadNumx);
            lsUniqIDx = loBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, lsUniqIDx);
        return lsUniqIDx;
    }
    public void InitGeoLocation(Activity poActivty){
        int LOCATION_REFRESH_TIME = 2000; // 15 seconds to update
        int LOCATION_REFRESH_DISTANCE = 500; // 500 meters to update

        if (ActivityCompat.checkSelfPermission(instance, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(instance, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            locManager= (LocationManager) instance.getSystemService(instance.LOCATION_SERVICE);

            Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);
            if (location != null) {
                nLatitude = String.valueOf(location.getLatitude());
                nLongitude = String.valueOf(location.getLongitude());
            } else  if (location1 != null) {
                nLatitude = String.valueOf(location1.getLatitude());
                nLongitude = String.valueOf(location1.getLongitude());
            } else  if (location2 != null) {
                nLatitude = String.valueOf(location2.getLatitude());
                nLongitude = String.valueOf(location2.getLongitude());
            }else{
                message = "Unable to Trace your location";
//                listner.OnFailedRetrieve("Unable to Trace your location");
            }

//            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                    LOCATION_REFRESH_DISTANCE, mLocationListener);

        }else {

            ActivityCompat.requestPermissions(poActivty, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }
    // Stop location updates
    public void stopLocationUpdates() {
        if (locManager != null) {
            locManager.removeUpdates(mLocationListener);
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            nLatitude = String.valueOf(location.getLatitude());
            nLongitude = String.valueOf(location.getLongitude());
            Log.e("nLatitude",nLatitude);
            Log.e("nLongitude",nLongitude);
        }
    };
}

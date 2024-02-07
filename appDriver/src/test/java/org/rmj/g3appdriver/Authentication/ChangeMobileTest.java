package org.rmj.g3appdriver.Authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.utils.SQLUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChangeMobileTest {
    private Map<String, String> headers;
    @Before
    public void SetUp(){
        /**NOTE: RUN THIS ON 192.168.10.224 (TEST DATABASE) TO INITIALIZE HEADERS PROPERLY
         * EXECUTE SQL: SELECT * FROM xxxSysUserLog WHERE  sUserIDxx = 'GAP0190004' AND sLogNoxxx = "GAP023110901" AND sProdctID = "gRider";
         * ACTION: Change 'dLogInxxx' column date to current date.*/

        Calendar calendar = Calendar.getInstance();
        //Create the header section needed by the API
        headers = new HashMap<String, String>();

        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("g-api-id", "gRider");
        headers.put("g-api-client", "GGC_BM001"); //CLIENT ID
        headers.put("g-api-log", "GAP023110901"); //LOG NO
        headers.put("g-api-imei", "GMC_SEG09"); //IMEI NO
        headers.put("g-api-key", SQLUtil.dateFormat(calendar.getTime(), "yyyyMMddHHmmss"));
        headers.put("g-api-hash", org.apache.commons.codec.digest.DigestUtils.md5Hex((String)headers.get("g-api-imei") + (String)headers.get("g-api-key")));
        headers.put("g-api-user", "GAP0190004"); //USER ID
        headers.put("g-api-mobile", "09260375777"); //USER MOBILE
        headers.put("g-api-token", "12312312"); //USER TOKEN
    }
    @Test
    public void TestUpdateAddress(){
        String sURL = "http://192.168.10.68:8080/security/request_mobile_update.php";
        try {
            JSONObject loParams = new JSONObject();

            loParams.put("sTransNox", CreateUniqueID());
            loParams.put("sClientID", "GGC_BM001");
            loParams.put("cReqstCDe", "0");
            loParams.put("sMobileNo", "09275408234");
            loParams.put("cPrimaryx", "1");
            loParams.put("sRemarksx", "");
            loParams.put("sSourceCD", "NULL");
            loParams.put("sSourceNo", "NULL");

            /**IF SUCCESSFUL REQUEST, SEARCH RETURNED TRANSACTION NO TO TABLE--> MOBILE_UPDATE_REQUEST*/
            String response = WebClient.sendRequest(sURL, loParams.toString(), (HashMap<String, String>) headers);
            System.out.println(response);

            JSONObject loRes = new JSONObject(response);
            assertEquals("success", loRes.get("result")); //result should be success
            assertNotNull(loRes.get("sTransNox")); //transaction no returned should not be null
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private String CreateUniqueID() {
        String lsUniqIDx = "";
        try {
            String lsBranchCd = "MX01";
            String lsCrrYear = new SimpleDateFormat("yy", Locale.getDefault()).format(new Date());
            StringBuilder loBuilder = new StringBuilder(lsBranchCd);
            loBuilder.append(lsCrrYear);

            int lnLocalID = 1;
            String lsPadNumx = String.format("%05d", lnLocalID);
            loBuilder.append(lsPadNumx);
            lsUniqIDx = loBuilder.toString();

            System.out.println(lsUniqIDx);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lsUniqIDx;
    }
}

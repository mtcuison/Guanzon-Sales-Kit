package org.rmj.g3appdriver.SalesKit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.utils.SQLUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class SelectUplineTest {
    private Map<String, String> headers;
    @Before
    public void SetUp(){
        /**NOTE: RUN THIS ON 192.168.10.224 (TEST DATABASE) TO INITIALIZE HEADERS PROPERLY
         * RUN: SELECT * FROM xxxSysUserLog WHERE  sUserIDxx = 'GAP0190004' AND sLogNoxxx = "GAP023110901" AND sProdctID = "gRider";
         * REQUIRED: Change 'dLogInxxx' column date to current date and time.*/

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
    public void SaveUpline(){
        try {
            /**CHANGE VALUES BELOW AS PARAMS*/
            String sUserID = "";
            String sUpprndIdxx = "";
            String nRoleId = "";
            String cRecdStat = "";

            String sURL = "http://192.168.10.68:8080/saleskit/submit_upline.php";

            JSONObject params = new JSONObject();
            params.put("sUserIDxx", sUserID);
            params.put("nRoleIdxx", nRoleId);
            params.put("sUpprndIdxx", sUpprndIdxx);
            params.put("cRecdStat", cRecdStat);

            String response = WebClient.sendRequest(sURL, params.toString(), (HashMap<String, String>) headers);
            System.out.println(response);

            JSONObject loRes = new JSONObject(response);
            assertEquals("success", loRes.get("result")); //result should be success

            JSONArray loArr = loRes.getJSONArray("agents");
            assertTrue(loArr.length() > 0); //result should be greater than 0

            JSONObject loObj = loArr.getJSONObject(0);
            System.out.println(loObj);
            assertNotNull(loObj);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

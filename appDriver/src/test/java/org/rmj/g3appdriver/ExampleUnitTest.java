/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.g3appdriver;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.rmj.g3appdriver.dev.Api.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.utils.SQLUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private String TAG = getClass().getSimpleName();
    private Map<String, String> headers;
    @Before
    public void SetUp(){
        /*NOTE: RUN THIS ON 192.168.10.224 (TEST DATABASE) TO INITIALIZE HEADERS PROPERLY
         * RUN: SELECT * FROM xxxSysUserLog WHERE  sUserIDxx = 'GAP0190004' AND sLogNoxxx = "GAP023110901" AND sProdctID = "gRider";
         * REQUIRED: Change 'dLogInxxx' column date to current date.*/

        Calendar calendar = Calendar.getInstance();
        //Create the header section needed by the API
        headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("g-api-id", "gRider");
        headers.put("g-api-client", "GGC_BM001");
        headers.put("g-api-log", "GAP023110901");
        headers.put("g-api-imei", "GMC_SEG09");
        headers.put("g-api-key", SQLUtil.dateFormat(calendar.getTime(), "yyyyMMddHHmmss"));
        headers.put("g-api-hash", org.apache.commons.codec.digest.DigestUtils.md5Hex((String)headers.get("g-api-imei") + (String)headers.get("g-api-key")));
        headers.put("g-api-user", "GAP0190004");
        headers.put("g-api-mobile", "09260375777");
        headers.put("g-api-token", "12312312");

    }
    @Test
    public void TestOTP(){
        String sURL = "http://192.168.10.68:8080/security/OTPGenerator.php";
        try {
            JSONObject params = new JSONObject();
            params.put("mobile_number", "09993095066");

            String response = WebClient.sendRequest(sURL, params.toString(), (HashMap<String, String>) headers);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
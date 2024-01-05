package org.rmj.g3appdriver.SalesKit;

import static org.junit.Assert.assertTrue;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.rmj.g3appdriver.GCircle.Account.EmployeeMaster;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.etc.AppConfigPreference;

@RunWith(AndroidJUnit4.class)
public class ImportKpopAgentTest {
    private Application instance;
    private String TAG = getClass().getSimpleName();
    private EmployeeMaster.UserAuthInfo loAuth;
    private EmployeeMaster poUser;
    private AppConfigPreference loConfig;
    private SalesKit poSalesKit;
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    @Before
    public void setUp() throws Exception{
        instance= ApplicationProvider.getApplicationContext();
        loConfig = AppConfigPreference.getInstance(instance);
        loConfig.setProductID("gRider");

        AppConfigPreference.getInstance(instance).setTestCase(true); //use this to test api on device local ip

        poUser = new EmployeeMaster(instance);
        poSalesKit = new SalesKit(instance);

        //WIPE DATA AND RUN TestLoginAccount BEFORE RUNNING THIS TEST, FOR VALID LOGIN AND SUCCESS
        //"INVALID LOG / INVALID AUTH DETECTED" - WIPE EMULATOR DATA
        //"INVALID MOBILE NUMBER DETECTED" - CHANGE lsMobileN VALUE TO SPECIFIC NUMBER
        loAuth = new EmployeeMaster.UserAuthInfo("mikegarcia8748@gmail.com", "123456", "09171870011");
    }
    @Test
    public void TestImportAgents(){
        Boolean isImported = poSalesKit.ImportKPOPAgent();
        System.out.println(poSalesKit.getMessage());
        assertTrue(isImported);
    }
}

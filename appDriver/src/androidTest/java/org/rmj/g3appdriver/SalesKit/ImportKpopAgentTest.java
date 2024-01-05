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
    private AppConfigPreference loConfig;
    private SalesKit poSalesKit;
    private EmployeeMaster poUser;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    @Before
    public void setUp() throws Exception{
        instance= ApplicationProvider.getApplicationContext();
        loConfig = AppConfigPreference.getInstance(instance);
        loConfig.setProductID("gRider");

        AppConfigPreference.getInstance(instance).setTestCase(true); //use this to test api on device local ip

        poSalesKit = new SalesKit(instance);
        poUser = new EmployeeMaster(instance);

        EmployeeMaster.UserAuthInfo loAuth = new EmployeeMaster.UserAuthInfo("mikegarcia8748@gmail.com", "123456", "09171870011");
        poUser.AuthenticateUser(loAuth);
    }
    /*RUNNING TEST SHOULD BE ONCE ONLY, RUN ONE TEST ONLY AT A TIME*/
    @Test
    public void TestImportKPopAgents(){
        Boolean isImported = poSalesKit.ImportKPOPAgent();
        System.out.println(poSalesKit.getMessage());
        assertTrue(isImported); //if no printed error and is successfully asserted, import is successful
    }
    @Test
    public void TestImportGanadoAgents(){
        Boolean isImported = poSalesKit.ImportAgent();
        System.out.println(poSalesKit.getMessage());
        assertTrue(isImported); //if no printed error and is successfully asserted, import is successful
    }
}

package org.rmj.g3appdriver.lib.Account;

import android.app.Application;

import org.rmj.g3appdriver.GCircle.Api.GCircleApi;
import org.rmj.g3appdriver.GConnect.Account.ClientMaster;
import org.rmj.g3appdriver.dev.Api.HttpHeaders;
import org.rmj.g3appdriver.etc.AppConfigPreference;

public class EmailSender {
    private Application instance;
    private AppConfigPreference poConfig;
    private GCircleApi loApi;
    private HttpHeaders poHeaders;
    private ClientMaster poUser;
    private String sMobile;
    private String message;

    public EmailSender(Application instance){
        this.instance = instance;

        this.poConfig = AppConfigPreference.getInstance(instance);
        this.sMobile = poConfig.getMobileNo(); //based on user logged account
        this.loApi = new GCircleApi(instance);
        this.poUser = new ClientMaster(instance);
        this.poHeaders = HttpHeaders.getInstance(instance);
    }
}

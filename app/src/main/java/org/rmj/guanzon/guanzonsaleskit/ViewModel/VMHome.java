package org.rmj.guanzon.guanzonsaleskit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.Account.ClientMasterSalesKit;
import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.GConnect.room.Entities.EEvents;
import org.rmj.g3appdriver.GConnect.room.Entities.EPromo;
import org.rmj.g3appdriver.SalesKit.Entities.EKPOPAgentRole;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.lib.Notifications.Obj.Notification;
import org.rmj.g3appdriver.lib.Promotions.GPromos;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzon.guanzonsaleskit.Service.DataSyncService;

import java.util.List;

public class VMHome extends AndroidViewModel {
    private static final String TAG =VMHome.class.getSimpleName();
    private String lsPromo, lsPmUrl;
    private String lsEvent, lsEvUrl;
    private final AppConfigPreference poConfig;
    private final ConnectionUtil poConn;
    private final GPromos poPromoEvent;
    private final ClientMasterSalesKit poClientSK;
    private final SalesKit poSaleskit;
    private final ClientSession poSession;
    private final DataSyncService poNetRecvr;

    private final Notification poNotif;
    public VMHome(@NonNull Application application) {
        super(application);

        this.poNetRecvr = new DataSyncService(application);
        this.poConn = new ConnectionUtil(application);
        this.poPromoEvent = new GPromos(application);
        this.poClientSK = new ClientMasterSalesKit(application);
        this.poSaleskit = new SalesKit(application);
        this.poSession = ClientSession.getInstance(application);
        this.poNotif = new Notification(application);
        this.poConfig = AppConfigPreference.getInstance(application);
        this.poConfig.setIsAppFirstLaunch(false);
    }

    public LiveData<Integer> GetUnreadMessagesCount(){
        return poNotif.GetUnreadNotificationCount();
    }
    public DataSyncService getInternetReceiver() {
        return poNetRecvr;
    }
    public LiveData<EKPOPAgentRole> GetKPOPAgentInfo(){
        return poSaleskit.GetKPOPAgentInfo(poSession.getUserID());
    }
    public LiveData<EClientInfoSalesKit> GetCompleteProfile(){
        return poClientSK.GetProfileAccount();
    }
    public LiveData<List<EPromo>> GetPromoLinkList(){
        return poPromoEvent.GetPromotions();
    }
    public void CheckPromotions(OnCheckPromotions listener){
        TaskExecutor.Execute(listener, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
            if(poPromoEvent.CheckPromo() != null){
                EPromo loPromo = poPromoEvent.CheckPromo();
                lsPromo = loPromo.getPromoUrl();
                lsPmUrl = loPromo.getImageUrl();
            }

            if(poPromoEvent.CheckPromo() != null){
                EEvents loEvent = poPromoEvent.CheckEvent();
                lsEvent = loEvent.getImageURL();
                lsEvUrl = loEvent.getEventURL();
            }
            return null;
            }

            @Override
            public void OnPostExecute(Object object) {
                if(lsPromo != null) {
                    listener.OnCheckPromos(lsPromo, lsPmUrl);
                }
                if(lsEvent != null){
                    listener.OnCheckEvents(lsEvent, lsEvUrl);
                }
                if(lsEvent == null && lsPromo == null){
                    listener.NoPromos();
                }

            }
        });
    }
    public interface OnCheckPromotions {
        void OnCheckPromos(String args1, String args2);
        void OnCheckEvents(String args1, String args2);
        void NoPromos();
    }
}
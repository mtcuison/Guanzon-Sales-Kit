package org.rmj.guanzon.guanzonsaleskit.ViewModel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GConnect.room.Entities.EEvents;
import org.rmj.g3appdriver.GConnect.room.Entities.EPromo;
import org.rmj.g3appdriver.etc.AppConfigPreference;
import org.rmj.g3appdriver.lib.Promotions.GPromos;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMHome extends AndroidViewModel {
    private static final String TAG =VMHome.class.getSimpleName();
    private String lsPromo, lsPmUrl;
    private String lsEvent, lsEvUrl;
    private final ConnectionUtil poConn;
    private Context mContext;
    private final AppConfigPreference poConfig;
    private final GPromos poPromoEvent;

    public interface OnViewGCardQrCode{
        void OnView(Bitmap foVal);
    }

    public interface OnValidateVerifiedUser{
        void OnValidate(String title, String message);
        void OnIncompleteAccountInfo();
        void OnAccountVerified();
        void OnAccountNotVerified();
        void OnFailed(String message);
    }

    public VMHome(@NonNull Application application) {
        super(application);
        this.mContext = application;
        this.poPromoEvent = new GPromos(application);
        this.poConn = new ConnectionUtil(application);
        poConfig = AppConfigPreference.getInstance(application);
        poConfig.setIsAppFirstLaunch(false);
    }

    public interface OnLogoutListener{
        void OnLogout();
    }

    public interface OnActionCallback{
        void OnLoad();
        void OnSuccess(String args);
        void OnFailed(String args);
    }

    public LiveData<List<EPromo>> GetPromoLinkList(){
        return poPromoEvent.GetPromotions();
    }

    public interface OnCheckPromotions {
        void OnCheckPromos(String args1, String args2);
        void OnCheckEvents(String args1, String args2);
        void NoPromos();
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

//    private class OnPromoCheckTask extends AsyncTask<String, Void, Boolean>{
//
//        private final OnCheckPromotions mListener;
//
//        private String lsPromo, lsPmUrl;
//        private String lsEvent, lsEvUrl;
//
//        public OnPromoCheckTask(OnCheckPromotions mListener) {
//            this.mListener = mListener;
//        }
//
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            poSystem = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.EXTRAS);
//            if(poSystem.CheckPromo() != null){
//                EPromo loPromo = poSystem.CheckPromo();
//                lsPromo = loPromo.getPromoUrl();
//                lsPmUrl = loPromo.getImageUrl();
//            }
//
//            if(poSystem.CheckEvents() != null){
//                EEvents loEvent = poSystem.CheckEvents();
//                lsEvent = loEvent.getImageURL();
//                lsEvUrl = loEvent.getEventURL();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            if(lsPromo != null) {
//                mListener.OnCheckPromos(lsPromo, lsPmUrl);
//            }
//            if(lsEvent != null){
//                mListener.OnCheckEvents(lsEvent, lsEvUrl);
//            }
//            if(lsEvent == null && lsPromo == null){
//                mListener.NoPromos();
//            }
//            super.onPostExecute(aBoolean);
//        }
//    }

//
//    public void ValidateUserVerification(OnValidateVerifiedUser listener){
//        new ValidateVerifiedTask(listener).execute();
//    }
//
//    private class ValidateVerifiedTask extends AsyncTask<Void, String, Integer>{
//
//        private final OnValidateVerifiedUser mListener;
//
//        private String message;
//
//        public ValidateVerifiedTask(OnValidateVerifiedUser mListener) {
//            this.mListener = mListener;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mListener.OnValidate("Guanzon App", "Checking account. Please wait...");
//        }
//
//        @Override
//        protected Integer doInBackground(Void... voids) {
//            try{
//                if(!poConn.isDeviceConnected()){
//                    message = "Unable to connect.";
//                    return 0;
//                }
//
//                if(!poClient.ImportAccountInfo()){
//                    message = poClient.getMessage();
//                    return 0;
//                }
//
//                EClientInfo loClient = poClient.GetClientInfo();
//                if(loClient == null){
//                    message = "Unable to find client record. Please try restarting the app and try again.";
//                    return 0;
//                }
//
//                if(!poClient.HasCompleteInfo()){
//                    message = "Client incomplete info.";
//                    return 3;
//                }
//
//                int lnVerified = loClient.getVerified();
//                if(lnVerified == 0){
//                    message = "Account not verified. Proceed to account verification.";
//                    return 2;
//                }
//
//                message = "Account verified. Proceed to loan application.";
//                return 1;
//            } catch (Exception e){
//                e.printStackTrace();
//                message = e.getMessage();
//                return 0;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//            super.onPostExecute(result);
//            switch (result){
//                case 0:
//                    mListener.OnFailed(message);
//                    break;
//                case 2:
//                    mListener.OnAccountNotVerified();
//                    break;
//                case 3:
//                    mListener.OnIncompleteAccountInfo();
//                    break;
//                default:
//                    mListener.OnAccountVerified();
//                    break;
//            }
//        }
//    }
}
package org.rmj.guanzongroup.agent.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.GCircle.room.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.GCircle.room.Entities.EGanadoOnline;
import org.rmj.g3appdriver.GConnect.Account.ClientSession;
import org.rmj.g3appdriver.SalesKit.Entities.EKPOPAgentRole;
import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMAgentInfo extends AndroidViewModel {

    private final Ganado poGanado;
    private final SalesKit poSys;

    private final ConnectionUtil poConn;
    private final ClientSession poSession;

    private String message;
    private String lsUserID;
    private String lsAgentLevel;


    public interface OnTaskExecute{
        void OnExecute();
        void OnSuccess();
        void OnFailed(String message);
    }

    public VMAgentInfo(@NonNull Application application) {
        super(application);
        this.poGanado = new Ganado(application);
        this.poSys = new SalesKit(application);
        this.poConn = new ConnectionUtil(application);
        this.poSession =  ClientSession.getInstance(application);
    }
    // TODO: Implement the ViewModel

    public LiveData<List<EGanadoOnline>> GetInquiries(String UserID){
        return poGanado.GetByAgentInquiries(UserID);
    }
    public String GetUserID(){
        return lsUserID;
    }
    public void setUserID(String UserID){
        lsUserID = UserID;
    }

    public String GetUserLvl(){
        return lsAgentLevel;
    }
    public void setUserLvl(String lsLvl){
        lsAgentLevel = lsLvl;
    }

    public LiveData<List<EKPOPAgentRole>> GetKPOPAgent(String UserIDxx){
        return poSys.GetKPOPAgent(UserIDxx);
    }
    public LiveData<EKPOPAgentRole> GetKPOPAgentInfo(){
        return poSys.GetKPOPAgentInfo(lsUserID);
    }
    public LiveData<DGanadoOnline.CountEntries> GetCountEntries(){
        return poSys.GetCountEntries(lsUserID);
    }
    public LiveData<DGanadoOnline.CountEntries> GetUserCountEntries(){
        return poSys.GetCountEntries(poSession.getUserID());
    }

    public void ImportKPOPAgent(VMAgentInfo.OnTaskExecute listener){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnExecute();
            }

            @Override
            public Object DoInBackground(Object args) {
                if (!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }

                if(!poSys.ImportKPOPAgent()){
                    message = poSys.getMessage();
                    return false;
                }

                return true;
            }

            @Override
            public void OnPostExecute(Object object) {
                boolean isSuccess = (boolean) object;
                if(!isSuccess){
                    listener.OnFailed(message);
                    return;
                }

                listener.OnSuccess();
            }
        });
    }

    public void ImportInquiries(VMAgentInfo.OnTaskExecute listener){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnExecute();
            }

            @Override
            public Object DoInBackground(Object args) {
                if (!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }

                if(!poGanado.ImportInquiries()){
                    message = poSys.getMessage();
                    return false;
                }

                return true;
            }

            @Override
            public void OnPostExecute(Object object) {
                boolean isSuccess = (boolean) object;
                if(!isSuccess){
                    listener.OnFailed(message);
                    return;
                }

                listener.OnSuccess();
            }
        });
    }
//    public void ImportPanaloRewards(String args, OnImportPanaloRewards listener){
//        new ImportRewardsTask(listener).execute(args);
//    }
//
//    private class ImportRewardsTask extends AsyncTask<String, Void, List<PanaloRewards>>{
//
//        private final OnImportPanaloRewards mListener;
//
//        private String message;
//
//        public ImportRewardsTask(OnImportPanaloRewards mListener) {
//            this.mListener = mListener;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mListener.OnImport();
//        }
//
//        @Override
//        protected List<PanaloRewards> doInBackground(String... strings) {
//            try{
//                if(!poConn.isDeviceConnected()){
//                    message = "Unable to connect";
//                    return null;
//                }
//
//                List<PanaloRewards> loRewards = poSys.GetRewards(strings[0]);
//                if(loRewards == null){
//                    message = poSys.getMessage();
//                    return null;
//                }
//
//                return loRewards;
//            } catch (Exception e){
//                e.printStackTrace();
//                message = e.getMessage();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(List<PanaloRewards> result) {
//            super.onPostExecute(result);
//            if(result == null){
//                mListener.OnFailed(message);
//            } else {
//                mListener.OnSuccess(result);
//            }
//        }
//    }
}
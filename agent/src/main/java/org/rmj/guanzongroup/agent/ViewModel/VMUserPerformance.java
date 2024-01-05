package org.rmj.guanzongroup.agent.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.rmj.g3appdriver.SalesKit.Obj.SalesKit;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMUserPerformance extends AndroidViewModel {
    private SalesKit poSaleskit;
    public VMUserPerformance(@NonNull Application application) {
        super(application);

        this.poSaleskit = new SalesKit(application);
    }

    public void GetUserPerformance(){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {

            }

            @Override
            public Object DoInBackground(Object args) {
                return null;
            }

            @Override
            public void OnPostExecute(Object object) {

            }
        });
    }
    public interface onGetCounts{
        void onCount(String title, String message);
        void onSuccess();
        void onFailed(String message);
    }

}

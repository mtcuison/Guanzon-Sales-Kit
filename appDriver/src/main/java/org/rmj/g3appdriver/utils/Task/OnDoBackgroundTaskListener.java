package org.rmj.g3appdriver.utils.Task;

public interface OnDoBackgroundTaskListener {
    Object DoInBackground(Object args);
    double OnPostExecute(Object object);
}

package org.rmj.g3appdriver.GCircle.room.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.GCircle.room.Entities.EClientInfoSalesKit;

@Dao
public interface DClientInfoSalesKit {

    @Insert
    void insert(EClientInfoSalesKit eClientInfo);
    @Update
    void update(EClientInfoSalesKit eClientInfo);

    @Query("SELECT * FROM Client_Profile_Info ORDER BY dTimeStmp DESC LIMIT 1")
    EClientInfoSalesKit GetLatestUserInfo();
    @Query("SELECT * FROM Client_Profile_Info")
    EClientInfoSalesKit GetUserInfo();
    @Query("SELECT * FROM Client_Profile_Info")
    LiveData<EClientInfoSalesKit> getClientInfo();
    @Query("SELECT sClientID FROM Client_Profile_Info")
    String GetClientID();
    @Query("SELECT sUserIDxx FROM Client_Profile_Info")
    String GetUserID();
    @Query("SELECT * FROM Client_Profile_Info")
    EClientInfoSalesKit GetClientInfo();
    @Query("DELETE FROM Client_Profile_Info")
    void DeleteProfile();
}

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

package org.rmj.g3appdriver.SalesKit.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.SalesKit.Entities.EKPOPAgentRole;

import java.util.List;

@Dao
public interface DKPOPAgentRole {

    @Insert
    void Save(EKPOPAgentRole foVal);

    @Update
    void Update(EKPOPAgentRole foVal);
    @Query("SELECT * FROM KPOP_Agent_Role")
    LiveData<List<EKPOPAgentRole>> getKPopAgentRole();

    @Query("SELECT sUserIDxx FROM KPOP_Agent_Role")
    String getUserID();

    @Query("SELECT COUNT(sUserIDxx) FROM KPOP_Agent_Role")
    int GetRowsCountForID();


    @Query("SELECT * FROM KPOP_Agent_Role ORDER BY dTimeStmp DESC LIMIT 1")
    EKPOPAgentRole GetLatestData();

    @Query("SELECT * FROM KPOP_Agent_Role WHERE sUserIDxx =:UserIDxx")
    EKPOPAgentRole GetKPOPAget(String UserIDxx);
}

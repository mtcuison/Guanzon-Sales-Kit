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

import org.rmj.g3appdriver.SalesKit.Entities.EAgentRole;

import java.util.List;

@Dao
public interface DAgentRole {

    @Insert
    void Save(EAgentRole foVal);

    @Update
    void Update(EAgentRole foVal);

    @Query("SELECT * FROM Agent_Role")
    LiveData<List<EAgentRole>> getAgentRole();

    @Query("SELECT nRoleIDxx FROM Agent_Role")
    String getRoleID();
    @Query("SELECT * FROM Agent_Role WHERE nRoleIDxx =:RoleIDxx")
    EAgentRole GetAgentRole(String RoleIDxx);
    @Query("SELECT * FROM Agent_Role ORDER BY dTimeStmp DESC LIMIT 1")
    EAgentRole GetLatestData();
}

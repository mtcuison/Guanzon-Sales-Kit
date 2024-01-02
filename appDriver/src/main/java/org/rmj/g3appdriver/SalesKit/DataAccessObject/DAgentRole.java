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

@Dao
public interface DAgentRole {

    @Insert
    void insert(EAgentRole agentRole);

    @Update
    void update(EAgentRole agentRole);

    @Query("SELECT * FROM Agent_Role")
    LiveData<EAgentRole> getAgentRole();

    @Query("SELECT nRoleIDxx FROM Agent_Role")
    String getRoleID();
}

package org.rmj.g3appdriver.SalesKit.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Agent_Role")
public class EAgentRole {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "nRoleIDxx")
    protected String RoleIDxx;
    @ColumnInfo(name = "sRoleDesc")
    protected String RoleDesc;
    @ColumnInfo(name = "cRecdStat")
    protected String RecdStat;
    @ColumnInfo(name = "sModified")
    protected String sModified;

    @ColumnInfo(name = "dModified")
    protected String dModified;

    @ColumnInfo(name = "dTimeStmp")
    protected String TimeStmp;


    public EAgentRole() {
    }

}

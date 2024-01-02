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
    public String RoleIDxx;
    @ColumnInfo(name = "sRoleDesc")
    public String RoleDesc;
    @ColumnInfo(name = "cRecdStat")
    public String RecdStat;
    @ColumnInfo(name = "sModified")
    public String sModified;

    @ColumnInfo(name = "dModified")
    public String dModified;

    @ColumnInfo(name = "dTimeStmp")
    public String TimeStmp;


    public EAgentRole() {
    }

}

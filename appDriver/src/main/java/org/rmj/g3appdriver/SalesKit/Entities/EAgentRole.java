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
    protected String Modifieds;

    @ColumnInfo(name = "dModified")
    protected String Modifiedd;

    @ColumnInfo(name = "dTimeStmp")
    protected String TimeStmp;


    public EAgentRole() {
    }


    @NonNull
    public String getRoleIDxx() {
        return RoleIDxx;
    }

    public void setRoleIDxx(@NonNull String roleIDxx) {
        RoleIDxx = roleIDxx;
    }

    public String getRoleDesc() {
        return RoleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        RoleDesc = roleDesc;
    }

    public String getRecdStat() {
        return RecdStat;
    }

    public void setRecdStat(String recdStat) {
        RecdStat = recdStat;
    }

    public String getModifieds() {
        return Modifieds;
    }

    public void setModifieds(String sModified) {
        this.Modifieds = sModified;
    }

    public String getModifiedd() {
        return Modifiedd;
    }

    public void setModifiedd(String dModified) {
        this.Modifiedd = dModified;
    }

    public String getTimeStmp() {
        return TimeStmp;
    }

    public void setTimeStmp(String timeStmp) {
        TimeStmp = timeStmp;
    }
}

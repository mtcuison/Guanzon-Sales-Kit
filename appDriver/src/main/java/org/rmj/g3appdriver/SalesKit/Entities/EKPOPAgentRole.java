package org.rmj.g3appdriver.SalesKit.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "KPOP_Agent_Role")
public class EKPOPAgentRole {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "sUserIDxx")
    public String UserIDxx;
    @ColumnInfo(name = "sEnrollBy")
    public String EnrollBy;
    @ColumnInfo(name = "dEnrolled")
    public String dEnrolled;
    @ColumnInfo(name = "nRoleIDxx")
    public String RoleIDxx;

    @ColumnInfo(name = "sUpprNdID")
    public String UpprNdID;

    @ColumnInfo(name = "cRecdStat")
    public String RecdStat;

    @ColumnInfo(name = "dTimeStmp")
    public String TimeStmp;


    public EKPOPAgentRole() {
    }


    @NonNull
    public String getUserIDxx() {
        return UserIDxx;
    }

    public void setUserIDxx(@NonNull String userIDxx) {
        UserIDxx = userIDxx;
    }

    public String getEnrollBy() {
        return EnrollBy;
    }

    public void setEnrollBy(String enrollBy) {
        EnrollBy = enrollBy;
    }

    public String getdEnrolled() {
        return dEnrolled;
    }

    public void setdEnrolled(String dEnrolled) {
        this.dEnrolled = dEnrolled;
    }

    public String getRoleIDxx() {
        return RoleIDxx;
    }

    public void setRoleIDxx(String roleIDxx) {
        RoleIDxx = roleIDxx;
    }

    public String getUpprNdID() {
        return UpprNdID;
    }

    public void setUpprNdID(String upprNdID) {
        UpprNdID = upprNdID;
    }

    public String getRecdStat() {
        return RecdStat;
    }

    public void setRecdStat(String recdStat) {
        RecdStat = recdStat;
    }

    public String getTimeStmp() {
        return TimeStmp;
    }

    public void setTimeStmp(String timeStmp) {
        TimeStmp = timeStmp;
    }

}

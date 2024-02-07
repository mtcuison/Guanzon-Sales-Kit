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
    public String Enrolled;
    @ColumnInfo(name = "nRoleIDxx")
    public String RoleIDxx;
    @ColumnInfo(name = "sUpprNdID")
    public String UpprNdID;
    @ColumnInfo(name = "sRoleDesc")
    public String sRoleDesc;
    @ColumnInfo(name = "sEmailAdd")
    public String sEmailAdd;
    @ColumnInfo(name = "sMobileNo")
    public String sMobileNo;
    @ColumnInfo(name = "sUserName")
    public String sUserName;
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

    public String getEnrolled() {
        return Enrolled;
    }

    public void setEnrolled(String dEnrolled) {
        this.Enrolled = dEnrolled;
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

    public String getsRoleDesc() {
        return sRoleDesc;
    }

    public void setsRoleDesc(String sRoleDesc) {
        this.sRoleDesc = sRoleDesc;
    }

    public String getsEmailAdd() {
        return sEmailAdd;
    }

    public void setsEmailAdd(String sEmailAdd) {
        this.sEmailAdd = sEmailAdd;
    }

    public String getsMobileNo() {
        return sMobileNo;
    }

    public void setsMobileNo(String sMobileNo) {
        this.sMobileNo = sMobileNo;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
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

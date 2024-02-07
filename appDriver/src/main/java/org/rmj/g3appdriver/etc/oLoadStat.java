package org.rmj.g3appdriver.etc;

public class oLoadStat {
    private boolean cPrmnGrnt = false;
    private boolean cIsLoginx = false;

    public oLoadStat(boolean cPrmnGrnt, boolean cIsLoginx) {
        this.cPrmnGrnt = cPrmnGrnt;
        this.cIsLoginx = cIsLoginx;
    }
    public void setPermissionGranted(boolean val) {
        this.cPrmnGrnt = val;
    }

    public void setLoginStat(boolean val) {
        this.cIsLoginx = val;
    }

    public boolean getPermissionGranted() {
        return cPrmnGrnt;
    }

    public boolean iscIsLoginx() {
        return cIsLoginx;
    }
}

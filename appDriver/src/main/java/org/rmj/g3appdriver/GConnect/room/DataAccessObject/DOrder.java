package org.rmj.g3appdriver.GConnect.room.DataAccessObject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import org.rmj.g3appdriver.GConnect.room.Entities.EItemCart;

@Dao
public interface DOrder {

    @Insert
    void SaveItemInfo(EItemCart foVal);

    @Query("DELETE FROM MarketPlace_Cart WHERE cBuyNowxx = '1' AND cCheckOut = '1'")
    void RemoveItemFromCart();

    //POJO use for list
    class OrderHistory{
        public String sTransNox;
        public String cTranStat;
        public double nTranTotl;
        public double nFreightx;
        public double nAmtPaidx;
        public double nProcPaym;
        public String sTermCode;
        public int nEntryNox;


        public int nQuantity;
        public double nUnitPrce;
        public double nDiscount;
        public String cPaymPstd;
        public String sBriefDsc;
        public String xBarCodex;
        public String sImagesxx;
        public String xBrandNme;
        public String xModelNme;
        public String xColorNme;
        public String xCategrNm;
    }

    class DetailedOrderHistory{
        public String sTransNox;
        public String dTransact;
        public String dExpected;
        public String sReferNox;
        public double nTranTotl;
        public double nDiscount;
        public double nFreightx;
        public double nProcPaym;
        public double nAmtPaidx;
        public String sTermCode;
        public String cTranStat;
        public String cPaymPstd;
        public String sWaybilNo;
        public String dWaybillx;
        public String dPickedUp;
        public String sBatchNox;

        public String sUserName;
        public String sAddressx;
        public String sMobileNo;
    }
}

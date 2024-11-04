package org.rmj.g3appdriver.GConnect.room.DataAccessObject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.GConnect.room.Entities.ERedeemItemInfo;

import java.util.List;

@Dao
public interface DRedeemItemInfo {

    @Insert()
    void insert(ERedeemItemInfo redeemItemInfo);

    @Insert()
    void insertBulkData(List<ERedeemItemInfo> redeemItemInfoList);

    @Update
    void update(ERedeemItemInfo redeemItemInfo);

    @Query("SELECT COUNT(*) FROM Redeem_Item")
    int GetRowsCountForID();

    @Query("SELECT sGCardNox FROM Gcard_App_Master WHERE cActvStat = '1'")
    String getCardNox();

    @Query("SELECT sCardNmbr FROM Gcard_App_Master WHERE cActvStat = '1'")
    String GetGCardNumber();

    @Query("SELECT sUserIDxx FROM Client_Profile_Info")
    String GetUserID();

    @Query("SELECT (SELECT sAvlPoint FROM GCard_App_Master WHERE cActvStat = '1') - " +
            "(SELECT nPointsxx FROM Redeem_Item WHERE sGCardNox = " +
            "(SELECT sGCardNox FROM GCard_App_Master WHERE cActvStat = '1')) AS RemainingPoints")
    double GetRemainingGcardPoints();

    @Query("SELECT sAvlPoint FROM Gcard_App_Master WHERE sCardNmbr =:CardNmbr")
    double getGCardTotPoints(String CardNmbr);

    @Query("SELECT SUM(nPointsxx) FROM redeem_item WHERE sGCardNox =:GCardNox AND cTranStat IN ('0', '1')")
    double getOrderPoints(String GCardNox);

    @Query("DELETE FROM redeem_item WHERE sTransNox = :fsPromoId")
    void removeItemFromCart(String fsPromoId);

    @Query("SELECT * FROM Redeem_Item WHERE sTransNox =:TransNox AND sPromoIDx=:PromoIDx")
    List<ERedeemItemInfo> getRedeemableIfExist(String TransNox, String PromoIDx);

    @Query("UPDATE Redeem_Item SET " +
            "nItemQtyx =:ItemQty " +
            "WHERE sTransNox=:TransNox AND sTransNox =:PromoIDx")
    void UpdateExistingItemOnCart(String TransNox, String PromoIDx, int ItemQty);
}

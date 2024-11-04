package org.rmj.g3appdriver.GConnect.room.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.GConnect.room.Entities.EItemCart;

import java.util.List;

@Dao
public interface DItemCart {

    @Insert
    void SaveItemInfo(EItemCart foVal);

    @Update
    void UpdateItemInfo(EItemCart foVal);

    @Query("SELECT * FROM MarketPlace_Cart WHERE sListIDxx =:ListngID")
    EItemCart GetItemCart(String ListngID);

    @Query("SELECT COUNT(*) FROM MarketPlace_Cart WHERE sUserIDxx = (SELECT sUserIDxx FROM Client_Profile_Info)")
    LiveData<Integer> GetMartketplaceCartItemCount();

    @Query("SELECT ((SELECT COUNT(*) FROM MarketPlace_Cart) + (SELECT COUNT(*) FROM Redeem_Item WHERE sBatchNox IS NULL)) AS CartItemCount")
    LiveData<Integer> GetCartItemCount();

    @Query("SELECT COUNT(*) FROM MarketPlace_Cart")
    int CheckIfCartHasRecord();

    @Query("SELECT dTimeStmp FROM MarketPlace_Cart ORDER BY dTimeStmp DESC LIMIT 1")
    String GetLatestCartTimeStamp();

    @Query("SELECT * FROM MarketPlace_Cart WHERE sListIDxx=:fsListID")
    EItemCart CheckIFItemExist(String fsListID);

    @Query("UPDATE MarketPlace_Cart SET nQuantity = :fnQty WHERE sListIDxx =:fsListID")
    void UpdateItem(String fsListID, int fnQty);

    @Query("DELETE FROM MarketPlace_Cart WHERE sListIDxx=:fsListID")
    void DeleteCartItem(String fsListID);

    @Query("UPDATE MarketPlace_Cart SET cCheckOut = '1' WHERE sListIDxx =:fsListID")
    void UpdateForCheckOut(String fsListID);

    @Query("UPDATE MarketPlace_Cart SET cCheckOut = '0' WHERE sListIDxx =:fsListID")
    void RemoveForCheckOut(String fsListID);

    @Query("SELECT COUNT(*) FROM MarketPlace_Cart WHERE cCheckOut ='1'")
    int CheckCartItemsForOrder();

    @Query("UPDATE Product_Inventory " +
            "SET nTotalQty =:nTotalQty, " +
            "nQtyOnHnd =:nQtyOnHnd, " +
            "nResvOrdr =:nResvOrdr, " +
            "nSoldQtyx =:nSoldQtyx, " +
            "nUnitPrce =:nUnitPrce " +
            "WHERE sListngID=:fsLstID")
    void UpdateProdcutQuantity(String fsLstID, String nTotalQty, String nQtyOnHnd,
                               String nResvOrdr, String nSoldQtyx, String nUnitPrce);

    class oMarketplaceCartItem{
        public String sListIDxx;
        public String nQuantity;
        public String xModelNme;
        public String xDescript;
        public String sImagesxx;
        public String nUnitPrce;
        public String cCheckOut;
    }
}

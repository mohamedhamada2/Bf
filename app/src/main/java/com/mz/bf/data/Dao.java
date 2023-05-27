package com.mz.bf.data;


import com.mz.bf.addbill.FatoraDetail;

import java.util.List;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@androidx.room.Dao
public interface Dao {

    @Insert
    void Addbill(FatoraDetail orderItemList);

    @Query("select * from bill where fatora_type =:id1")
    List<FatoraDetail> getallbills(String id1);

    @Query("DELETE FROM bill")
    void deleteAllproduct();

    @Query("delete from bill where product_id_fk =:id1")
    void DeleteProduct(int id1);
    @Update
    void editproduct(FatoraDetail fatoraDetail);
}

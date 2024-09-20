package com.klavs.roomornekproje.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.klavs.roomornekproje.data.entity.KisiModel

@Dao
interface KisilerDao {

    @Query("SELECT * FROM kisiler")
    suspend fun getKisiler():List<KisiModel>

    @Query("SELECT * FROM kisiler WHERE kisi_ad like '%' || :aramaKelimesi || '%'")
    suspend fun araKisi(aramaKelimesi:String): List<KisiModel>

    @Insert
    suspend fun addKisi(kisi:KisiModel)

    @Delete
    suspend fun silKisi(kisi:KisiModel)

    @Update
    suspend fun guncelleKisi(kisi: KisiModel)
}
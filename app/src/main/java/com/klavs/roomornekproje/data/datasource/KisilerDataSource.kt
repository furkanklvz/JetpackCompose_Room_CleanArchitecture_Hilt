package com.klavs.roomornekproje.data.datasource

import com.klavs.roomornekproje.data.entity.KisiModel
import com.klavs.roomornekproje.room.KisilerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KisilerDataSource(val kdao: KisilerDao) {

    suspend fun KisileriAl():List<KisiModel> = withContext(Dispatchers.IO){
        return@withContext kdao.getKisiler()
    }

    suspend fun KisiAra(aramaKelimesi:String):List<KisiModel> = withContext(Dispatchers.IO){
        return@withContext kdao.araKisi(aramaKelimesi)
    }

    suspend fun KisiKaydet(kisi_ad: String, kisi_tel: String) {
        val yeniKisi = KisiModel(0, kisi_ad, kisi_tel)
        kdao.addKisi(yeniKisi)
    }

    suspend fun KisiSil(kisi_id: Int){
        val silinenKisi = KisiModel(kisi_id,"","")
        kdao.silKisi(silinenKisi)
    }

    suspend fun KisiGuncelle(kisi: KisiModel){
        kdao.guncelleKisi(kisi)
    }


}
package com.klavs.roomornekproje.data.repo

import com.klavs.roomornekproje.data.datasource.KisilerDataSource
import com.klavs.roomornekproje.data.entity.KisiModel

class KisilerRepostory(val kds: KisilerDataSource) {

    suspend fun KisileriAl():List<KisiModel> = kds.KisileriAl()

    suspend fun KisiAra(aramaKelimesi:String):List<KisiModel> = kds.KisiAra(aramaKelimesi)

    suspend fun KisiKaydet(kisi_ad: String, kisi_tel: String) = kds.KisiKaydet(kisi_ad, kisi_tel)

    suspend fun KisiSil(kisi_id: Int) = kds.KisiSil(kisi_id)

    suspend fun KisiGuncelle(kisi: KisiModel) = kds.KisiGuncelle(kisi)

}
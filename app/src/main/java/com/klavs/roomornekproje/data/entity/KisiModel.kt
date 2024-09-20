package com.klavs.roomornekproje.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "kisiler")
data class KisiModel(

    @PrimaryKey(autoGenerate = true)
    val kisi_id: Int,
    val kisi_ad: String,
    val kisi_tel: String
)

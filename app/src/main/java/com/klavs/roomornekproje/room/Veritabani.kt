package com.klavs.roomornekproje.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.klavs.roomornekproje.data.entity.KisiModel

@Database(entities = [KisiModel::class], version = 1)
abstract class Veritabani:RoomDatabase() {
    abstract fun getKisilerDao(): KisilerDao
}
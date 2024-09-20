package com.klavs.roomornekproje.di

import android.content.Context
import androidx.room.Room
import com.klavs.roomornekproje.data.datasource.KisilerDataSource
import com.klavs.roomornekproje.data.repo.KisilerRepostory
import com.klavs.roomornekproje.room.KisilerDao
import com.klavs.roomornekproje.room.Veritabani
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun ProvideKisilerRepostory(kds: KisilerDataSource): KisilerRepostory {
        return KisilerRepostory(kds)
    }

    @Provides
    @Singleton
    fun ProvideKisilerDataSource(dao: KisilerDao): KisilerDataSource {
        return KisilerDataSource(dao)
    }

    @Provides
    @Singleton
    fun ProvideKisilerDao(@ApplicationContext context: Context): KisilerDao {
        val db = Room.databaseBuilder(context, Veritabani::class.java, "kisiler")
            .createFromAsset("kisiler.sqlite").build()
        return db.getKisilerDao()
    }

}
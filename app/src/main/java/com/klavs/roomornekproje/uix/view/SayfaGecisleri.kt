package com.klavs.roomornekproje.uix.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.klavs.roomornekproje.data.entity.KisiModel

@Composable
fun SayfaGecisleri(paddingValues: PaddingValues) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "ana_sayfa", modifier = Modifier.padding(paddingValues)) {
        composable("ana_sayfa") { AnaSayfa(navController = navController) }
        composable("kisi_kayit") { KisiKayit(navController = navController) }
        composable("kisi_detay/{kisi}", arguments = listOf(navArgument("kisi"){
            type = NavType.StringType
        })){
            val json = it.arguments?.getString("kisi")
            val kisi = Gson().fromJson(json, KisiModel::class.java)
            KisiDetay(navController = navController, kisiModel = kisi)
        }
    }
}
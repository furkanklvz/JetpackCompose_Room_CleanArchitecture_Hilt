package com.klavs.roomornekproje.uix.view

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.klavs.roomornekproje.data.entity.KisiModel
import com.klavs.roomornekproje.ui.theme.Purple80
import com.klavs.roomornekproje.uix.viewmodel.KisiDetayViewModel
import com.klavs.roomornekproje.uix.viewmodel.KisiKayitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KisiDetay(navController: NavHostController, kisiModel: KisiModel) {
    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "geri")
            }
        }, title = { Text(text = kisiModel.kisi_ad) })
    }) { paddingValues ->
        Content(paddingValues = paddingValues, navController = navController, kisiModel = kisiModel)
    }
}

@Composable

private fun Content(
    paddingValues: PaddingValues,
    navController: NavHostController,
    kisiModel: KisiModel
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val viewModel: KisiDetayViewModel = hiltViewModel()

    Column(
        Modifier
            .fillMaxWidth()
            .height(screenHeight / 2)
            .padding(top = paddingValues.calculateTopPadding()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val tfKisiAd = remember { mutableStateOf(kisiModel.kisi_ad) }
        val tfKisiTel = remember { mutableStateOf(kisiModel.kisi_tel) }

        KisiDetayTextField(label = "Ad", value = tfKisiAd.value) {
            tfKisiAd.value = it
        }
        KisiDetayTextField(label = "Telefon", value = tfKisiTel.value) {
            tfKisiTel.value = it
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple80,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                viewModel.KisiGuncelle(
                    KisiModel(
                        kisi_id = kisiModel.kisi_id,
                        kisi_ad = tfKisiAd.value,
                        kisi_tel = tfKisiTel.value
                    )
                )
            }) {
            Text(text = "Güncelle")
        }
    }
    if (viewModel.islemTamamlandi.value){
        if (!viewModel.hataOlustu.value){
            Toast.makeText(LocalContext.current, "İşlem Başarılı", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(LocalContext.current, "Hata: "+viewModel.hataMesaji.value, Toast.LENGTH_SHORT).show()

        }
        viewModel.islemTamamlandi.value = false
        viewModel.hataOlustu.value = false
        navController.popBackStack()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KisiDetayTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            .fillMaxWidth(0.8f),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        label = { Text(text = label) },
        singleLine = true,
        value = value,
        onValueChange = onValueChange
    )
}
package com.klavs.roomornekproje.uix.view

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.klavs.roomornekproje.ui.theme.Purple80
import com.klavs.roomornekproje.uix.viewmodel.KisiKayitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KisiKayit(navController: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Yeni Kişi Kaydet") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "geri")
                }
            })
    }) { paddingValues ->
        Content(paddingValues = paddingValues, navController = navController)
    }
}


@Composable
private fun Content(paddingValues: PaddingValues,navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val viewModel: KisiKayitViewModel = hiltViewModel()

    Column(
        Modifier
            .fillMaxWidth()
            .height(screenHeight / 2)
            .padding(top = paddingValues.calculateTopPadding()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val tfKisiAd = remember { mutableStateOf("") }
        val tfKisiTel = remember { mutableStateOf("") }

        KisiKayitTextField(label = "Ad", value = tfKisiAd.value) {
            tfKisiAd.value = it
        }
        KisiKayitTextField(label = "Telefon", value = tfKisiTel.value) {
            tfKisiTel.value = it
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple80,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            onClick = { viewModel.KisiEkle(tfKisiAd.value, tfKisiTel.value) }) {
            Text(text = "Ekle")
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
private fun KisiKayitTextField(label: String,value: String, onValueChange: (String) -> Unit){
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
        onValueChange = onValueChange)
}


@Preview
@Composable
fun KisiKayitPreview() {
    //Content(paddingValues = PaddingValues(0.dp))
}
package com.klavs.roomornekproje.uix.view


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.klavs.roomornekproje.data.entity.KisiModel
import com.klavs.roomornekproje.uix.viewmodel.AnaSayfaViewModel
import com.klavs.roomornekproje.uix.viewmodel.KisilerStatement
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaSayfa(
    navController: NavHostController,
    viewModel: AnaSayfaViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.KisileriYukle()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val aramaYapiliyor = remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate("kisi_kayit") }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "ekle")
        }
    }, topBar = {
        TopAppBar(title = {
            if (aramaYapiliyor.value) {
                val aramaKelimesi = remember { mutableStateOf("") }
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    value = aramaKelimesi.value,
                    onValueChange = {
                        aramaKelimesi.value = it
                        viewModel.KisiAra(aramaKelimesi.value)
                    },
                    placeholder = {
                        Text(
                            text = "Ara.."
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "ara"
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent
                    )
                )
            } else {
                Text(text = "Anasayfa")
            }


        },
            actions = {
                if (aramaYapiliyor.value) {
                    IconButton(onClick = {
                        aramaYapiliyor.value = false
                        viewModel.KisileriYukle()
                    }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "iptal")
                    }
                } else {
                    IconButton(onClick = { aramaYapiliyor.value = true }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "ara")
                    }
                }
            })
    },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        Content(
            paddingValues = it,
            snackbarHostState = snackbarHostState,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: AnaSayfaViewModel = hiltViewModel()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
    ) {

        Spacer(modifier = Modifier.padding(10.dp))
        when (val kisilerState = viewModel.kisilerState.value) {
            KisilerStatement.Empty -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Kişiler Boş", fontSize = 30.sp)
                }
            }

            is KisilerStatement.Error -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Hata meydana geldi: " + kisilerState.message, fontSize = 20.sp)
                }
            }

            KisilerStatement.Loading -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            KisilerStatement.Success -> {
                LazyColumn(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(viewModel.kisiler) {
                        KisiCard(
                            kisiModel = it,
                            snackbarHostState = snackbarHostState,
                            navController = navController
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun KisiCard(
    kisiModel: KisiModel,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: AnaSayfaViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    Card(
        Modifier
            .padding(5.dp)
            .height(70.dp)
            .clickable {
                val kisiJson = Gson().toJson(kisiModel)
                navController.navigate("kisi_detay/${kisiJson}")
            }
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = kisiModel.kisi_id.toString(), fontSize = 35.sp)
                Spacer(modifier = Modifier.padding(10.dp))
                Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = kisiModel.kisi_ad, fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    Text(text = kisiModel.kisi_tel, fontSize = 19.sp)
                    Log.e("kisi tel", kisiModel.kisi_tel)
                }
            }

            IconButton(onClick = {
                scope.launch {
                    val sb = snackbarHostState.showSnackbar(
                        "${kisiModel.kisi_ad} silinsin mi?",
                        actionLabel = "Evet",
                        withDismissAction = true
                    )
                    if (sb == SnackbarResult.ActionPerformed) {
                        viewModel.KisiyiSil(kisiModel.kisi_id)
                    }
                }

            }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "delete")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AnaSayfaPreview() {
}
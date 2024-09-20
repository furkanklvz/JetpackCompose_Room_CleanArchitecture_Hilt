package com.klavs.roomornekproje.uix.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.roomornekproje.data.entity.KisiModel
import com.klavs.roomornekproje.data.repo.KisilerRepostory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class KisilerStatement(val message:String? = null){
    object Success:KisilerStatement()
    class Error(message: String):KisilerStatement(message)
    object Loading:KisilerStatement()
    object Empty:KisilerStatement()
}

@HiltViewModel
class AnaSayfaViewModel @Inject constructor(var krepo: KisilerRepostory) :ViewModel() {

    val kisilerState : MutableState<KisilerStatement> = mutableStateOf(KisilerStatement.Loading)
    val kisiler = mutableStateListOf<KisiModel>()



    fun KisileriYukle(){
        kisilerState.value = KisilerStatement.Loading
        viewModelScope.launch(Dispatchers.Main) {
            try {
                kisiler.clear()
                kisiler.addAll(krepo.KisileriAl())
                kisilerState.value = KisilerStatement.Success
            }catch (e:Exception){
                kisilerState.value = KisilerStatement.Error(e.localizedMessage?:"Bilinmeyen bir hata oluştu")

            }
        }
    }

    fun KisiyiSil(kisi_id:Int){
        viewModelScope.launch {
            krepo.KisiSil(kisi_id)
            KisileriYukle()
        }
    }

    fun KisiAra(aramaKelimesi:String){
        viewModelScope.launch(Dispatchers.IO) {
            kisiler.clear()
            val arananKisiler = krepo.KisiAra(aramaKelimesi)
            kisiler.addAll(arananKisiler)
        }
    }


}
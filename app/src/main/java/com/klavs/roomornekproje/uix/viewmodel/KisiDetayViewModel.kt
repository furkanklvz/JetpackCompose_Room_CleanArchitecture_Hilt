package com.klavs.roomornekproje.uix.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.roomornekproje.data.entity.KisiModel
import com.klavs.roomornekproje.data.repo.KisilerRepostory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KisiDetayViewModel @Inject constructor(var krepo: KisilerRepostory) : ViewModel() {

    val hataOlustu = mutableStateOf(false)
    val islemTamamlandi = mutableStateOf(false)
    val hataMesaji = mutableStateOf("")

    fun KisiGuncelle(kisi: KisiModel) {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                krepo.KisiGuncelle(kisi).also {
                    islemTamamlandi.value = true
                }
            }
        } catch (e: Exception) {
            hataOlustu.value = true
            islemTamamlandi.value = true
            hataMesaji.value = e.localizedMessage ?: "Bilinmeyen bir hata oluştu"
        }

    }
}
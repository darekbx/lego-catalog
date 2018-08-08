package com.legocatalog.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository): ViewModel() {

    val result: MutableLiveData<String> = MutableLiveData()

    fun synchronize() {
        repository.synchronizeRemoteItems { message -> result.postValue(message) }
    }
}
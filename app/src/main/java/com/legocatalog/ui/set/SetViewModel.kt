package com.legocatalog.ui.set

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.legocatalog.model.LegoSet
import com.legocatalog.repository.Repository
import javax.inject.Inject

class SetViewModel @Inject constructor(val repository: Repository): ViewModel() {

    val set = MutableLiveData<LegoSet>()
    val message = MutableLiveData<String>()

    fun loadSet(number: String) {
        repository.loadItem(number,
                { message.value = it },
                { set.postValue(it) })
    }
}
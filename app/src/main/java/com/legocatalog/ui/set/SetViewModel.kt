package com.legocatalog.ui.set

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import com.legocatalog.ui.model.SetInfo
import javax.inject.Inject

class SetViewModel @Inject constructor(val repository: Repository): ViewModel() {

    val set = MutableLiveData<SetInfo>()
    val message = MutableLiveData<String>()

    fun loadSet(number: String) {
        repository.loadItem(number,
                { message.value = it },
                { set.postValue(it) })
    }
}
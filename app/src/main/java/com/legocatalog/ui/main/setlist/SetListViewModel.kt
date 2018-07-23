package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.legocatalog.model.LegoSet
import com.legocatalog.repository.Repository
import javax.inject.Inject

class SetListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    val sets = MutableLiveData<List<LegoSet>>()
    val message = MutableLiveData<String>()

    fun loadSets(theme: LegoSet.Theme) {
        repository.fetchItems(theme,
                { message.value = it },
                { sets.postValue(it) })
    }
}
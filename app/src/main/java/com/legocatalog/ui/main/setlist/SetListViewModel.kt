package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import com.legocatalog.ui.model.SetInfo
import javax.inject.Inject

class SetListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    val sets = MutableLiveData<List<SetInfo>>()
    val message = MutableLiveData<String>()

    fun loadSets(theme: SetInfo.Theme) {
        repository.fetchItems(theme,
                { message.value = it },
                { sets.postValue(it) })
    }
}
package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import com.legocatalog.extensions.toSetInfo
import com.legocatalog.ui.model.SetInfo
import javax.inject.Inject

class SetListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    var sets: LiveData<List<SetInfo>>? = null
    val message = MutableLiveData<String>()

    fun loadSets(theme: SetInfo.Theme) {
        sets = Transformations.map(repository.fetchSets(theme.ordinal), { setEntities ->
            setEntities.map { it.toSetInfo() }
        })
        /*repository.fetchItems(theme,
                { message.value = it },
                { sets.postValue(it) })*/
    }
}
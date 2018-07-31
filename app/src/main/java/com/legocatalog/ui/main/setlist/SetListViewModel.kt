package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import com.legocatalog.extensions.toSetInfo
import com.legocatalog.ui.model.SetInfo
import javax.inject.Inject

class SetListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    lateinit var sets: LiveData<List<SetInfo>>

    fun loadSets(theme: SetInfo.Theme) {
        sets = Transformations.map(
                repository.fetchSets(theme.ordinal),
                { setEntities -> setEntities.map { it.toSetInfo() } })
    }
}
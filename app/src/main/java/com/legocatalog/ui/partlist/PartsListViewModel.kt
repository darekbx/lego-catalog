package com.legocatalog.ui.partlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.local.PartEntity
import com.legocatalog.data.repository.Repository
import javax.inject.Inject

class PartsListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    var parts: LiveData<List<PartEntity>>? = null

    fun loadParts(setNumber: String) {
        parts = repository.fetchParts(setNumber)
    }
}
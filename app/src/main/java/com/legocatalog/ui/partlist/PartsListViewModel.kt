package com.legocatalog.ui.partlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.local.PartEntity
import com.legocatalog.data.repository.Repository
import com.legocatalog.ui.model.Part
import javax.inject.Inject

class PartsListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    var parts: LiveData<List<Part>>? = null

    fun loadParts(setNumber: String) {
        parts = Transformations
                .map(repository.fetchParts(setNumber), { partEntities ->
                    partEntities.map { PartEntity.mapToPart(it) }
                })
    }
}
package com.legocatalog.ui.partlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import com.legocatalog.extensions.toPart
import com.legocatalog.ui.model.Part
import javax.inject.Inject

class PartsListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    var parts: LiveData<List<Part>>? = null

    fun loadParts(setId: Int) {
        parts = Transformations
                .map(repository.fetchParts(setId), { partEntities ->
                    partEntities.map { part -> part.toPart() }
                })
    }

    fun loadParts() {
        parts = Transformations
                .map(repository.fetchParts(), { partEntities ->
                    partEntities.map { part -> part.toPart() }
                })
    }
}
package com.legocatalog.ui.filters

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations.*
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import javax.inject.Inject

class FiltersViewModel @Inject constructor(val repository: Repository): ViewModel() {

    lateinit var names: LiveData<List<String>>
    lateinit var elementIds: LiveData<List<String>>
    lateinit var colors: LiveData<List<String>>

    fun loadFilters() {
        names = map(repository.fetchParts(),
                { setEntities -> setEntities.map { it.partEntity?.name ?: "" } })

        elementIds = map(repository.fetchParts(),
                { setEntities -> setEntities.map { it.partEntity?.elementId ?: "" } })

        colors = repository.fetchColors()
    }
}
package com.legocatalog.ui.set

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.legocatalog.data.repository.Repository
import com.legocatalog.extensions.toSetInfo
import com.legocatalog.ui.model.SetInfo
import javax.inject.Inject

class SetViewModel @Inject constructor(val repository: Repository): ViewModel() {

    var set: LiveData<SetInfo>? = null
    val message = MutableLiveData<String>()

    fun loadSet(setId: Int) {
        set = Transformations
                .map(repository.fetchSet(setId), { setEntity -> setEntity.toSetInfo() })
    }
}
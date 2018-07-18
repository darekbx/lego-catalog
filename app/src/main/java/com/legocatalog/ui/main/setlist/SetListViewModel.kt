package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.legocatalog.firebase.FirebaseDatabase
import com.legocatalog.model.LegoSet
import javax.inject.Inject

class SetListViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase): ViewModel() {

    val sets = MutableLiveData<LegoSet>()

    fun loadSets() {

    }
}
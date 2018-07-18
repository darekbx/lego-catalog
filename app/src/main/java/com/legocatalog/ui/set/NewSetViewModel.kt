package com.legocatalog.ui.set

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.legocatalog.firebase.FirebaseDatabase
import com.legocatalog.model.LegoSet
import com.legocatalog.remote.rebrickable.SetInfoWorker
import javax.inject.Inject

class NewSetViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase): ViewModel() {

    var result: MutableLiveData<Pair<Boolean,String>> = MutableLiveData()
    var workStatus: LiveData<WorkStatus>? = null

    fun discoverSetInfo(setNumber: String) {
        val data = Data.Builder()
                .putString(SetInfoWorker.NUMBER_KEY, setNumber)
                .build()
        val work = OneTimeWorkRequestBuilder<SetInfoWorker>()
                .setInputData(data)
                .build()
        WorkManager.getInstance()?.let {
            with (it) {
                enqueue(work)
                workStatus = getStatusById(work.id)
            }
        }
    }

    fun saveSet(set: LegoSet) {
        firebaseDatabase.sets.child(set.number).setValue(set)
                .addOnSuccessListener { result.value = (true to "") }
                .addOnFailureListener { error -> result.value = (false to error.toString()) }
    }
}
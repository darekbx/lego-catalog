package com.legocatalog.ui.newset

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.legocatalog.data.repository.Repository
import com.legocatalog.data.repository.workers.SetInfoWorker
import com.legocatalog.ui.model.SetInfo
import javax.inject.Inject

class NewSetViewModel @Inject constructor(val repository: Repository): ViewModel() {

    var result: MutableLiveData<Pair<Boolean,String>> = MutableLiveData()
    var workStatus: LiveData<WorkStatus>? = null

    SetInforWorker will save set when fetched, not when saved to firebase

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

    fun saveSet(set: SetInfo) {
        repository.saveItem(set)
                .addOnSuccessListener { result.value = (true to "") }
                .addOnFailureListener { error -> result.value = (false to error.toString()) }
    }
}
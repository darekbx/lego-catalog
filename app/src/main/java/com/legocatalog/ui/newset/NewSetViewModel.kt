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
import com.legocatalog.data.repository.workers.SetSaveWorker
import com.legocatalog.ui.model.SetInfo
import javax.inject.Inject

class NewSetViewModel @Inject constructor(val repository: Repository): ViewModel() {

    val result: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    lateinit var discoverWorkStatus: LiveData<WorkStatus>
    lateinit var saveWorkStatus: LiveData<WorkStatus>

    fun discoverSetInfo(setNumber: String) {
        val data = Data.Builder()
                .putString(SetInfoWorker.NUMBER_KEY, setNumber)
                .build()
        val work = OneTimeWorkRequestBuilder<SetInfoWorker>()
                .setInputData(data)
                .build()
        WorkManager.getInstance()?.run {
            enqueue(work)
            discoverWorkStatus = getStatusById(work.id)
        }
    }

    fun saveSetRemoetly(set: SetInfo) {
        repository.saveItemRemoetly(set)
                .addOnSuccessListener { result.value = (true to "") }
                .addOnFailureListener { error -> result.value = (false to error.toString()) }
    }

    fun saveSetLocally(set: SetInfo) {
        val data = Data.Builder()
                .putString(SetSaveWorker.NUMBER_KEY, set.number)
                .putInt(SetSaveWorker.THEME_KEY, set.themeId)
                .build()
        val work = OneTimeWorkRequestBuilder<SetSaveWorker>()
                .setInputData(data)
                .build()
        WorkManager.getInstance()?.run {
            enqueue(work)
            saveWorkStatus = getStatusById(work.id)
        }
    }
}
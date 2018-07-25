package com.legocatalog.ui.partlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.legocatalog.data.repository.Repository
import com.legocatalog.data.repository.workers.PartsWorker
import javax.inject.Inject

class PartsListViewModel @Inject constructor(val repository: Repository): ViewModel() {

    var workStatus: LiveData<WorkStatus>? = null

    fun loadSetParts(setNumber: String) {
        val data = Data.Builder()
                .putString(PartsWorker.NUMBER_KEY, setNumber)
                .build()
        val work = OneTimeWorkRequestBuilder<PartsWorker>()
                .setInputData(data)
                .build()
        WorkManager.getInstance()?.let {
            with (it) {
                enqueue(work)
                workStatus = getStatusById(work.id)
            }
        }
    }
}
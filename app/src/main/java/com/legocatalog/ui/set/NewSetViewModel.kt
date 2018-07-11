package com.legocatalog.ui.set

import android.arch.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.legocatalog.remote.rebrickable.SetInfoWorker
import java.util.*
import javax.inject.Inject

class NewSetViewModel @Inject constructor(): ViewModel() {

    fun discoverSetInfo(setNumber: String): UUID {
        val data = Data.Builder()
                .putString(SetInfoWorker.NUMBER_KEY, setNumber)
                .build()
        val work = OneTimeWorkRequestBuilder<SetInfoWorker>()
                .setInputData(data)
                .build()
        WorkManager.getInstance()?.enqueue(work)
        return work.id
    }
}
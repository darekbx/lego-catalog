package com.legocatalog.ui.set

import android.arch.lifecycle.ViewModel
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.database.FirebaseDatabase
import com.legocatalog.model.LegoSet
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

    fun saveSet(set: LegoSet) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("sets").child(set.number).setValue(set)
                .addOnSuccessListener { Log.v("-----------", "Success") }
                .addOnFailureListener { a->a.printStackTrace()  }
    }
}
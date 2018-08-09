package com.legocatalog.data.repository

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.legocatalog.data.local.LegoDatabase
import com.legocatalog.data.local.SetEntity
import com.legocatalog.data.local.SetXPartEntity
import com.legocatalog.data.remote.firebase.FirebaseDatabase
import com.legocatalog.data.remote.model.LegoPartsWrapper
import com.legocatalog.data.repository.workers.SetSaveWorker
import com.legocatalog.extensions.toPartEntity
import com.legocatalog.ui.model.SetInfo
import java.util.*

class Repository(val firebaseDatabase: FirebaseDatabase, val legoDatabase: LegoDatabase) {

    fun fetchParts() = legoDatabase.getDao().fetchParts()
    fun fetchParts(setId: Int) = legoDatabase.getDao().fetchParts(setId)
    fun fetchSet(setId: Int) = legoDatabase.getDao().fetchSet(setId)
    fun fetchSets(themeId: Int) = legoDatabase.getDao().fetchSets(themeId)
    fun fetchColors() = legoDatabase.getDao().fetchColors()
    fun addSet(setEntity: SetEntity) = legoDatabase.getDao().add(setEntity)

    fun deleteSet(setId: Int) {
        with(legoDatabase.getDao()) {
            deleteSet(setId)
            deleteSetXPart(setId)
        }
        firebaseDatabase
                .sets
                .orderByKey()
                .addValueEventListener(object : ValueEventListener {

                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach { child ->
                            val setInfo = childToLegoSet(child)
                            if (setInfo.id == setId) {
                                child.ref.removeValue()
                            }
                        }
                    }
                })
    }

    fun addParts(setId: Int, wrapper: LegoPartsWrapper) {
        with(legoDatabase) {
            beginTransaction()
            val dao = getDao()
            try {
                wrapper.results?.forEach {
                    val partExists = dao.countByElementId(it.elementId) > 0
                    if (!partExists) {
                        val partEntity = it.toPartEntity()
                        dao.add(partEntity)
                    }
                    dao.add(SetXPartEntity(null, setId, it.elementId, it.quantity))
                }
                setTransactionSuccessful()
            } finally {
                endTransaction()
            }
        }
    }

    fun saveItemRemoetly(setInfo: SetInfo) =
            firebaseDatabase
                    .sets
                    .child(UUID.randomUUID().toString())
                    .setValue(setInfo)

    fun synchronizeRemoteItems(onMessage: (message: String) -> Unit) {
        firebaseDatabase
                .sets
                .orderByKey()
                .addValueEventListener(object : ValueEventListener {

                    override fun onCancelled(error: DatabaseError) {
                        onMessage(error.message)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        verifySets(dataSnapshot)
                    }
                })
    }

    private fun verifySets(dataSnapshot: DataSnapshot) {
        dataSnapshot.children.forEach { child ->
            val setInfo = childToLegoSet(child)
            val setEntity = legoDatabase.getDao().fetchSetSynchronously(setInfo.number)
            if (setEntity == null) {
                saveSetLocally(setInfo)
            }
        }
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
        }
    }

    private fun childToLegoSet(child: DataSnapshot) =
            child.getValue(SetInfo::class.java) as SetInfo
}
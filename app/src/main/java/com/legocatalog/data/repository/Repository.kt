package com.legocatalog.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.legocatalog.data.local.LegoDatabase
import com.legocatalog.data.remote.firebase.FirebaseDatabase
import com.legocatalog.ui.model.SetInfo
import java.util.*

class Repository(val firebaseDatabase: FirebaseDatabase, val legoDatabase: LegoDatabase) {

    fun fetchParts(setNumber: String) = legoDatabase.getDao().fetch(setNumber)

    fun fetchItems(theme: SetInfo.Theme,
                   onMessage: (message: String) -> Unit,
                   onResult: (list: List<SetInfo>) -> Unit) =
            firebaseDatabase
                    .sets
                    .orderByKey()
                    .addValueEventListener(object : ValueEventListener {

                        override fun onCancelled(error: DatabaseError) {
                            onMessage(error.message)
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            childrenToList(dataSnapshot, theme, onResult)
                        }
                    })

    fun saveItem(setInfo: SetInfo) =
            firebaseDatabase
                    .sets
                    .child(UUID.randomUUID().toString())
                    .setValue(setInfo)

    fun loadItem(number: String,
                 onMessage: (message: String) -> Unit,
                 onResult: (set: SetInfo) -> Unit) {
        firebaseDatabase
                .sets
                .orderByKey()
                .addValueEventListener(object : ValueEventListener {

                    override fun onCancelled(error: DatabaseError) {
                        onMessage(error.message)
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        findLegoSet(dataSnapshot, number, onResult)
                    }
                })
    }

    private fun findLegoSet(dataSnapshot: DataSnapshot, number: String, onResult: (set: SetInfo) -> Unit) {
        dataSnapshot.children.forEach { child ->
            val setInfo = childToLegoSet(child)
            if (setInfo.number == number) {
                onResult(setInfo)
                return@forEach
            }
        }
    }

    private fun childrenToList(dataSnapshot: DataSnapshot, theme: SetInfo.Theme, onResult: (list: List<SetInfo>) -> Unit) {
        with(mutableListOf<SetInfo>()) {
            dataSnapshot.children.forEach { child ->
                val setInfo = childToLegoSet(child)
                if (setInfo.themeId == theme.ordinal) {
                    add(setInfo)
                }
            }
            sortByDescending { it.year }
            onResult(this)
        }
    }

    private fun childToLegoSet(child: DataSnapshot) =
            child.getValue(SetInfo::class.java) as SetInfo
}
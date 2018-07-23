package com.legocatalog.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.legocatalog.model.LegoSet
import com.legocatalog.repository.firebase.FirebaseDatabase
import java.util.*

class Repository(val firebaseDatabase: FirebaseDatabase) {

    fun fetchItems(theme: LegoSet.Theme,
                   onMessage: (message: String) -> Unit,
                   onResult: (list: List<LegoSet>) -> Unit) =
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

    fun saveItem(legoSet: LegoSet) =
            firebaseDatabase
                    .sets
                    .child(UUID.randomUUID().toString())
                    .setValue(legoSet)

    fun loadItem(number: String,
                 onMessage: (message: String) -> Unit,
                 onResult: (set: LegoSet) -> Unit) {
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

    private fun findLegoSet(dataSnapshot: DataSnapshot, number: String, onResult: (set: LegoSet) -> Unit) {
        dataSnapshot.children.forEach { child ->
            val legoSet = childToLegoSet(child)
            if (legoSet.number == number) {
                onResult(legoSet)
                return@forEach
            }
        }
    }

    private fun childrenToList(dataSnapshot: DataSnapshot, theme: LegoSet.Theme, onResult: (list: List<LegoSet>) -> Unit) {
        with(mutableListOf<LegoSet>()) {
            dataSnapshot.children.forEach { child ->
                val legoSet = childToLegoSet(child)
                if (legoSet.themeId == theme.ordinal) {
                    add(legoSet)
                }
            }
            sortByDescending { it.year }
            onResult(this)
        }
    }

    private fun childToLegoSet(child: DataSnapshot) =
            child.getValue(LegoSet::class.java) as LegoSet
}
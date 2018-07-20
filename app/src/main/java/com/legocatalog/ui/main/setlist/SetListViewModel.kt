package com.legocatalog.ui.main.setlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.legocatalog.firebase.FirebaseDatabase
import com.legocatalog.model.LegoSet
import javax.inject.Inject

class SetListViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase): ViewModel() {

    val sets = MutableLiveData<List<LegoSet>>()
    val message = MutableLiveData<String>()

    fun loadSets(theme: LegoSet.Theme) {
        val query = firebaseDatabase.sets.orderByKey()
        query.addValueEventListener(object: ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                message.value = error.message
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                with(mutableListOf<LegoSet>()) {
                    dataSnapshot.children.forEach { child ->
                        val legoSet = child.getValue(LegoSet::class.java) as LegoSet
                        if (legoSet.themeId == theme.ordinal) {
                            add(legoSet)
                        }
                    }
                    sets.postValue(this)
                }
            }
        })
    }
}
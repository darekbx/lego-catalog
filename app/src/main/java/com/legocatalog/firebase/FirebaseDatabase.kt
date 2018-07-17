package com.legocatalog.firebase

import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabase {

    companion object {
        val SETS_KEY = "sets"
    }

    val reference by lazy { FirebaseDatabase.getInstance().reference }
    val sets by lazy { reference.child(SETS_KEY) }
}
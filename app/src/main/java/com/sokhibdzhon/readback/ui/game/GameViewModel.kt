package com.sokhibdzhon.readback.ui.game

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

private const val LEVELS = "levels"

class GameViewModel @Inject constructor(val firestoreDb: FirebaseFirestore) : ViewModel() {

    init {
        firestoreDb.collection(LEVELS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Timber.d("${document.id} => ${document.data.keys}")
                    Timber.d("${document.id} => ${document.metadata}")
                    Timber.d("${document.id} => ${document["1"]}")
                }
            }
            .addOnFailureListener { exception ->
                Timber.d("Error getting documents: $exception")
            }

    }

}
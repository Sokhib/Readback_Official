package com.sokhibdzhon.readback.data.network.custom

import com.google.firebase.firestore.FirebaseFirestore
import com.sokhibdzhon.readback.data.Resource
import com.sokhibdzhon.readback.data.model.Word
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject


/**     I ❤ Code:)
╔═══════════════════════════════════════╗
║  Created by Sokhibdzhon Saidmuratov   ║
╠═══════════════════════════════════════╣
║ sokhibsaid@gmail.com                  ║
╚═══════════════════════════════════════╝
 */

class CustomGameDataSourceImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    CustomGameDataSource {
    private companion object {
        private const val LEVELS = "levels"
        private const val CUSTOM = "custom"
        private const val WORDS = "words"
    }

    override fun getWords(): Flow<Resource<MutableList<Word>>> = callbackFlow {
        var words: MutableList<Word>? = null
        Timber.d("LOADING...")
        offer(Resource.loading())
        val eventDocument = firestore.collection(LEVELS).document(CUSTOM).collection(WORDS)
        val subscription =
            eventDocument.addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let { document ->
                    for (currentWord in document.documents) {
                        val options =
                            (currentWord.get("options") as MutableList<String>?)?.let {
                                currentWord.get("options") as MutableList<String>
                            } ?: mutableListOf("", "", "", "")
                        options.shuffle()
                        val correct =
                            (currentWord.get("correct") as String?)?.let { currentWord.get("correct") as String }
                                ?: ""
                        val word =
                            (currentWord.get("word") as String?)?.let { currentWord.get("word") as String }
                                ?: ""

                        // Adding words
                        words?.let {
                            words!!.add(Word(correct, options, word))
                        } ?: run {
                            words = mutableListOf(Word(correct, options, word))
                        }
                    }
                    if (!words.isNullOrEmpty()) {
                        words!!.shuffle()
                        offer(Resource.success(words))
                    } else {
                        offer(Resource.error("No Words or Check Internet Connection"))
                    }

                }
            }
        awaitClose { subscription.remove() }
    }

}
//    override fun getWords(): Flow<Resource<MutableList<Word>>> = flow {
//        Timber.d("Loading...")
//        emit(Resource.loading())
//        firestore.collection(LEVELS).document(CUSTOM).collection(WORDS)
//            .get()
//            .addOnSuccessListener { document ->
//                document?.let { doc ->
//                    for (currentWord in doc.documents) {
//                        // In case there are wrong inserts data in Firebase Firestore not to get crash.
//                        val options =
//                            (currentWord.get("options") as MutableList<String>?)?.let {
//                                currentWord.get("options") as MutableList<String>
//                            } ?: mutableListOf("", "", "", "")
//                        options.shuffle()
//                        val correct =
//                            (currentWord.get("correct") as String?)?.let { currentWord.get("correct") as String }
//                                ?: ""
//                        val word =
//                            (currentWord.get("word") as String?)?.let { currentWord.get("word") as String }
//                                ?: ""
//
//                        // Adding words
//                        words?.let {
//                            words!!.add(Word(correct, options, word))
//                        } ?: run {
//                            words = mutableListOf(Word(correct, options, word))
//                        }
//                    }
//                    if (!words.isNullOrEmpty()) {
//                        words!!.shuffle()
//                        Timber.d("shuffled")
//                        emit(Resource.success(words))
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Timber.d("Error getting documents: $exception")
//            }
//    }

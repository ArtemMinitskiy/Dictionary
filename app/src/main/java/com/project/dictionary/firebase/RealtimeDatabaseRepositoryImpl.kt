package com.project.dictionary.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.dictionary.utils.Constants.DICTIONARY_TABLE
import com.project.dictionary.utils.Constants.WORD_DEFINITION
import com.project.dictionary.utils.Constants.WORD_NAME
import com.project.dictionary.model.Word
import com.project.dictionary.utils.Constants.WORD_IMAGE
import com.project.dictionary.utils.Constants.WORD_LINK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeDatabaseRepositoryImpl @Inject constructor(
) : RealtimeDatabaseRepository {
    private val firebaseDatabase = Firebase.database
//    private var firebase = FirebaseDatabase.getInstance()

    @ExperimentalCoroutinesApi
    override fun fetchWords() = callbackFlow<Result<List<Word>>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listOfWords: ArrayList<Word> = arrayListOf()
                for (postSnapshot in dataSnapshot.children) {
//                    Log.i("mLogFirebase", "dataSnapshot ${dataSnapshot.child("Words").children}")
//                    Log.i("mLogFirebase", "postSnapshot $postSnapshot")
                    postSnapshot.children.forEach {
                        if (it.child(WORD_NAME).value != "") {
                            listOfWords.add(Word(it.key.toString(),
                                wordName = it.child(WORD_NAME).value.toString(),
                                wordDescription = it.child(WORD_DEFINITION).value.toString(),
                                wordLink = it.child(WORD_LINK).value.toString(),
                                wordImage = it.child(WORD_IMAGE).value.toString()))
//                            Log.e("mLogFirebase", "Key ${it.key} WordName ${it.child(WORD_NAME).value} - ${it.child(WORD_DEFINITION).value}")
                        }
                    }
                }
                this@callbackFlow.trySendBlocking(Result.success(listOfWords))
            }
        }
        firebaseDatabase.getReference(DICTIONARY_TABLE).orderByKey().addValueEventListener(postListener)

//        firebase.setPersistenceEnabled(true)

        awaitClose {
            firebaseDatabase.getReference(DICTIONARY_TABLE).removeEventListener(postListener)
        }

    }
}
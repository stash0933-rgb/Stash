package uk.ac.tees.mad.stash.data.Repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uk.ac.tees.mad.stash.domain.Repo
import uk.ac.tees.mad.stash.model.RecordModel
import uk.ac.tees.mad.stash.model.ResultState
import uk.ac.tees.mad.stash.model.UserData

class RepoImpl : Repo {

    override fun registeruserwithemailandpassword(
        userdata: UserData
    ): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val email = userdata.email
        val password = userdata.password

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            trySend(ResultState.error("Email or Password cannot be empty"))
            close()
            return@callbackFlow
        }

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    trySend(ResultState.Succes("Registration Successful"))
                } else {
                    trySend(
                        ResultState.error(
                            task.exception?.localizedMessage ?: "Unknown error"
                        )
                    )
                }
            }

        awaitClose { close() }
    }


    override fun loginuserwithemailandpassword(
        userdata: UserData
    ): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val email = userdata.email
        val password = userdata.password

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            trySend(ResultState.error("Email or Password cannot be empty"))
            close()
            return@callbackFlow
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    trySend(ResultState.Succes("Login Successful"))
                } else {
                    trySend(
                        ResultState.error(
                            task.exception?.localizedMessage ?: "Unknown error"
                        )
                    )
                }
            }

        awaitClose { close() }
    }
    override fun addRecord(record: RecordModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid


        val userDocRef =
            FirebaseFirestore.getInstance().collection("Records").document(currentUser.toString())

        userDocRef.set(record)
            .addOnSuccessListener {
                trySend(ResultState.Succes("Record added successfully"))
            }
            .addOnFailureListener { e ->
                trySend(ResultState.error("Failed to update record: ${e.localizedMessage}"))
            }

        awaitClose { close() }
    }
    override fun updateRecord(record: RecordModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val recordRef = FirebaseFirestore.getInstance()
            .collection("Records")
            .document(record.recordID)

        recordRef.update(
            mapOf(
                "title" to record.title,
                "value" to record.value
            )
        )
            .addOnSuccessListener {
                trySend(ResultState.Succes("Record updated successfully"))
            }
            .addOnFailureListener { e ->
                trySend(ResultState.error("Failed to update record: ${e.localizedMessage}"))
            }

        awaitClose { close() }
    }
    override fun deleteRecord(recordID: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val recordRef = FirebaseFirestore.getInstance()
            .collection("Records")
            .document(recordID)

        recordRef.delete()
            .addOnSuccessListener {
                trySend(ResultState.Succes("Record deleted successfully"))
            }
            .addOnFailureListener { e ->
                trySend(ResultState.error("Failed to delete record: ${e.localizedMessage}"))
            }

        awaitClose { close() }
    }
    override fun getAllRecords(): Flow<ResultState<List<RecordModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        val firestore = FirebaseFirestore.getInstance()

        val listener = firestore.collection("Records")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(ResultState.error(error.localizedMessage ?: "Unknown error"))
                    return@addSnapshotListener
                }

                val records = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(RecordModel::class.java)
                } ?: emptyList()

                trySend(ResultState.Succes(records))
            }

        awaitClose { listener.remove() }
    }
    override fun getRecordById(recordID: String): Flow<ResultState<RecordModel>> = callbackFlow {
        trySend(ResultState.Loading)

        val docRef = FirebaseFirestore.getInstance()
            .collection("Records")
            .document(recordID)

        val listener = docRef.addSnapshotListener { snapshot, error ->

            if (error != null) {
                trySend(ResultState.error(error.localizedMessage ?: "Unknown error"))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val record = snapshot.toObject(RecordModel::class.java)
                if (record != null) {
                    trySend(ResultState.Succes(record))
                } else {
                    trySend(ResultState.error("Record parsing failed"))
                }
            } else {
                trySend(ResultState.error("Record not found"))
            }
        }

        awaitClose { listener.remove() }
    }




    override fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }
}

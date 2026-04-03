package uk.ac.tees.mad.stash.data.Repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.ac.tees.mad.stash.StashApp
import uk.ac.tees.mad.stash.data.local.RecordEntity
import uk.ac.tees.mad.stash.domain.Repo
import uk.ac.tees.mad.stash.model.RecordModel
import uk.ac.tees.mad.stash.model.ResultState
import uk.ac.tees.mad.stash.model.UserData

class RepoImpl : Repo {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val recordDao = StashApp.database.recordDao()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    // ---------------- AUTH ----------------

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

        auth.createUserWithEmailAndPassword(email, password)
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

        auth.signInWithEmailAndPassword(email, password)
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

    // ---------------- ADD RECORD ----------------

    override fun addRecord(record: RecordModel): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val uid = auth.currentUser?.uid ?: run {
            trySend(ResultState.error("User not logged in"))
            close()
            return@callbackFlow
        }

        val docRef = firestore
            .collection("Users")
            .document(uid)
            .collection("Records")
            .document()

        val newRecord = record.copy(recordID = docRef.id)

        docRef.set(newRecord)
            .addOnSuccessListener {

                // 🔥 Save to Room
                ioScope.launch {
                    recordDao.insert(
                        RecordEntity(
                            recordID = newRecord.recordID,
                            title = newRecord.title,
                            value = newRecord.value
                        )
                    )
                }

                trySend(ResultState.Succes("Record added successfully"))
            }
            .addOnFailureListener {
                trySend(ResultState.error(it.localizedMessage ?: "Error"))
            }

        awaitClose { close() }
    }

    // ---------------- GET ALL RECORDS ----------------

    override fun getAllRecords(): Flow<ResultState<List<RecordModel>>> = callbackFlow {

        trySend(ResultState.Loading)

        val uid = auth.currentUser?.uid ?: run {
            trySend(ResultState.error("User not logged in"))
            close()
            return@callbackFlow
        }

        // 🔥 Firestore listener → Sync to Room
        val listener = firestore
            .collection("Users")
            .document(uid)
            .collection("Records")
            .addSnapshotListener { snapshot, error ->

                if (error != null) return@addSnapshotListener

                val records = snapshot?.documents?.mapNotNull { doc ->
                    val record = doc.toObject(RecordModel::class.java)
                    record?.copy(recordID = doc.id)
                } ?: emptyList()

                ioScope.launch {
                    recordDao.insertAll(
                        records.map {
                            RecordEntity(
                                recordID = it.recordID,
                                title = it.title,
                                value = it.value
                            )
                        }
                    )
                }
            }

        // 🔥 Room drives UI
        ioScope.launch {
            recordDao.getAllRecords().collectLatest { entities ->
                val models = entities.map {
                    RecordModel(
                        recordID = it.recordID,
                        title = it.title,
                        value = it.value
                    )
                }
                trySend(ResultState.Succes(models))
            }
        }

        awaitClose { listener.remove() }
    }

    // ---------------- UPDATE ----------------

    override fun updateRecord(record: RecordModel): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val uid = auth.currentUser?.uid ?: run {
            trySend(ResultState.error("User not logged in"))
            close()
            return@callbackFlow
        }

        firestore.collection("Users")
            .document(uid)
            .collection("Records")
            .document(record.recordID)
            .update(
                mapOf(
                    "title" to record.title,
                    "value" to record.value
                )
            )
            .addOnSuccessListener {

                ioScope.launch {
                    recordDao.update(
                        RecordEntity(
                            recordID = record.recordID,
                            title = record.title,
                            value = record.value
                        )
                    )
                }

                trySend(ResultState.Succes("Record updated successfully"))
            }
            .addOnFailureListener {
                trySend(ResultState.error(it.localizedMessage ?: "Error"))
            }

        awaitClose { close() }
    }

    // ---------------- DELETE ----------------

    override fun deleteRecord(recordID: String): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        val uid = auth.currentUser?.uid ?: run {
            trySend(ResultState.error("User not logged in"))
            close()
            return@callbackFlow
        }

        firestore.collection("Users")
            .document(uid)
            .collection("Records")
            .document(recordID)
            .delete()
            .addOnSuccessListener {

                ioScope.launch {
                    recordDao.deleteById(recordID)
                }

                trySend(ResultState.Succes("Record deleted successfully"))
            }
            .addOnFailureListener {
                trySend(ResultState.error(it.localizedMessage ?: "Error"))
            }

        awaitClose { close() }
    }

    override fun getRecordById(recordID: String): Flow<ResultState<RecordModel>> {
        return callbackFlow {
            val entity = recordDao.getById(recordID)
            if (entity != null) {
                trySend(
                    ResultState.Succes(
                        RecordModel(
                            recordID = entity.recordID,
                            title = entity.title,
                            value = entity.value
                        )
                    )
                )
            } else {
                trySend(ResultState.error("Record not found"))
            }
            close()
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    override fun logoutUser() {
        auth.signOut()
    }

}

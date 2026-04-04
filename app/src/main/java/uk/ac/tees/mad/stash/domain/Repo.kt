package uk.ac.tees.mad.stash.domain

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.stash.model.RecordModel
import uk.ac.tees.mad.stash.model.ResultState
import uk.ac.tees.mad.stash.model.UserData

interface Repo {
    fun registeruserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
    fun loginuserwithemailandpassword(userdata: UserData): Flow<ResultState<String>>
    fun addRecord(record: RecordModel):Flow<ResultState<String>>
    fun updateRecord(record: RecordModel): Flow<ResultState<String>>

    fun getAllRecords(): Flow<ResultState<List<RecordModel>>>
    fun deleteRecord(recordID: String): Flow<ResultState<String>>
    fun getRecordById(recordID: String): Flow<ResultState<RecordModel>>
    fun isUserLoggedIn(): Boolean
    fun logoutUser()

    // Preferences
    fun getBiometricEnabled(): Flow<Boolean>
    suspend fun setBiometricEnabled(enabled: Boolean)
    fun getLastActiveTimestamp(): Flow<Long>
    suspend fun setLastActiveTimestamp(timestamp: Long)

}
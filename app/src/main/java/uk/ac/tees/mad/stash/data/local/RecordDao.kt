package uk.ac.tees.mad.stash.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: RecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<RecordEntity>)

    @Update
    suspend fun update(record: RecordEntity)

    @Query("DELETE FROM records WHERE recordID = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM records WHERE userId = :userId")
    fun getAllRecords(userId: String): Flow<List<RecordEntity>>
    
    @Query("SELECT * FROM records WHERE recordID = :id AND userId = :userId")
    suspend fun getById(id: String, userId: String): RecordEntity?

    @Query("DELETE FROM records")
    suspend fun clearAll()
}

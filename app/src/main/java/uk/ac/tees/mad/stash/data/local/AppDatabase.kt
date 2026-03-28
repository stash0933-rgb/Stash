package uk.ac.tees.mad.stash.data.local
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecordEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}

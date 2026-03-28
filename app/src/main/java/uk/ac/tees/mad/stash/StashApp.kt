package uk.ac.tees.mad.stash



import android.app.Application
import androidx.room.Room
import uk.ac.tees.mad.stash.data.local.AppDatabase

class StashApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "stash_db"
        ).build()
    }
}

package uk.ac.tees.mad.stash



import android.app.Application
import androidx.room.Room
import uk.ac.tees.mad.stash.data.local.AppDatabase
import uk.ac.tees.mad.stash.data.local.PreferencesManager

class StashApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
        lateinit var preferencesManager: PreferencesManager
            private set
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "stash_db"
        ).fallbackToDestructiveMigration().build()

        preferencesManager = PreferencesManager(applicationContext)
    }
}

package dev.namitala.metas

import android.app.Application
import androidx.room.Room
import dev.namitala.metas.database.AppDatabase
import dev.namitala.metas.repository.GoalsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalsApplication : Application() {
    private lateinit var db : AppDatabase
    lateinit var goalsRepository: GoalsRepository

    val database : AppDatabase
        get() = db

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this)
        goalsRepository = GoalsRepository(db.goalDao())
    }
}
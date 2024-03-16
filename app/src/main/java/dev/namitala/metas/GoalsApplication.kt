package dev.namitala.metas

import android.app.Application
import android.content.Context
import androidx.room.Room
import dev.namitala.metas.database.AppDatabase
import dev.namitala.metas.repository.GoalsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GoalsApplication : Application() {
    private lateinit var db : AppDatabase
    lateinit var goalsRepository: GoalsRepository

    val database : AppDatabase
        get() = db

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GoalsApplication)
            modules(goalsModule)
        }
    }
}
package dev.namitala.metas.widgets

import android.content.Intent
import android.widget.RemoteViewsService
import dev.namitala.metas.GoalsApplication
import dev.namitala.metas.database.AppDatabase
import dev.namitala.metas.repository.GoalsRepository

class GoalWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return GoalWidgetDataProvider(applicationContext, (application as GoalsApplication).goalsRepository)
    }
}
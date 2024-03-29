package dev.namitala.metas.widgets

import android.content.Intent
import android.widget.RemoteViewsService
import dev.namitala.metas.repository.GoalsRepository
import org.koin.android.ext.android.inject

class GoalWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val repository : GoalsRepository by inject()
        return GoalWidgetDataProvider(applicationContext, repository)
    }
}
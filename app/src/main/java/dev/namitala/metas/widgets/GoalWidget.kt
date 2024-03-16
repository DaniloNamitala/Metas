package dev.namitala.metas.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import dev.namitala.metas.GoalsActivity
import dev.namitala.metas.GoalsApplication
import dev.namitala.metas.R
import dev.namitala.metas.repository.GoalsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent


class GoalWidget : AppWidgetProvider(), KoinComponent {
    companion object {
        private const val ACTION_INCREMENT = "INTENT_INCREMENT"
        const val UPDATE_TYPE = "UPDATE_TYPE"
        const val NAME_KEY = "NAME_KEY"
        const val COUNT_KEY = "COUNT_KEY"
        const val INCREMENT = "INCREMENT"
        const val DECREMENT = "DECREMENT"
        const val INCREMENT_SIZE = "INCREMENT_SIZE"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val widgetManager = AppWidgetManager.getInstance(context.applicationContext)
        widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(ComponentName(context.applicationContext.packageName, GoalWidget::class.java.name)), R.id.widget_listView)

        for (appWidgetId in appWidgetIds) {
            val addIntent = Intent(context, javaClass)
            addIntent.action = ACTION_INCREMENT
            val addPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, addIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

            val serviceIntent = Intent(context, GoalWidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

            val intent = Intent(context, GoalsActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val views = RemoteViews(context.packageName, R.layout.goal_widget)

            views.setOnClickPendingIntent(R.id.layout_root, pendingIntent)
            views.setRemoteAdapter(R.id.widget_listView, serviceIntent)
            views.setEmptyView(R.id.widget_listView, R.id.goal_widget_empty)
            views.setPendingIntentTemplate(R.id.widget_listView, addPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val repository : GoalsRepository by inject()
        if (intent != null) {
            when (intent.action) {
                ACTION_INCREMENT -> {
                    val curName = intent.getStringExtra(NAME_KEY) ?: ""

                    when (intent.extras?.getString(UPDATE_TYPE,"")) {
                        INCREMENT, DECREMENT -> {
                            val multi = intent.getIntExtra(INCREMENT_SIZE, 1)
                            val count = intent.getIntExtra(COUNT_KEY, 0)
                            CoroutineScope(Dispatchers.Main).launch {
                                repository.incrementWidget(curName, count + multi)
                                val widgetManager = AppWidgetManager.getInstance(context!!.applicationContext)
                                widgetManager.notifyAppWidgetViewDataChanged(
                                    widgetManager.getAppWidgetIds(
                                        ComponentName(
                                            context.applicationContext.packageName,
                                            GoalWidget::class.java.name
                                        )
                                    ), R.id.widget_listView
                                )
                            }
                        }
                    }
                }
            }
        }
        super.onReceive(context, intent)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) { }

    override fun onEnabled(context: Context) { }

    override fun onDisabled(context: Context) { }
}
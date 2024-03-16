package dev.namitala.metas.widgets

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import dev.namitala.metas.R
import dev.namitala.metas.repository.GoalsRepository

class GoalWidgetDataProvider    (
    private val context: Context,
    private val repository : GoalsRepository
) : RemoteViewsFactory {

    override fun onCreate() { }

    override fun onDataSetChanged() {
        if (repository.getGoals().isNotEmpty()) {
            getViewAt(0)
        }
    }

    override fun onDestroy() { }

    override fun getCount(): Int {
        return repository.getGoals().size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val fillIntentAdd = Intent()
        val goal = repository.getGoals()[position]
        fillIntentAdd.putExtra(GoalWidget.UPDATE_TYPE,GoalWidget.INCREMENT)
        fillIntentAdd.putExtra(GoalWidget.NAME_KEY, goal.name)
        fillIntentAdd.putExtra(GoalWidget.COUNT_KEY, goal.count)
        fillIntentAdd.putExtra(GoalWidget.INCREMENT_SIZE, goal.increment)

        val fillIntentSub = Intent()
        fillIntentSub.putExtra(GoalWidget.UPDATE_TYPE,GoalWidget.DECREMENT)
        fillIntentSub.putExtra(GoalWidget.NAME_KEY, goal.name)
        fillIntentSub.putExtra(GoalWidget.COUNT_KEY, goal.count)
        fillIntentSub.putExtra(GoalWidget.INCREMENT_SIZE, -goal.increment)

        val remoteViews = RemoteViews(context.packageName, R.layout.goal_item_widget)

        remoteViews.setTextViewText(R.id.name, goal.name)
        remoteViews.setTextViewText(R.id.count_text, goal.count.toString())

        remoteViews.setTextColor(R.id.count_text, goal.fgColor)
        remoteViews.setTextColor(R.id.name, goal.fgColor)
        remoteViews.setInt(R.id.btn_increment, "setColorFilter", goal.fgColor)
        remoteViews.setInt(R.id.btn_decrement, "setColorFilter", goal.fgColor)
        remoteViews.setInt(R.id.icon, "setColorFilter", goal.fgColor)
        remoteViews.setInt(R.id.image_bg, "setColorFilter", goal.bgColor)

        remoteViews.setOnClickFillInIntent(R.id.btn_increment, fillIntentAdd)
        remoteViews.setOnClickFillInIntent(R.id.btn_decrement, fillIntentSub)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = i.toLong()

    override fun hasStableIds(): Boolean = true
}
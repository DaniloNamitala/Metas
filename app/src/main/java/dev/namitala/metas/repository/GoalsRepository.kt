package dev.namitala.metas.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dev.namitala.metas.database.GoalDao
import dev.namitala.metas.model.GoalItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface GoalsRepository {
    fun setGoal(goal : GoalItem, old : GoalItem?)
    fun update(idx : Int, goal : GoalItem)
    fun incrementWidget(name : String, count : Int)
    fun deleteGoal(goal : GoalItem)
    fun getGoals() : List<GoalItem>
    fun goalsLiveData() : LiveData<List<GoalItem>>
}
class GoalsRepositoryImpl(private val prefs : SharedPreferences) : GoalsRepository {

    companion object {
        const val GOALS_KEY = "GOALS_KEY"
    }

    private val gson = Gson()
    private var goalsList : ArrayList<GoalItem> = ArrayList()
    private var goalsLiveData : MutableLiveData<List<GoalItem>> = MutableLiveData(goalsList)

    init {
        loadGoals()
        prefs.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == GOALS_KEY) {
                goalsLiveData.value = goalsList
            }
        }
    }

    private fun saveGoals() {
        val goalSet = goalsList.map { gson.toJson(it) }.toSet()
        prefs.edit().putStringSet(GOALS_KEY, goalSet).apply()
    }

    private fun loadGoals() {
        val goalSet = prefs.getStringSet(GOALS_KEY, setOf())
        goalSet?.map { gson.fromJson(it, GoalItem::class.java) }?.let { list ->
            goalsList.addAll(list)
        }
    }

    override fun getGoals(): List<GoalItem> = goalsList

    override fun goalsLiveData(): LiveData<List<GoalItem>> = goalsLiveData
    override fun setGoal(goal : GoalItem, old : GoalItem?) {
        if (old == null) {
            goalsList.add(goal)
        } else {
            val idx = goalsList.indexOf(old)
            goalsList[idx] = goal
        }
        saveGoals()
    }

    override fun update(idx : Int, goal : GoalItem) {
        goalsList[idx] = goal
        saveGoals()
    }

    override fun incrementWidget(name : String, count : Int) {
        goalsList.find { it.name == name }?.let {
            val idx = goalsList.indexOf(it)
            goalsList[idx] = it.copy(count = count)
        }
        saveGoals()
    }

    override fun deleteGoal(goal : GoalItem) {
        goalsList.remove(goal)
        saveGoals()
    }
}
package dev.namitala.metas.repository

import androidx.lifecycle.LiveData
import dev.namitala.metas.database.GoalDao
import dev.namitala.metas.model.GoalItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalsRepository(private val goalsDao : GoalDao) {
    init {
        loadGoals()
    }

    private var goalsList : ArrayList<GoalItem> = ArrayList()
    val goals : List<GoalItem> = goalsList
    val goalsLivedata : LiveData<List<GoalItem>> = goalsDao.getAllGoalsLiveData()

    fun setGoal(goal : GoalItem, old : GoalItem?) {
        if (old == null) {
            goalsList.add(goal)
            CoroutineScope(Dispatchers.IO).launch {
                goalsDao.insert(goal).let {
                    goal.id = it[0]
                }
            }
        } else {
            val idx = goalsList.indexOf(old)
            goalsList[idx] = goal
            CoroutineScope(Dispatchers.IO).launch {
                goalsDao.update(goal)
            }
        }
    }

    fun update(idx : Int, goal : GoalItem) {
        goalsList[idx] = goal
        CoroutineScope(Dispatchers.IO).launch {
            goalsDao.update(goal)
        }
    }

    fun incrementWidget(name : String, count : Int) {
        goalsList.find { it.name == name }?.let {
            val idx = goalsList.indexOf(it)
            goalsList[idx] = it.copy(count = count)

            CoroutineScope(Dispatchers.IO).launch {
                goalsDao.update(goalsList[idx])
            }
        }
    }

    fun deleteGoal(goal : GoalItem) {
        goalsList.remove(goal)
        CoroutineScope(Dispatchers.IO).launch {
            goalsDao.delete(goal)
        }
    }

    private fun loadGoals() {
        CoroutineScope(Dispatchers.IO).launch {
            goalsDao.getAllGoals().let {
                goalsList.addAll(it)
            }
        }
    }
}
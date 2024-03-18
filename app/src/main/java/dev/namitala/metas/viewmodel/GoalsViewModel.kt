package dev.namitala.metas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.namitala.metas.model.GoalItem
import dev.namitala.metas.repository.GoalsRepository
class GoalsViewModel(private val repository: GoalsRepository) : ViewModel() {
    val goals : LiveData<List<GoalItem>> = repository.goalsLiveData()

    fun addGoal(goal : GoalItem, old : GoalItem?) {
        repository.setGoal(goal, old)
    }

    fun incrementGoal(idx : Int, inc : Int) {
        val goal = goals.value?.get(idx)
        goal?.let {
            val new = goal.copy(count = goal.count.plus(inc).coerceAtLeast(0))
            repository.update(idx, new)
        }
    }

    fun deleteGoal(pos : Int) {
        goals.value?.get(pos)?.let { g ->
            repository.deleteGoal(g)
        }
    }
}
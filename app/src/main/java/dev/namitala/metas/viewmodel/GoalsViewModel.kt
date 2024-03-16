package dev.namitala.metas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import dev.namitala.metas.model.GoalItem
import dev.namitala.metas.repository.GoalsRepository
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import dev.namitala.metas.GoalsApplication
import dev.namitala.metas.database.AppDatabase

class GoalsViewModel(private val repository: GoalsRepository) : ViewModel() {
    val goals : LiveData<List<GoalItem>> = repository.goalsLivedata

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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[AndroidViewModelFactory.APPLICATION_KEY]) as GoalsApplication

                return GoalsViewModel(
                    GoalsRepository(application.database.goalDao()),
                ) as T
            }
        }
    }
}
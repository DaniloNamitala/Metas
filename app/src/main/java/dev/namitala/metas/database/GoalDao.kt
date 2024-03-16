package dev.namitala.metas.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.namitala.metas.model.GoalItem

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals")
    fun getAllGoals() : List<GoalItem>

    @Query("SELECT * FROM goals")
    fun getAllGoalsLiveData() : LiveData<List<GoalItem>>

    @Insert
    fun insert(vararg goals : GoalItem) : List<Long>

    @Update
    fun update(goal : GoalItem)

    @Delete
    fun delete(goal : GoalItem)
}
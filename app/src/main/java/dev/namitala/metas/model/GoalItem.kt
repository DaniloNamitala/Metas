package dev.namitala.metas.model

import android.graphics.Color
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName  = "goals")
@Parcelize
data class GoalItem(
    @PrimaryKey(autoGenerate = true) var id : Long = 0,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "count") val count : Int = 0,
    @ColumnInfo(name = "goal") val goal : Int? = null,
    @ColumnInfo(name = "increment") val increment : Int = 1,
    @ColumnInfo(name = "bg_color") val bgColor : Int = Color.WHITE,
    @ColumnInfo(name = "fg_color") val fgColor : Int = Color.BLACK
) : Parcelable

package dev.namitala.metas.model

import android.graphics.Color
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalItem(
    val name : String,
    val count : Int = 0,
    val goal : Int? = null,
    val increment : Int = 1,
    val bgColor : Int = Color.WHITE,
    val fgColor : Int = Color.BLACK
) : Parcelable

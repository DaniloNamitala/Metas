package dev.namitala.metas

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.ScaleAnimation
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev.namitala.metas.databinding.GoalItemBinding
import dev.namitala.metas.extensions.colorMultiply
import dev.namitala.metas.extensions.isDarkColor
import dev.namitala.metas.model.GoalItem


class GoalsAdapter(
    private var goals : List<GoalItem>,
    private val eventListener: GoalsEventListener
) : Adapter<GoalsAdapter.GoalsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        val binding = GoalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalsViewHolder(binding, eventListener)
    }

    override fun getItemCount(): Int = goals.size

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        val goal = goals[position]
        holder.bind(goal)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list : List<GoalItem>) {
        goals = list
        notifyDataSetChanged()
    }

    class GoalsViewHolder(private val view: GoalItemBinding, private val eventListener: GoalsEventListener) : ViewHolder(view.root) {
        private val increment = view.btnIncrement
        private val decrement = view.btnDecrement
        private val icon = view.icon
        private val countText = view.countText
        private val progress = view.progress
        private val name = view.name
        private val options = view.options
        private val content = view.content
        private val delete = view.optionDelete
        private val edit = view.optionEdit

        private val handler = Handler(Looper.getMainLooper())
        private val hideRunnable = Runnable {
            content.animate().x(0f)
        }

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                progress.min = 0
            }
        }

        fun bind(goal : GoalItem) {
            name.text = goal.name
            countText.text = goal.count.toString()
            progress.max = goal.goal ?: 1

            goal.goal?.let {
                progress.progress = goal.count
            }

            progress.trackColor = goal.bgColor
            val multiplier = if (goal.bgColor.isDarkColor()) 1.25F else 0.85F
            progress.indicatorColor[0] = goal.bgColor.colorMultiply(multiplier)
            countText.setTextColor(goal.fgColor)
            name.setTextColor(goal.fgColor)
            increment.setColorFilter(goal.fgColor)
            decrement.setColorFilter(goal.fgColor)
            icon.setColorFilter(goal.fgColor)

            setListeners(goal)
        }

        private fun setListeners(goal : GoalItem) {
            delete.setOnClickListener {
                eventListener.deleteGoal(layoutPosition)
            }

            edit.setOnClickListener {
                eventListener.editGoal(layoutPosition)
            }

             view.root.setOnLongClickListener {
                 handler.removeCallbacks(hideRunnable)
                 content.animate().x(-(options.width.toFloat()+20))
                 handler.postDelayed(hideRunnable, 3000L)
                 true
             }

            increment.setOnClickListener {
                eventListener.increment(layoutPosition, goal.increment)
            }

            decrement.setOnClickListener {
                eventListener.increment(layoutPosition, -goal.increment)
            }
        }
    }

    interface GoalsEventListener {
        fun increment(position: Int, increment : Int)
        fun editGoal(position: Int)
        fun deleteGoal(position: Int)
    }
}
package dev.namitala.metas

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.namitala.metas.databinding.ActivityMainBinding
import dev.namitala.metas.model.GoalItem
import dev.namitala.metas.repository.GoalsRepository
import dev.namitala.metas.viewmodel.GoalsViewModel
import dev.namitala.metas.widgets.GoalWidget
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GoalsActivity : AppCompatActivity(), NewGoalBottomSheet.AddGoalListener, GoalsAdapter.GoalsEventListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var goalsAdapter : GoalsAdapter
    private val viewModel: GoalsViewModel by viewModel()
    private val aaaa: GoalsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapter()
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        goalsAdapter.notifyDataSetChanged()
    }

    private fun setListeners() {
        binding.btnAdd.setOnClickListener {
            openNewGoalBottomSheet()
        }

        viewModel.goals.observe(this) {
            val manager = AppWidgetManager.getInstance(applicationContext)
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(ComponentName(applicationContext.packageName, GoalWidget::class.java.name)), R.id.widget_listView)

            if (::goalsAdapter.isInitialized && it != null)
                goalsAdapter.setList(it)
        }

    }

    private fun openNewGoalBottomSheet() {
        val modal = NewGoalBottomSheet.newInstance()
        modal.listener = this
        modal.show(supportFragmentManager, NewGoalBottomSheet.MODAL_NAME)
    }

    private fun setAdapter() {
        goalsAdapter = GoalsAdapter(viewModel.goals.value ?: listOf(), this)
        binding.recyclerGoals.adapter = goalsAdapter
    }

    override fun addGoal(goal : GoalItem, old : GoalItem?) {
        viewModel.addGoal(goal, old)
        if (old == null) {
            viewModel.goals.value?.size?.let { goalsAdapter.notifyItemInserted(it) }
        } else {
            val idx = viewModel.goals.value?.indexOf(goal) ?: 0
            viewModel.goals.value?.size?.let { goalsAdapter.notifyItemChanged(idx) }
        }
    }

    override fun increment(position: Int, increment: Int) {
        viewModel.incrementGoal(position, increment)
        goalsAdapter.notifyItemChanged(position, viewModel.goals.value?.get(position))
    }

    override fun editGoal(position: Int) {
        viewModel.goals.value?.get(position)?.let {
            val modal = NewGoalBottomSheet.newInstance(it)
            modal.listener = this
            modal.show(supportFragmentManager, NewGoalBottomSheet.MODAL_NAME)
        }
    }

    override fun deleteGoal(position: Int) {
        viewModel.deleteGoal(position)
        goalsAdapter.notifyItemRemoved(position)
    }
}
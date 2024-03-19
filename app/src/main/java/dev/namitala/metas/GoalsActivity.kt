package dev.namitala.metas

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private fun showDeleteConfirmation(position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.confirm_delete))
            .setPositiveButtonIcon(AppCompatResources.getDrawable(this, R.drawable.icon_confirm))
            .setNegativeButtonIcon(AppCompatResources.getDrawable(this, R.drawable.icon_cancel))
            .setNegativeButton("") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("") { _, _ ->
                viewModel.deleteGoal(position)
                goalsAdapter.notifyItemRemoved(position)
            }
            .show()
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
        val pos = viewModel.addGoal(goal, old)
        if (old == null) {
            viewModel.goals.value?.size?.let { goalsAdapter.notifyItemInserted(pos) }
        } else {
            viewModel.goals.value?.size?.let { goalsAdapter.notifyItemChanged(pos) }
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

    override fun restoreGoal(position: Int) {
        viewModel.restoreGoal(position)
        goalsAdapter.notifyItemChanged(position, viewModel.goals.value?.get(position))
    }

    override fun deleteGoal(position: Int) {
        showDeleteConfirmation(position)
    }
}
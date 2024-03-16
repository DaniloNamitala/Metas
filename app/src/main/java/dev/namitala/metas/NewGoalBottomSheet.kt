package dev.namitala.metas

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dev.namitala.metas.databinding.NewGoalBottomsheetBinding
import dev.namitala.metas.model.GoalItem

class NewGoalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding : NewGoalBottomsheetBinding
    var listener : AddGoalListener? = null
    private var bgColor : Int = Color.WHITE
    private var fgColor : Int = Color.BLACK

    private var goal : GoalItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewGoalBottomsheetBinding.inflate(inflater)

        if (arguments?.containsKey(GOAL_EDIT) == true) {
            goal = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU)
                arguments?.getParcelable(GOAL_EDIT, GoalItem::class.java)
            else
                arguments?.get(GOAL_EDIT) as? GoalItem
        }

        binding.editIncrement.setText("1")
        goal?.let { goal ->
            binding.editName.setText(goal.name)
            binding.editGoal.setText(goal.goal?.toString() ?: "")
            binding.editIncrement.setText(goal.increment.toString())
            fgColor = goal.fgColor
            bgColor = goal.bgColor
        }

        binding.colorPickerFg.backgroundTintList = ColorStateList.valueOf(fgColor)
        binding.colorPickerBg.backgroundTintList = ColorStateList.valueOf(bgColor)

        setListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.editName.requestFocus()
    }

    private fun setListeners() {

        binding.editGoal.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                Toast.makeText(requireContext(), requireContext().getString(R.string.goal_toast), Toast.LENGTH_LONG).show()
        }

        binding.colorPickerBg.setOnClickListener {
            openColorPicker(it, PREFERENCE_BG)
        }

        binding.colorPickerFg.setOnClickListener {
            openColorPicker(it, PREFERENCE_FG)
        }

        binding.btnSave.setOnClickListener {
            val name = binding.editName.text.toString().trim()
            val strGoal = binding.editGoal.text.toString().trim()
            val goalInt = strGoal.ifEmpty { null }?.toInt()
            val strInc = binding.editIncrement.text.toString().trim()
            val intInc = strInc.ifEmpty { "1" }.toInt()

            val newGoal =
                goal?.copy(name = name, goal = goalInt, fgColor = fgColor, bgColor = bgColor, increment = intInc) ?:
                GoalItem(name = name, goal = goalInt, fgColor = fgColor, bgColor = bgColor, increment = intInc)

            listener?.addGoal(newGoal, goal)
            dismiss()
        }
    }

    private fun openColorPicker(picker : View, preference : String) {
        ColorPickerDialog.Builder(requireContext())
            .setTitle(requireContext().getString(R.string.select_color))
            .setPreferenceName(preference)
            .setPositiveButton(requireContext().getString(R.string.select), object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    envelope?.color?.let {
                        color -> picker.backgroundTintList = ColorStateList.valueOf(color)
                        if (picker == binding.colorPickerBg)
                            bgColor = color
                        else
                            fgColor = color
                    }
                }
            })
            .setNegativeButton(requireContext().getString(R.string.cancel)
            ) { _, _ ->  }
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    companion object {
        const val PREFERENCE_FG = "FG_PREFERENCE_COLOR"
        const val PREFERENCE_BG = "BG_PREFERENCE_COLOR"
        const val MODAL_NAME = "NEW_GOAL_BOTTOMSHEET"
        const val GOAL_EDIT = "GOAL_EDIT"
        fun newInstance(goal : GoalItem? = null) = NewGoalBottomSheet().apply {
            arguments = Bundle().apply {
                goal?.let {
                    putParcelable(GOAL_EDIT, goal)
                }
            }
        }
    }

    interface AddGoalListener {
        fun addGoal(goal: GoalItem, old : GoalItem?)
    }
}
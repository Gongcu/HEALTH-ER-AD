package com.health.myapplication.ui.custom_program.program_info

import com.health.myapplication.callback.ItemTouchHelperCallback.ItemMoveListener
import com.health.myapplication.dialog.ProgramDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import com.health.myapplication.R
import androidx.recyclerview.widget.ItemTouchHelper
import com.health.myapplication.callback.ItemTouchHelperCallback
import com.health.myapplication.listener.ProgramDialogListener
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.health.myapplication.dialog.DialogType
import com.health.myapplication.entity.custom_program.CustomProgramExercise
import com.health.myapplication.ui.custom_program.CustomProgramViewModel
import kotlinx.android.synthetic.main.fragment_bodyweight_data.view.recycler_view
import kotlinx.android.synthetic.main.fragment_custom_program.view.*
import kotlin.collections.ArrayList

class CustomProgramFragment : Fragment(), ItemMoveListener {
    private var DIVIDE = 0
    private var ACTIVITY_NUMBER = 0
    private var list: ArrayList<CustomProgramExercise> = ArrayList()
    private val viewModel : CustomProgramViewModel by viewModels()
    private val dialog : ProgramDialog by lazy {
        ProgramDialog(requireContext(), DialogType.INSERT)
    }
    private val adapter : CustomProgramExerciseAdapter by lazy{
        CustomProgramExerciseAdapter(requireContext())
    }

    private var orderChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIVIDE = requireArguments().getInt("date")
        ACTIVITY_NUMBER = requireArguments().getInt("activity_number")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_custom_program, container, false)
        val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(this)
        val touchHelper = ItemTouchHelper(callback)

        dialog.setDialogListener(ProgramDialogListener { _, part, exercise, set, rep ->
            val customProgramExercise = CustomProgramExercise(ACTIVITY_NUMBER,DIVIDE,part,exercise,set,rep)
            viewModel.insert(customProgramExercise,ACTIVITY_NUMBER,DIVIDE)
        })

        view.apply {
            touchHelper.attachToRecyclerView(this.recycler_view)
            recycler_view.adapter = adapter
            this.add_btn.setOnClickListener(View.OnClickListener {
                dialog.show()
            })
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCustomProgramExercise(ACTIVITY_NUMBER, DIVIDE).observe(viewLifecycleOwner) {
            Log.d("GET LIST ",it.toString())
            list=ArrayList(it)
            adapter.setList(list)
            adapter.notifyDataSetChanged()
        }

        viewModel.errorAlert.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),"이미 해당 운동이 존재합니다.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        if(orderChange) {
            Log.d("onPause","UPDATE LIST")
            viewModel.updateList(list)
            orderChange=false
        }
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        orderChange = true
        list.add(toPosition, list.removeAt(fromPosition))
        adapter.notifyItemMoved(fromPosition,toPosition)
    }

    override fun onItemSwiped(id: Int) {
        viewModel.delete(id)
    }

    override fun onItemMoveFinished() {}

    companion object {
        fun newInstance(DATE: Int, ACTIVITY_NUMBER: Int): CustomProgramFragment {
            val fragment = CustomProgramFragment()
            val args = Bundle()
            args.putInt("date", DATE)
            args.putInt("activity_number", ACTIVITY_NUMBER)
            fragment.arguments = args
            return fragment
        }
    }
}
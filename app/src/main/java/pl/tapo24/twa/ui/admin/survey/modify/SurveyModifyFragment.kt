package pl.tapo24.twa.ui.admin.survey.modify

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.SurveyAnswerEditModeAdapter
import pl.tapo24.twa.data.survey.SurveyAnswer
import pl.tapo24.twa.databinding.FragmentSurveyModifyBinding
import pl.tapo24.twa.ui.helpers.spb.viewer.SpbViewerFragmentArgs
import pl.tapo24.twa.utils.DataFormatterConverter
import java.util.*

@AndroidEntryPoint
class SurveyModifyFragment : Fragment(), DataFormatterConverter {
    companion object {
        fun newInstance() = SurveyModifyFragment()
    }

    private var _binding: FragmentSurveyModifyBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: SurveyModifyViewModel

    private lateinit var datePickerDialog: DatePickerDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SurveyModifyViewModel::class.java)
        _binding = FragmentSurveyModifyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        datePickerDialog = DatePickerDialog(requireContext())

        val args = arguments?.let { SurveyModifyFragmentArgs.fromBundle(it) }
        if (args!!.survey != null) {
            viewModel.dataModel = args.survey!!
            viewModel.name = args.survey.name
            viewModel.dateStart = convertTimeStampToHumanCalendarDate(args.survey.startDate.toInt())
            viewModel.dateEnd = convertTimeStampToHumanCalendarDate(args.survey.endDate.toInt())
            viewModel.activate = args.survey.active
            viewModel.singleChoice = args.survey.singleChoice
            viewModel.toVipUser = args.survey.toVipUser
            viewModel.toLoginUser = args.survey.toLoginUser
            viewModel.answerList.value = args.survey.surveyAnswers?.sortedBy { it.sortId }?.toMutableList()

        }
        // CHECK ARGS AND MAP TO VIEWMODEL


        //TYPE
        binding.name.setText(viewModel.name)
        if (viewModel.singleChoice) {
           binding.radioSingleChoice.isChecked = true
        } else {
            binding.radioMultiChoice.isChecked = true
        }
        binding.radioChoice.setOnCheckedChangeListener { group, checkedId ->
            viewModel.singleChoice = checkedId == R.id.radioSingleChoice
        }
        //STATUS
        if (viewModel.activate) {
            binding.radioActive.isChecked = true
        } else {
            binding.radioDeactive.isChecked = true
        }
        binding.radioStatus.setOnCheckedChangeListener { group, checkedId ->
            viewModel.activate = checkedId == R.id.radioActive
        }
        //Target
        // ALL
        if (viewModel.toLoginUser == null && viewModel.toVipUser == null) {
            binding.radioAll.isChecked = true
        }
        // only login with vip
        if (viewModel.toLoginUser == true && viewModel.toVipUser == true) {
            binding.radioOnlyLogin.isChecked = true
        }
        // non login
        if (viewModel.toLoginUser == false && viewModel.toVipUser == null) {
            binding.radioOnlyNonLogin.isChecked = true
        }
        // only vip
        if (viewModel.toLoginUser == false && viewModel.toVipUser == true) {
            binding.radioOnlyVip.isChecked = true
        }
        // only login non vip
        if (viewModel.toLoginUser == true && viewModel.toVipUser == false) {
            binding.radioOnlyLoginNonVip.isChecked = true
        }

        // map from check to model
        binding.radioTarget.setOnCheckedChangeListener { group, checkedId ->
            //all
            println(viewModel.dateEnd)
            if (checkedId == R.id.radioAll) {
                viewModel.toLoginUser = null
                viewModel.toVipUser = null
            }
            // only login with vip
            if (checkedId == R.id.radioOnlyLogin) {
                viewModel.toLoginUser = true
                viewModel.toVipUser = true
            }
            // non login
            if (checkedId == R.id.radioOnlyNonLogin) {
                viewModel.toLoginUser = false
                viewModel.toVipUser = null
            }
            // only vip
            if (checkedId == R.id.radioOnlyVip) {
                viewModel.toLoginUser = false
                viewModel.toVipUser = true
            }
            // only login non vip
            if (checkedId == R.id.radioOnlyLoginNonVip) {
                viewModel.toLoginUser = true
                viewModel.toVipUser = false
            }
        }

        // date
        binding.dateEndTIET.setText(viewModel.dateEnd)
        binding.dateStartTIET.setText(viewModel.dateStart)
        binding.dateStartTIET.setOnClickListener {
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->

                binding.dateStartTIET.setText(formatToHumanCalendarDate(year,month, dayOfMonth))
                viewModel.dateStart = formatToHumanCalendarDate(year,month, dayOfMonth)

            }
            datePickerDialog.show()
        }
        binding.dateEndTIET.setOnClickListener {
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->

                binding.dateEndTIET.setText(formatToHumanCalendarDate(year,month, dayOfMonth))
                viewModel.dateEnd = formatToHumanCalendarDate(year,month, dayOfMonth)
            }
            datePickerDialog.show()
        }

//        binding.publish.setOnClickListener {
//           // println(convertHumanCalendarDateToTimeStamp(viewModel.dateStart!!))
//            println(convertTimeStampToHumanCalendarDate(1712613600))
//        }
        binding.addAnswer.setOnClickListener {
            val viewDialogWithEditNameCustomCategory = layoutInflater.inflate(R.layout.dialog_add_survey_answer, null)
            viewDialogWithEditNameCustomCategory.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.surveyAnswer).setText("")
            val dialogAddAnswer = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.addAnswer))
                .setView(viewDialogWithEditNameCustomCategory)
                .setPositiveButton("Dodaj") { dialog, which ->
                    // Respond to positive button press
                    val answer = viewDialogWithEditNameCustomCategory.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.surveyAnswer).text.toString()
                    viewModel.addAnswer(answer)
                }
                .create()
            dialogAddAnswer.show()

        }

        val rv = binding.recyclerView
        val itemTouchHelper = ItemTouchHelper(moveCallback)
        itemTouchHelper.attachToRecyclerView(rv)
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = SurveyAnswerEditModeAdapter(viewModel.answerList.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.answerList.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })

        binding.save.setOnClickListener {
            viewModel.name = binding.name.text.toString()
            viewModel.saveSurvey()
        }

        if (viewModel.dataModel.id == null ){
            binding.publish.visibility = View.GONE
        } else {
            binding.publish.visibility = View.VISIBLE
        }
        binding.publish.setOnClickListener {
            val infoDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Publikacja ankiety")
                .setMessage("Czy na pewno chcesz opublikować ankietę? Po jej publikacji modyfikacja nie będzie możliwa.")
                .setPositiveButton("Tak") { dialog, which ->
                    viewModel.saveSurvey(true)
                }
                .setNegativeButton("Nie") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Zapis")
            .setMessage("Proszę czekać")
            .setCancelable(true)
            .create()
        viewModel.showLoader.observe(viewLifecycleOwner, Observer {
            if (it) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        })
        viewModel.moveBack.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().popBackStack()
                viewModel.moveBack.value = false
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val moveCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            viewModel.answerList.value!!.let { Collections.swap(it, fromPosition, toPosition) }
            viewModel.adapter.notifyItemMoved(fromPosition, toPosition)


            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewModel.changeOrder()
        }

    }

}


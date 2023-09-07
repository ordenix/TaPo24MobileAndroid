package pl.tapo24.twa.ui.road.controlList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.data.PriorityList
import pl.tapo24.twa.databinding.DialogControlListBinding

class ControlListDialog: DialogFragment() {

    private var _binding: DialogControlListBinding? = null
    private val binding get() = _binding!!

    var priorityList = PriorityList()

    var onOkClick: (priorityLvl: Int) -> Unit = {
        it
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = DialogControlListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dialog?.setCanceledOnTouchOutside(false)

        if (!priorityList.p1) {
            binding.radioButton.visibility = View.GONE
        }
        if (!priorityList.p2) {
            binding.radioButton2.visibility = View.GONE
        }
        if (!priorityList.p3) {
            binding.radioButton3.visibility = View.GONE
        }
        binding.radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onOkClick(1)
                binding.radioButton.isChecked = false

                this.dismiss()

            }
        }
        binding.radioButton2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onOkClick(2)
                binding.radioButton2.isChecked = false

                this.dismiss()
            }
        }
        binding.radioButton3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onOkClick(3)
                binding.radioButton3.isChecked = false
                this.dismiss()

            }
        }


        return root
    }
}
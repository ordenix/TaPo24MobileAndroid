package pl.tapo24.ui.tariff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.tapo24.databinding.DialogTariffMoreBinding


class DialogTariffMore: DialogFragment() {

    private var _binding:DialogTariffMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = DialogTariffMoreBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dialog?.setCanceledOnTouchOutside(true)


        return root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

}
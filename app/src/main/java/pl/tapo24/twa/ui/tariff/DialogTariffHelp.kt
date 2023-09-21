package pl.tapo24.twa.ui.tariff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.DialogTariffHelpBinding

class DialogTariffHelp: DialogFragment() {
    private var _binding:DialogTariffHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogTariffHelpBinding.inflate(inflater, container,false)
        val root = binding.root
        binding.button9.setOnClickListener {
            this.dismiss()
        }
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
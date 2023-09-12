package pl.tapo24.twa.updater

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.DialogDataUpdaterBinding
import pl.tapo24.twa.databinding.DialogTariffMoreBinding

class DialogDataUpdater: DialogFragment() {


    private var _binding: DialogDataUpdaterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogDataUpdaterBinding.inflate(inflater, container, false)
        val root: View = binding.root





        return root
    }

    fun setProgres(progress: Int) {
        binding.linearProgressIndicator.setProgressCompat(progress, true)
    }

    fun setBody(body: String) {
        binding.description.text = body
    }
    fun setIndeterminate() {
        binding.linearProgressIndicator.isIndeterminate = true

    }

    fun setDone() {
        if (this.isVisible) {
            binding.linearProgressIndicator.setProgressCompat(100, true)
            binding.description.text = getString(R.string.done)
        }



    }

}
package pl.tapo24.twa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.databinding.DialogNotifiicationRequestBinding

class DialogNotificationRequest: DialogFragment() {

    private var _binging: DialogNotifiicationRequestBinding? = null
    private val binding get() = _binging!!

    var skipClick: () -> Unit = {

    }

    var allowClick: () -> Unit = {

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binging = DialogNotifiicationRequestBinding.inflate(inflater, container, false)
        val root = binding.root
        dialog?.setCanceledOnTouchOutside(false)
        binding.allow.setOnClickListener {
            allowClick()
        }
        binding.skip.setOnClickListener {
            skipClick()
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

}
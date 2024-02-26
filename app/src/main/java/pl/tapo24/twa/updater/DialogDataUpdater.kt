package pl.tapo24.twa.updater

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.acra.ACRA
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.DialogDataUpdaterBinding
import pl.tapo24.twa.databinding.DialogTariffMoreBinding

class DialogDataUpdater: DialogFragment() {


    private var _binding: DialogDataUpdaterBinding? = null
    private val binding get() = _binding!!

    val body: MutableLiveData<String> = MutableLiveData()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = DialogDataUpdaterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        body.observe(this, Observer {
            binding.description.text = it
        })




        return root
    }

    fun setBody(body: String) {
        binding.description.text = body
    }

    fun setProgres(progress: Int) {
        binding.linearProgressIndicator.setProgressCompat(progress, true)
    }

//    fun setBody(body: String) {
//        binding.description.text = body
//    }
    fun setIndeterminate() {
        binding.linearProgressIndicator.isIndeterminate = true

    }

    fun setDone() {
        if (this.isVisible) {
            binding.linearProgressIndicator.setProgressCompat(100, true)
            binding.description.text = getString(R.string.done)
        }



    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!this.isAdded && !manager.isStateSaved && !manager.isDestroyed && !this.isStateSaved && !this.isDetached && !this.isRemoving) {
            try {
                super.show(manager, tag)
            } catch (e: IllegalStateException) {
                ACRA.errorReporter.handleSilentException(e)
            }
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (!this.isAdded && !transaction.isEmpty && !this.isStateSaved && !this.isDetached && !this.isRemoving) {
            try  {
                return super.show(transaction, tag)

            } catch (e: IllegalStateException) {
                ACRA.errorReporter.handleSilentException(e)
            }
        }
        return 0

    }
//    override fun onStart() {
//        super.onStart()
//        dialog?.setCanceledOnTouchOutside(false)
//    }

}
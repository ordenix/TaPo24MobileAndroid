package pl.tapo24.twa.ui.tariff

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.DialogTariffMoreBinding
import pl.tapo24.twa.db.entity.Tariff


class DialogTariffMore: DialogFragment() {

    private var _binding:DialogTariffMoreBinding? = null
    private val binding get() = _binding!!
    var item: Tariff? = null
    var position = 0

    var onAddFavClick: () -> Unit = {

    }

    var closeClick: () -> Unit = {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = DialogTariffMoreBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dialog?.setCanceledOnTouchOutside(true)
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            binding.sv2.layoutParams.height = 200
        } else {
            // Portrait
        }
        if (item !=null) {
            binding.textView66.text = item!!.name
            setFav(binding, item!!)
            binding.constraintLayout6.setOnClickListener {
                onAddFavClick()
                setFav(binding, item!!)
            }
        }
        binding.button6.setOnClickListener {
            this.dismiss()
        }

        return root
    }

    fun setFav(binding: DialogTariffMoreBinding, item: Tariff) {
        if (item.favorites) {
            binding.textView60.text = getString(R.string.removeFav)
            binding.imageView14.setImageResource(R.drawable.star_solid)

        } else {
            binding.textView60.text = getString(R.string.addFav)
            binding.imageView14.setImageResource(R.drawable.star_regular)

        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        println("CLOSE")
        closeClick()
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
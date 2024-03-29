package pl.tapo24.twa.ui.tariff

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.R
import pl.tapo24.twa.data.State
import pl.tapo24.twa.databinding.DialogTariffMoreBinding
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.utils.UlListBuilder


class DialogTariffMore: DialogFragment() {

    private var _binding:DialogTariffMoreBinding? = null
    private val binding get() = _binding!!
    var item: Tariff? = null
    var position = 0

    var onAddFavClick: () -> Unit = {

    }

    var closeClick: () -> Unit = {

    }

    var editMapClick: () -> Unit = {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogTariffMoreBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dialog?.setCanceledOnTouchOutside(true)
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            binding.sv2.layoutParams.height = 200
            binding.scrollView2.layoutParams.height= 200
        } else {
            // Portrait
            binding.scrollView2.layoutParams.height= 280
        }
        if (item !=null) {
            //binding  here
            val textCategory = item!!.category?.let { parseCategory(it) }
            binding.textView61.text = "Kategoria: ${textCategory}"


            if (item!!.code != null) {
                binding.codeContainer.visibility = View.VISIBLE
                binding.textView64.text = "${item!!.points} pkt"
                binding.textView65.text = item!!.code

            } else {
                binding.codeContainer.visibility = View.GONE
            }
            binding.textVievText.text = item!!.text
            binding.lawContent.text = UlListBuilder().getSpannableTextListWithoutBullet(item!!.law+"/n"+"/n"+item!!.paragraph, true)
            binding.textView62.text = "${item!!.tax} zł"
            if (item!!.recidive == true) {
                binding.recidive.visibility = View.VISIBLE
                binding.textView63.text = "${item!!.taxRecidive} zł"
            } else {
                binding.recidive.visibility = View.GONE
            }
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
        if (State.premiumVersion) {
            binding.editCategory.visibility = View.VISIBLE
            binding.editCategory.setOnClickListener {
                editMapClick()
            }
        } else {
            binding.editCategory.visibility = View.GONE
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

            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    private fun parseCategory(text: String): String {
        var textToSet = ""
        when (text) {
            "prevention" -> {
                textToSet= "Prewencja"
            }
            "accident" -> {
                textToSet= "Kolizja"
            }
            "pedestrian" -> {
                textToSet= "Wykroczenia pieszych"
            }
            "support" -> {
                textToSet= "Wykroczenia pieszych z urz. wsp. ruch/ rowerzystów"
            }
            "to_pede" -> {
                textToSet= "Wykroczenia wobec pieszych przez poj. mech."
            }
            "non_mech_to_pede" -> {
                textToSet= "Wykroczenia wobec pieszych przez poj. inny niż mech."
            }
            "speed" -> {
                textToSet= "Prędkość pojazdu/ wyprzedzanie"
            }
            "lights" -> {
                textToSet= "Światła zewnętrzne"
            }
            "sign" -> {
                textToSet= "Znaki i sygnały"
            }
            "belts" -> {
                textToSet= "Przewóz osób/ pasy bezp."
            }
            "invalid" -> {
                textToSet= "Karta Parkingowa"
            }
            "stop" -> {
                textToSet= "Postój/Zatrzymanie/ Cofanie/ Zawracanie/ Holowanie"
            }
            "allow" -> {
                textToSet= "Dopuszczenie/ Kierowanie"
            }
            "package" -> {
                textToSet= "Przewóz ładunku/ Tablice/ Przejazd Kolejowy lub tramw."
            }
            "others" -> {
                textToSet= "Pozostałe"
            }
        }
        return textToSet
    }



}
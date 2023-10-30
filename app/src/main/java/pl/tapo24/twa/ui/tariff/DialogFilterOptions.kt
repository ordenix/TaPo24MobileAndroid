package pl.tapo24.twa.ui.tariff

import android.R as r2
import pl.tapo24.twa.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import androidx.fragment.app.DialogFragment
import pl.tapo24.twa.databinding.DialogTariffFilterBinding


class DialogFilterOptions: DialogFragment() {

    private var _binding: DialogTariffFilterBinding? = null
    private val binding get() = _binding!!

    var onSelected: (value: String) -> Unit = {
        it
    }
    var selectedValue: String = "%"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogTariffFilterBinding.inflate(inflater,container,false)
        val root = binding.root

        val options = ArrayList<String>()

        options.add("option 1")
        options.add("option 2")
        options.add("option 3")

        var courses = arrayOf<String?>("C", "Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS")

// use default spinner item to show options in spinner

// use default spinner item to show options in spinner
//        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), r2.layout.simple_spinner_item, courses)
//        adapter.setDropDownViewResource(android.R.layout
//            .simple_spinner_dropdown_item)
//        binding.spinner.adapter = adapter
        var textToSet = ""
        when (selectedValue) {
            "%" -> {
                textToSet= "Wszystkie wykroczenia"
            }
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
        binding.button8.text = textToSet


        val items = listOf("Wszystkie wykroczenia",
            "Prewencja",
            "Kolizja",
            "Wykroczenia pieszych",
            "Wykroczenia pieszych z urz. wsp. ruch/ rowerzystów",
            "Wykroczenia wobec pieszych przez poj. mech.",
            "Wykroczenia wobec pieszych przez poj. inny niż mech.",
            "Prędkość pojazdu/ wyprzedzanie",
            "Światła zewnętrzne",
            "Znaki i sygnały",
            "Przewóz osób/ pasy bezp.",
            "Karta Parkingowa",
            "Postój/Zatrzymanie/ Cofanie/ Zawracanie/ Holowanie",
            "Dopuszczenie/ Kierowanie",
            "Przewóz ładunku/ Tablice/ Przejazd Kolejowy lub tramw.",
            "Pozostałe")
        val listPopupWindow = ListPopupWindow(requireContext(), null, r2.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = binding.button8
        val adapter2 = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
        listPopupWindow.setAdapter(adapter2)

        listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            var value: String = ""
            var text: String = ""
            when (id.toInt()) {
                0 -> {
                    value = "%"
                    text= "Wszystkie wykroczenia"
                }
                1 -> {
                    value = "prevention"
                    text= "Prewencja"
                }
                2 -> {
                    value = "accident"
                    text= "Kolizja"
                }
                3 -> {
                    value = "pedestrian"
                    text= "Wykroczenia pieszych"
                }
                4 -> {
                    value = "support"
                    text= "Wykroczenia pieszych z urz. wsp. ruch/ rowerzystów"
                }
                5 -> {
                    value = "to_pede"
                    text= "Wykroczenia wobec pieszych przez poj. mech."
                }
                6 -> {
                    value = "non_mech_to_pede"
                    text= "Wykroczenia wobec pieszych przez poj. inny niż mech."
                }
                7 -> {
                    value = "speed"
                    text= "Prędkość pojazdu/ wyprzedzanie"
                }
                8 -> {
                    value = "lights"
                    text= "Światła zewnętrzne"
                }
                9 -> {
                    value = "sign"
                    text= "Znaki i sygnały"
                }
                10 -> {
                    value = "belts"
                    text= "Przewóz osób/ pasy bezp."
                }
                11 -> {
                    value = "invalid"
                    text= "Karta Parkingowa"
                }
                12 -> {
                    value = "stop"
                    text= "Postój/Zatrzymanie/ Cofanie/ Zawracanie/ Holowanie"
                }
                13 -> {
                    value = "allow"
                    text= "Dopuszczenie/ Kierowanie"
                }
                14 -> {
                    value = "package"
                    text= "Przewóz ładunku/ Tablice/ Przejazd Kolejowy lub tramw."
                }
                15 -> {
                    value = "others"
                    text= "Pozostałe"
                }
            }
            binding.button8.text = text
            // Dismiss popup.
            listPopupWindow.dismiss()
            onSelected(value)
            dialog?.dismiss()

        }
        binding.button8.setOnClickListener { v: View? -> listPopupWindow.show() }
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
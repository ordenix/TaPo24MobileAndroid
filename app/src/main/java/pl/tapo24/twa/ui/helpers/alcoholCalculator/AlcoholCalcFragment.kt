package pl.tapo24.twa.ui.helpers.alcoholCalculator

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentAlcoholCalcBinding

class AlcoholCalcFragment : Fragment() {

    private var _binding: FragmentAlcoholCalcBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AlcoholCalcViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlcoholCalcBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(AlcoholCalcViewModel::class.java)

        binding.calculate.setOnClickListener {
            val inputManager: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
            val promilStr: String = binding.promil.text.toString().trim()
            val mglStr: String = binding.mgl.text.toString().trim()
            if (promilStr.isEmpty() && mglStr.isEmpty()) {
                Snackbar.make(it, "Wprowadź wartość mg/l lub ‰", Snackbar.LENGTH_LONG).show()
            } else {
                //calculate
                if (promilStr.isEmpty()) {
                    // convert from mg/l to promil
                    val promil = viewModel.calculateFromMgLToPromile(mglStr.toDouble())
                    binding.promil.setText(promil)
                    binding.view13.visibility = View.VISIBLE
                    binding.infoText.visibility = View.VISIBLE
                    if (mglStr.toDouble() >= 0.25) {
                        binding.infoText.text = "Jeśli badany to kierujący pojazdem mechanicznym - przestępstwo art. 178A KK - czynności procesowe"
                    } else {
                        binding.infoText.text = "Jeśli badany to kierujący pojazdem mechanicznym - wykroczenie art. 87 § 1 KW - czynności 54 KPOW"
                    }
                } else {
                    // convert from  promil to mg/l
                    binding.mgl.setText(viewModel.calculateFromPromileToMgL(promilStr.toDouble()))
                    binding.view13.visibility = View.VISIBLE
                    binding.infoText.visibility = View.VISIBLE
                    if (promilStr.toDouble() >= 0.50) {
                        binding.infoText.text = "Jeśli badany to kierujący pojazdem mechanicznym - przestępstwo art. 178A KK - czynności procesowe"
                    } else {
                        binding.infoText.text = "Jeśli badany to kierujący pojazdem mechanicznym - wykroczenie art. 87 § 1 KW - czynności 54 KPOW"
                    }
                }
            }

        }
//        binding.mgl.doOnTextChanged { text, start, before, count ->
//            binding.mgl.setText("")
//        }
//

        binding.mgl.doOnTextChanged { text, start, before, count ->
            val promilStr: String = binding.promil.text.toString().trim()
            val mglStr: String = binding.mgl.text.toString().trim()
            if (promilStr.isNotEmpty()) {
                if (text.toString() !=  viewModel.calculateFromPromileToMgL(promilStr.toDouble())) {
                    binding.promil.setText("")
                }
            }


        }
        binding.promil.doOnTextChanged { text, start, before, count ->
            val promilStr: String = binding.promil.text.toString().trim()
            val mglStr: String = binding.mgl.text.toString().trim()
            if (mglStr.isNotEmpty()) {
                if (text.toString() !=  viewModel.calculateFromMgLToPromile(mglStr.toDouble())) {
                    binding.mgl.setText("")
                }
            }


        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
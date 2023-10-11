package pl.tapo24.twa.ui.helpers.speedCalc

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentSpeedCalcBinding
import pl.tapo24.twa.ui.tariff.DialogTariffMore

@AndroidEntryPoint
class SpeedCalcFragment : Fragment() {

    private var _binding: FragmentSpeedCalcBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SpeedCalcViewModel
    private val dialogMore = DialogTariffMore()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpeedCalcBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(SpeedCalcViewModel::class.java)
        binding.speed.doOnTextChanged { text, start, before, count ->
            binding.tariffInfo.visibility = View.GONE
        }
        binding.speedLimit.doOnTextChanged { text, start, before, count ->
            binding.tariffInfo.visibility = View.GONE
        }
        viewModel.tariffDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.tariffInfo.visibility = View.VISIBLE

                binding.textView62.text = "${it.tax} zł"
                if (it.recidive == true) {
                    binding.recidive.visibility = View.VISIBLE
                    binding.textView63.text = "${it.taxRecidive} zł"
                } else {
                    binding.recidive.visibility = View.GONE
                }
                binding.tariffName.text = it.name
                binding.tariffText.text = it.text

                if (it.code != null) {
                    binding.codeContainer.visibility = View.VISIBLE
                    binding.textView64.text = "${it.points} pkt"
                    binding.textView65.text = it.code

                } else {
                    binding.codeContainer.visibility = View.GONE
                }

            } else {
                binding.tariffInfo.visibility = View.GONE

            }
        })
        binding.button7.setOnClickListener {
            showDialog()
        }

        binding.calculate.setOnClickListener {
            binding.tariffInfo.visibility = View.GONE
            val speedStr :String = binding.speed.text.toString().trim()
            val speedLimitStr : String = binding.speedLimit.text.toString().trim()
            val inputManager: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)

            if (speedStr.isEmpty()) {
                Snackbar.make(it, "Wprowadź prędkość", Snackbar.LENGTH_LONG).show()
            } else if (speedLimitStr.isEmpty()) {
                Snackbar.make(it, "Wprowadź ograniczenie prędkości", Snackbar.LENGTH_LONG).show()

            } else {
                val speed: Int =speedStr.toInt()
                val speedLimit : Int = speedLimitStr.toInt()

                if (speed <= speedLimit) {
                    Snackbar.make(it, "Nie doszło do przekroczenia prędkości", Snackbar.LENGTH_LONG).show()
                } else {
                    val diff: Int = speed - speedLimit
                    viewModel.calculateTariffForSpeedOvf(diff)
                    if (diff > 2000) {
                        Snackbar.make(it, "Naczelnik będzie zadowolony", Snackbar.LENGTH_LONG).show()

                    } else if (diff > 1000) {
                        Snackbar.make(it, "Nawet Ania BMW tyle nie pojedzie", Snackbar.LENGTH_LONG).show()

                    } else if (diff > 500) {
                        Snackbar.make(it, "Chyba coś źle zmierzono", Snackbar.LENGTH_LONG).show()

                    }

                }
            }

        }
        dialogMore.onAddFavClick = {
            viewModel.tariffDetail.value?.let { viewModel.clickOnFavorites(it) }
        }

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        if (dialogMore.isVisible) {
            dialogMore.dismiss()
        }
        super.onPause()

    }

    private fun showDialog() {
        dialogMore.item = viewModel.tariffDetail.value
        dialogMore.show(childFragmentManager, "More")
    }
}
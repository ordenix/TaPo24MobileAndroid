package pl.tapo24.twa.ui.helpers.postalCode

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.PostalCodeSequenceAdapter
import pl.tapo24.twa.databinding.FragmentPostalCodeBinding


@AndroidEntryPoint
class PostalCodeFragment : Fragment() {

    private var _binding: FragmentPostalCodeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PostalCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostalCodeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PostalCodeViewModel::class.java)
        val root = binding.root



        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = PostalCodeSequenceAdapter(viewModel.responseCodeSequence.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.responseCodeSequence.observe(viewLifecycleOwner, Observer {

            viewModel.adapter.items = it.orEmpty()
            viewModel.adapter.notifyDataSetChanged()


        })


        binding.toggleButton.check(R.id.simple)
        binding.simple.setOnClickListener {
            binding.toggleButton.check(R.id.simple)
            binding.toggleButton.uncheck(R.id.advenced)
            clearData()
        }
        binding.advenced.setOnClickListener {
            binding.toggleButton.uncheck(R.id.simple)
            binding.toggleButton.check(R.id.advenced)
            clearData()
        }

        binding.searchPostal.setOnClickListener {
            clearData()
            val inputManager: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
            if (viewModel.busy.value == true) {
                showDialog(getString(R.string.wait_for_response))
            } else {
                if (binding.postalCode.text.isNullOrEmpty() && binding.postalCity.text.isNullOrEmpty()) {
                    showDialog(getString(R.string.enter_city_or_code))
                } else {
                    // search
                    if (binding.postalCode.text.isNullOrEmpty()) {
                        // search city get sequence
                        viewModel.getCodeSequenceByCity(binding.postalCity.text.toString())
                    }
                    if (binding.postalCity.text.isNullOrEmpty()) {
                        // search code to get city
                        val code = binding.postalCode.text.toString()
                        if(viewModel.validCode(code)) {
                            // valid ok
                            viewModel.getCityByCode(code)
                        } else {
                            showDialog(getString(R.string.enter_valid_postal_code))

                        }
                    }

                }
            }

        }

        binding.postalCode.doOnTextChanged { text, start, before, count ->

            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.postalCity.setText("")
                    clearData()
                }

            }

        }

        binding.postalCity.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.postalCode.setText("")
                    clearData()
                }

            }

        }

        viewModel.showError.observe(viewLifecycleOwner , Observer {
            if (it) {
                viewModel.showError.value = false
                showDialog(viewModel.errorMessage)
            }
        })
        binding.postalCityWidnow.visibility = View.GONE
        viewModel.responseCity.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.postalCityWidnow.visibility = View.VISIBLE
                binding.tvGm.text = it.r[0].gmina
                binding.tvMsc.text = it.r[0].miejscowosc
                binding.tvPow.text = it.r[0].powiat
                binding.tvWoj.text = it.r[0].wojewodztwo
            } else {
                binding.postalCityWidnow.visibility = View.GONE
            }
        })


        return root
    }
    private fun clearData() {
        viewModel.responseCity.value = null
        viewModel.responseCodeSequence.value = null
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.error))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                // Respond to neutral button press
                dialog.dismiss()
            }
            .show()
    }

}
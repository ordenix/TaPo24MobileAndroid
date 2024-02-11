package pl.tapo24.twa.ui.home.update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.data.NetworkTypes
import pl.tapo24.twa.databinding.FragmentHomeBinding
import pl.tapo24.twa.databinding.FragmentUpdaterBinding
import pl.tapo24.twa.ui.home.HomeFragmentDirections
import pl.tapo24.twa.utils.CheckConnection

@AndroidEntryPoint
class UpdaterFragment : Fragment() {

    private var _binding: FragmentUpdaterBinding? = null //WiFi All

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: UpdaterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdaterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(UpdaterViewModel::class.java)
        binding.cell.setOnClickListener {
            viewModel.choseConnection.value = "All"
        }
        binding.wifi.setOnClickListener {
            viewModel.choseConnection.value = "WiFi"
        }
        viewModel.choseConnection.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty() && !viewModel.startDownloadProcedure) {
                binding.mainInfo.visibility = View.GONE
                binding.workInfo.visibility = View.VISIBLE
                when (CheckConnection().getConnectionType(requireContext())) {
                    NetworkTypes.None -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Błąd")
                            .setMessage("Aby pobrać dane musisz być połączony z internetem!")
                            .setPositiveButton("Ok") { _, _ ->
                                binding.errorDataEmpty.visibility = View.VISIBLE
                                binding.workInfo.visibility = View.GONE
                            }
                            .show()
                    }
                    NetworkTypes.WiFi -> {
                        viewModel.startDownload()
                    }
                    NetworkTypes.Vpn -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Błąd")
                            .setMessage("Aby pobrać dane musisz być połączony z internetem!")
                            .setPositiveButton("Ok") { _, _ ->
                                binding.errorDataEmpty.visibility = View.VISIBLE
                                binding.workInfo.visibility = View.GONE
                            }
                            .show()
                    }
                    NetworkTypes.Mobile -> {
                        if (it == "WiFi") {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Błąd")
                                .setMessage("Aby pobrać dane połącz się z siecią WiFi z uwagi na wybrany wcześniej rodzaj połączenia!")
                                .setPositiveButton("Ok") { _, _ ->
                                    binding.errorDataEmpty.visibility = View.VISIBLE
                                    binding.workInfo.visibility = View.GONE
                                }
                                .show()
                        } else {
                            viewModel.startDownload()
                        }

                    }
                }



            } else if (it.isNotEmpty()) {
                binding.mainInfo.visibility = View.GONE
                binding.workInfo.visibility = View.VISIBLE
            }
        })
        binding.backToHome.setOnClickListener {
            view?.findNavController()?.navigate(R.id.nav_tariff)
        }
        viewModel.showError.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.showError.value = true
                binding.errorDataEmpty.visibility = View.VISIBLE
                binding.workInfo.visibility = View.GONE

            }
        })

        viewModel.showPausedDialog.observe(viewLifecycleOwner, Observer {
            if(it) {
                viewModel.showPausedDialog.value = false
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Błąd")
                    .setMessage("Przerwano pobieranie! Spróbuj ponownie później!")
                    .setPositiveButton("Ok") { _, _ ->
                        binding.errorDataEmpty.visibility = View.VISIBLE
                        binding.workInfo.visibility = View.GONE
                    }
                    .show()
            }
        })
        viewModel.showSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.showSuccess.value = false
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Sukces")
                    .setMessage("Poprawnie pobrano dane!")
                    .setPositiveButton("Ok") { _, _ ->
                        view?.findNavController()?.navigate(R.id.nav_home)
                    }
                    .show()
            }
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
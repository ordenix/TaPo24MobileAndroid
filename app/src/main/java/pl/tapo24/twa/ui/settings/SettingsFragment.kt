package pl.tapo24.twa.ui.settings

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.adapter.SettingFontAdapter
import pl.tapo24.twa.adapter.SettingThemeAdapter
import pl.tapo24.twa.data.*
import pl.tapo24.twa.databinding.FragmentSettingBinding

@AndroidEntryPoint
class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var clickedOnInfo = false
    private var clickedOnDevInfo = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingsViewModel.environment.observe(viewLifecycleOwner) {
            when (it) {
                EnvironmentType.Master -> binding.radioGroupEnviroment.check(binding.radioButtonProduction.id)
                EnvironmentType.Beta -> binding.radioGroupEnviroment.check(binding.radioButtonTest.id)
                EnvironmentType.Feature -> binding.radioGroupEnviroment.check(binding.radioButtonFeature.id)
            }
        }
        settingsViewModel.engine.observe(viewLifecycleOwner) {
            when (it) {
                EnginesType.New -> binding.radioGroupEngine.check(binding.radioButtonAfter.id)
                EnginesType.Old -> binding.radioGroupEngine.check(binding.radioButtonBefore.id)
            }
        }
        settingsViewModel.connectionType.observe(viewLifecycleOwner) {
            if (it == "All") {
                binding.radioGroupConnectionType.check(binding.radioButtonCell.id)
            }
            if (it == "WiFi") {
                binding.radioGroupConnectionType.check(binding.radioButtonWifi.id)

            }
        }
        binding.radioButtonCell.setOnClickListener {
            //binding.radioButtonWifi.isChecked = true
            settingsViewModel.connectionType.value = "All"
            State.networkType = "All"
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono rodzaj połączenia, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonWifi.setOnClickListener {
            //binding.radioButtonWifi.isChecked = true
            settingsViewModel.connectionType.value = "WiFi"
            State.networkType = "WiFi"
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono rodzaj połączenia, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }

        binding.radioButtonBefore.setOnClickListener {
            binding.radioButtonBefore.isChecked = true
            settingsViewModel.engine.value = EnginesType.Old
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono silnik, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonAfter.setOnClickListener {
            binding.radioButtonAfter.isChecked = true
            settingsViewModel.engine.value = EnginesType.New
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono silnik, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }

        binding.radioButtonProduction.setOnClickListener {
            binding.radioButtonProduction.isChecked = true
            settingsViewModel.environment.value = EnvironmentType.Master
            settingsViewModel.saveSettings()
            settingsViewModel.getData()
            Snackbar.make(it, "Zmieniono środowisko, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonFeature.setOnClickListener {
            binding.radioButtonFeature.isChecked = true
            settingsViewModel.environment.value = EnvironmentType.Feature
            settingsViewModel.saveSettings()
            settingsViewModel.getData()
            Snackbar.make(it, "Zmieniono środowisko, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonTest.setOnClickListener {
            binding.radioButtonTest.isChecked = true
            settingsViewModel.environment.value = EnvironmentType.Beta
            settingsViewModel.saveSettings()
            settingsViewModel.getData()
            Snackbar.make(it, "Zmieniono środowisko, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.tvDevelop.setOnClickListener {
            if (!clickedOnInfo) {
                clickedOnDevInfo = true
            }

         }
        binding.tvInfo.setOnClickListener {
            clickedOnInfo = true
        }
        binding.tvEnvir.setOnClickListener {
            if (clickedOnDevInfo && clickedOnInfo) {
                binding.radioButtonTest.isEnabled = true
                binding.radioButtonFeature.isEnabled = true
                Snackbar.make(it, "Gratulacje możesz dołączyć do zespołu Tapo24.pl ;)", Snackbar.LENGTH_LONG).show()
            }
        }

        settingsViewModel.settingFontBoldMain.observe(viewLifecycleOwner) {
            binding.boldMain.isChecked = it
        }
        settingsViewModel.settingFontBoldTariff.observe(viewLifecycleOwner) {
            binding.boldTariff.isChecked = it
        }
        var premiumVisible = State.premiumVersion
        if (State.premiumVersion) {
            binding.textView102.visibility = View.GONE
        } else {
            binding.textView102.visibility = View.VISIBLE
        }
        if (State.funeralTheme) {
            premiumVisible = false
            binding.recyclerView.visibility = View.GONE
            binding.textInfoForceStyle.visibility = View.VISIBLE
        } else {
            binding.textInfoForceStyle.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
        binding.boldTariff.isEnabled = premiumVisible
        binding.boldMain.isEnabled = premiumVisible



        binding.boldTariff.setOnClickListener {
            settingsViewModel.settingFontBoldTariff.value = !settingsViewModel.settingFontBoldTariff.value!!
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono pogrubienie tekstów taryfikatora, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }

        binding.boldMain.setOnClickListener {
            settingsViewModel.settingFontBoldMain.value = !settingsViewModel.settingFontBoldMain.value!!
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono pogrubienie tekstów głównych, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        ///


        // 270
        val rv = binding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity)
        settingsViewModel.adapter = SettingThemeAdapter(settingsViewModel.themeTypesDataList.value.orEmpty(),settingsViewModel)
        rv.adapter = settingsViewModel.adapter
//
        settingsViewModel.themeTypesDataList.observe(viewLifecycleOwner, Observer {
            settingsViewModel.adapter.items = it
            settingsViewModel.adapter.notifyDataSetChanged()
        })
        // 220
        val rv2 = binding.recyclerView2
        rv2.layoutManager = LinearLayoutManager(activity)
        settingsViewModel.adapter2 = SettingFontAdapter(settingsViewModel.fontTypesDataList.value.orEmpty(),settingsViewModel, requireContext())
        rv2.adapter = settingsViewModel.adapter2
        settingsViewModel.fontTypesDataList.observe(viewLifecycleOwner, Observer {
            settingsViewModel.adapter2.items = it
            settingsViewModel.adapter2.notifyDataSetChanged()
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
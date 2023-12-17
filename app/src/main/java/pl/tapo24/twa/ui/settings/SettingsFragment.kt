package pl.tapo24.twa.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.data.*
import pl.tapo24.twa.databinding.FragmentSettingBinding

@AndroidEntryPoint
class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var context2: Context? = null
    private var clickedOnInfo = false
    private var clickedOnDevInfo = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        context2 = container?.context

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
            context2?.let { it1 -> settingsViewModel.getData(it1) }
            Snackbar.make(it, "Zmieniono środowisko, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonFeature.setOnClickListener {
            binding.radioButtonFeature.isChecked = true
            settingsViewModel.environment.value = EnvironmentType.Feature
            settingsViewModel.saveSettings()
            context2?.let { it1 -> settingsViewModel.getData(it1) }
            Snackbar.make(it, "Zmieniono środowisko, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonTest.setOnClickListener {
            binding.radioButtonTest.isChecked = true
            settingsViewModel.environment.value = EnvironmentType.Beta
            settingsViewModel.saveSettings()
            context2?.let { it1 -> settingsViewModel.getData(it1) }
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

        settingsViewModel.fontType.observe(viewLifecycleOwner) {
            when (it) {
                FontTypes.itim -> binding.radioGroupFontTypes.check(binding.radioButtonItim.id)
                FontTypes.sansSerifLight -> binding.radioGroupFontTypes.check(binding.radioButtonSansSerifLight.id)
                FontTypes.arimo -> binding.radioGroupFontTypes.check(binding.radioButtonArmino.id)
                FontTypes.armata -> binding.radioGroupFontTypes.check(binding.radioButtonArmata.id)
                FontTypes.alegreya -> binding.radioGroupFontTypes.check(binding.radioButtonAlegreya.id)
            }
        }
        settingsViewModel.settingFontBoldMain.observe(viewLifecycleOwner) {
            binding.boldMain.isChecked = it
        }
        settingsViewModel.settingFontBoldTariff.observe(viewLifecycleOwner) {
            binding.boldTariff.isChecked = it
        }
        if (State.premiumVersion) {
            binding.radioButtonArmino.isEnabled = true
            binding.radioButtonAlegreya.isEnabled = true
            binding.radioButtonArmata.isEnabled = true

            binding.radioBlueIce.isEnabled = true
            binding.radioBlueMint.isEnabled = true
            binding.radioPink.isEnabled = true
            binding.radioGreenMint.isEnabled = true
            binding.radioDesert.isEnabled = true
            binding.textView102.visibility = View.GONE
        } else {
            binding.radioButtonArmino.isEnabled = false
            binding.radioButtonAlegreya.isEnabled = false
            binding.radioButtonArmata.isEnabled = false

            binding.radioBlueIce.isEnabled = false
            binding.radioBlueMint.isEnabled = false
            binding.radioPink.isEnabled = false
            binding.radioGreenMint.isEnabled = false
            binding.radioDesert.isEnabled = false
            binding.textView102.visibility = View.VISIBLE
        }

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
        binding.radioButtonItim.setOnClickListener {
            binding.radioButtonItim.isChecked = true
            settingsViewModel.fontType.value = FontTypes.itim
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono czcionki, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonArmino.setOnClickListener {
            binding.radioButtonArmino.isChecked = true
            settingsViewModel.fontType.value = FontTypes.arimo
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono czcionki, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonSansSerifLight.setOnClickListener {
            binding.radioButtonSansSerifLight.isChecked = true
            settingsViewModel.fontType.value = FontTypes.sansSerifLight
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono czcionki, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonAlegreya.setOnClickListener {
            binding.radioButtonAlegreya.isChecked = true
            settingsViewModel.fontType.value = FontTypes.alegreya
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono czcionki, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        binding.radioButtonArmata.setOnClickListener {
            binding.radioButtonArmata.isChecked = true
            settingsViewModel.fontType.value = FontTypes.armata
            settingsViewModel.saveSettings()
            Snackbar.make(it, "Zmieniono czcionki, ustawienia zapisano", Snackbar.LENGTH_LONG).show()
        }
        ///
        binding.radioDefault.setOnClickListener {
            binding.radioDefault.isChecked = true
            State.settingTheme.value = ThemeTypes.Default

        }
        binding.radioGray.setOnClickListener {
            binding.radioGray.isChecked = true
            State.settingTheme.value = ThemeTypes.GrayTheme

        }
        binding.radioBlueMint.setOnClickListener {
            binding.radioBlueMint.isChecked = true
            State.settingTheme.value = ThemeTypes.BlueMintTheme

        }
        binding.radioPink.setOnClickListener {
            binding.radioPink.isChecked = true
            State.settingTheme.value = ThemeTypes.PinkTheme

        }
        binding.radioBlueIce.setOnClickListener {
            binding.radioBlueIce.isChecked = true
            State.settingTheme.value = ThemeTypes.BlueIceTheme

        }
        binding.radioGreenMint.setOnClickListener {
            binding.radioGreenMint.isChecked = true
            State.settingTheme.value = ThemeTypes.GreenMintTheme

        }
        binding.radioDesert.setOnClickListener {
            binding.radioDesert.isChecked = true
            State.settingTheme.value = ThemeTypes.DesertTheme

        }




        settingsViewModel.themeType.observe(viewLifecycleOwner) {
            when (it) {
                ThemeTypes.Default -> binding.radioGroupTheme.check(binding.radioDefault.id)
                ThemeTypes.GrayTheme -> binding.radioGroupTheme.check(binding.radioGray.id)
                ThemeTypes.BlueMintTheme -> binding.radioGroupTheme.check(binding.radioBlueMint.id)
                ThemeTypes.PinkTheme -> binding.radioGroupTheme.check(binding.radioPink.id)
                ThemeTypes.BlueIceTheme -> binding.radioGroupTheme.check(binding.radioBlueIce.id)
                ThemeTypes.GreenMintTheme -> binding.radioGroupTheme.check(binding.radioGreenMint.id)
                ThemeTypes.DesertTheme -> binding.radioGroupTheme.check(binding.radioDesert.id)
                ThemeTypes.FuneralTheme -> binding.radioGroupTheme.check(binding.radioDefault.id)

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
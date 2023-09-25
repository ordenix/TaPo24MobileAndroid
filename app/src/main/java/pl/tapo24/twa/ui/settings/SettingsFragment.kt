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
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.EnvironmentType
import pl.tapo24.twa.databinding.FragmentSettingBinding

@AndroidEntryPoint
class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var context2: Context? = null


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


//        val textView: TextView = binding.textViewSetting
//        settingsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
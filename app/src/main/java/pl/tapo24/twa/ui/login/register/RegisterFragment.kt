package pl.tapo24.twa.ui.login.register

import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.data.register.RegisterForm
import pl.tapo24.twa.databinding.FragmentRegisterBinding
import pl.tapo24.twa.utils.PdfOpenIntent


@AndroidEntryPoint
class RegisterFragment: Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var viewModel: RegisterViewModel
    private val validateLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    private val validateEmail: MutableLiveData<Boolean> = MutableLiveData(false)
    private val validatePassword: MutableLiveData<Boolean> = MutableLiveData(false)
    private val validateRePassword: MutableLiveData<Boolean> = MutableLiveData(false)
    private val validateStatute: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onDestroy() {
        super.onDestroy()
        val a = requireActivity()
        a.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Proszę czekać")
            .setMessage("Serwer pracuje nad nowym radarem")
             .create()
        
            viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val a = requireActivity()
        a.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//        val currentOrientation = resources.configuration.orientation
//        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            val params: LinearLayout.LayoutParams =
//                LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
//            params.setMargins(0, 0, 0, 150)
//            binding.cl.layoutParams = params
//
//        } else {
//            val params: LinearLayout.LayoutParams =
//                LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
//            params.setMargins(0, 0, 0, 150)
//            binding.cl.layoutParams = params
//        }



//        val textView: TextView = binding.textViewLogin
//        loginViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        // login
        binding.inputLogin.doOnTextChanged { text, start, before, count ->
            val textValidate = validateLogin(text.toString())
            if (textValidate != null) {
                validateLogin.value = false
                validator(false,textValidate,binding.formLogin,binding.inputLoginDesc,binding.inputLoginDescIco)
            }
//            else {
//                validateLogin.value = true
//                validator(true,"Login poprawny",binding.formLogin,binding.inputLoginDesc,binding.inputLoginDescIco)
//            }
        }
        viewModel.validateLogin.observe(viewLifecycleOwner, Observer { it1 ->
            if (it1 != null) {
                it1.onSuccess {
                    validateLogin.value = true
                    validator(true,it,binding.formLogin,binding.inputLoginDesc,binding.inputLoginDescIco)
                }
                it1.onFailure {
                    validateLogin.value = false
                    validator(false,it.message!!,binding.formLogin,binding.inputLoginDesc,binding.inputLoginDescIco)
                }
            }
        })



        validateLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.formEmail.visibility = View.VISIBLE
            } else {
                binding.formEmail.visibility = View.GONE
            }
        })
        // email

        binding.inputEmail.doOnTextChanged { text, start, before, count ->
            val textValidate = validateEmail(text.toString())
            if (textValidate != null) {
                validateEmail.value = false
                validator(false,textValidate,binding.formEmail,binding.inputEmailDesc,binding.inputEmailDescIco)
            }
//            else {
//                validateEmail.value = true
//                validator(true,"Email poprawny",binding.formEmail,binding.inputEmailDesc,binding.inputEmailDescIco)
//            }
        }

        viewModel.validateEmail.observe(viewLifecycleOwner, Observer { it1 ->
            if (it1 != null) {
                it1.onSuccess {
                    validateEmail.value = true
                    validator(true,it,binding.formEmail,binding.inputEmailDesc,binding.inputEmailDescIco)
                }
                it1.onFailure {
                    validateEmail.value = false
                    validator(false,it.message!!,binding.formEmail,binding.inputEmailDesc,binding.inputEmailDescIco)
                }
            }
        })

        validateEmail.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.formPassword.visibility = View.VISIBLE
                //binding.formLogin.visibility = View.GONE

            } else {
                binding.formPassword.visibility = View.GONE
                binding.formLogin.visibility = View.VISIBLE
            }
        })
        // password
        binding.inputPassword.doOnTextChanged { text, start, before, count ->
            val textValidate = validatePassword(text.toString())
            if (textValidate != null) {
                validatePassword.value = false
                validator(false,textValidate,binding.formPassword,binding.inputPasswordDesc,binding.inputPasswordDescIco)
            } else {
                validatePassword.value = true
                validator(true,"Hasło poprawne",binding.formPassword,binding.inputPasswordDesc,binding.inputPasswordDescIco)
            }
        }

        validatePassword.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.formRePassword.visibility = View.VISIBLE
               // binding.formEmail.visibility = View.GONE
                binding.inputRePasswordDesc.visibility = View.VISIBLE
                binding.inputRePasswordDescIco.visibility = View.VISIBLE

            } else {
                binding.formRePassword.visibility = View.GONE
                if (validateLogin.value == true) {
                    binding.formEmail.visibility = View.VISIBLE
                }

                binding.inputRePasswordDesc.visibility = View.GONE
                binding.inputRePasswordDescIco.visibility = View.GONE
            }
        })
        // repassword
        binding.inputRePassword.doOnTextChanged { text, start, before, count ->
            if (text.toString() == binding.inputPassword.text.toString()) {
                // ok
                validateRePassword.value = true
                validator(true,"Hasła poprawne",binding.formRePassword,binding.inputRePasswordDesc,binding.inputRePasswordDescIco)
            } else {
                validateRePassword.value = false
                validator(false,"Hasła się różnią",binding.formRePassword,binding.inputRePasswordDesc,binding.inputRePasswordDescIco)
            }
        }

        validateRePassword.observe(viewLifecycleOwner, Observer {
            if (it) {
               // binding.formPassword.visibility = View.GONE
                binding.sv.visibility = View.VISIBLE
                binding.allowAll.visibility = View.VISIBLE
                binding.info.visibility = View.VISIBLE

            } else {
                if (validatePassword.value == true) {
                    binding.formPassword.visibility = View.VISIBLE

                }
                binding.sv.visibility = View.GONE
                binding.allowAll.visibility = View.GONE
                binding.info.visibility = View.GONE


            }
        })

        // bind pdf
        binding.buttonPolicy.setOnClickListener {
            PdfOpenIntent(requireContext()).openPDF("POLITYKA.pdf", viewModel.isPublicStorage)
        }
        binding.buttonStatute.setOnClickListener {
            PdfOpenIntent(requireContext()).openPDF("Regulamin.pdf", viewModel.isPublicStorage)
        }
        binding.allowAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkMarket.isChecked = true
                binding.checkStautePolicy.isChecked = true
            }
        }
        binding.checkMarket.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                binding.allowAll.isChecked = false
            }
        }
        binding.checkStautePolicy.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                validateStatute.value = true
            } else {
                binding.allowAll.isChecked = false
                validateStatute.value = false
            }
        }
        validateStatute.observe(viewLifecycleOwner, Observer {
            binding.register.isEnabled = it
        })
        binding.register.setOnClickListener {
            if (
                validatePassword.value == true &&
                validateRePassword.value == true &&
                validateLogin.value == true &&
                validateEmail.value == true &&
                validateStatute.value == true) {
                // register !
            }
            val dataToSend = RegisterForm(
                acceptAdv = binding.checkMarket.isChecked,
                acceptRules = binding.checkStautePolicy.isChecked,
                email =  binding.inputEmail.text.toString(),
                login = binding.inputLogin.text.toString(),
                password = binding.inputPassword.text.toString()

            )
            viewModel.registerUser(dataToSend)
            binding.sv1.visibility = View.GONE
            dialog.show()

        }
        viewModel.statusRegister.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
            it.onSuccess {
                findNavController().navigate(
                    R.id.action_nav_register_to_nav_success,
                    bundleOf("successDesc" to getString(R.string.succesRegister))
                )
            }
            it.onFailure {e ->
                findNavController().navigate(
                    R.id.action_nav_register_to_nav_error,
                    bundleOf("errorDesc" to e.message)
                )
            }
        })


        return root
    }

    private fun validatePassword(password: String):String? {
        val regex = Regex(pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[\\S\\d@$!%*?&]{8,}$")
        if (!regex.matches(password)) {
            return "Hasło musi mieć minimalną długość 8 znaków oraz zawierać 1 dużą i małą literę, liczbę, znak specjalny"
        } else {
            return null
        }
    }
    private fun validateEmail(email: String): String? {
        val regex = Regex(pattern = "^[^@]+@[^@]+\\.([^@.]{2,})\$" )
        if (!regex.matches(email)) {
            return "Błędny adres e-mail"
        }
        viewModel.existEmailInService(email)

        return null
    }
    private fun validateLogin(login: String): String? {
        if (login.length < 5) {
            return "Login powinien mieć conajmniej 5 znaków"
        }
        val regex = Regex(pattern = "[A-Za-z0-9]+")
        if (!regex.matches(login)) {
            return "Login powinien składać się z znaków a-z oraz cyfr 0-9"
        }
        viewModel.existLoginInService(login)
        return null
    }

    private fun validator(state: Boolean?, desc: String,
                          input: TextInputLayout, tv: TextView, img: ImageView) {
        if (state == null) {
            // gone
            tv.visibility = View.GONE
            img.visibility = View.GONE
            input.error = null
        } else if (state) {
            // ok
            img.setImageResource(resources.getIdentifier("check_solid","drawable","pl.tapo24.twa"))

            tv.visibility = View.VISIBLE
            img.visibility = View.VISIBLE
            input.error = null
            val color = requireContext().getColor(R.color.standard_green)
            img.imageTintList = ColorStateList.valueOf(color)
            tv.setTextColor(ColorStateList.valueOf(color))

        } else {
            // error
            img.setImageResource(resources.getIdentifier("triangle_exclamation_solid","drawable","pl.tapo24.twa"))
            tv.visibility = View.VISIBLE
            img.visibility = View.VISIBLE
            input.error = " "
            val color = requireContext().getColor(R.color.standard_red)
            img.imageTintList = ColorStateList.valueOf(color)
            tv.setTextColor(ColorStateList.valueOf(color))
        }
        tv.text = desc
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        val a = requireActivity()
        a.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
    }
}
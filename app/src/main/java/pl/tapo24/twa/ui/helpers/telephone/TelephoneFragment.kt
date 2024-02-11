package pl.tapo24.twa.ui.helpers.telephone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.TelephoneAdapter
import pl.tapo24.twa.data.State
import pl.tapo24.twa.databinding.FragmentTelephoneBinding

@AndroidEntryPoint
class TelephoneFragment : Fragment() {
    private var _binding: FragmentTelephoneBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = TelephoneFragment()
    }

    private lateinit var viewModel: TelephoneViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTelephoneBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(TelephoneViewModel::class.java)

        val args = arguments?.let { TelephoneFragmentArgs.fromBundle(it) }

        if (args?.bookmark != null) {
            viewModel.bookmarkClicked = args.bookmark
            binding.grid.visibility = View.GONE
            binding.rv.visibility = View.VISIBLE

        } else {
            binding.grid.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE
        }

        viewModel.getTelephoneList()

        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = TelephoneAdapter(viewModel.telephoneList.value.orEmpty(),viewModel.telephoneList.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.telephoneList.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResult = it
            viewModel.adapter.notifyDataSetChanged()
        })
        binding.queryText.addTextChangedListener {
            if (args?.bookmark == null) {
                if (!it.isNullOrEmpty()) {
                    binding.grid.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                } else {
                    binding.grid.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                }
            }
            viewModel.searchText = binding.queryText.text.toString()
            viewModel.adapter.filter.filter(binding.queryText.text)
        }
        binding.rv.setOnTouchListener { v, event ->
            val inputManager: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
            false
        }

        viewModel.selectedNumber.observe(viewLifecycleOwner, Observer {
            if (it != null ) {
                viewModel.selectedNumber.value = null
                if (State.premiumVersion) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Info")
                        .setMessage("Czy chcesz zadzwonić na numer: $it?")
                        .setPositiveButton("Ok") { _, _ ->
                            val phone = it
                            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                            startActivity(intent)
                        }
                        .setNegativeButton("Anuluj") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Info")
                        .setMessage("Aby skorzystać z funkcji przekierowania nr musisz posiadać wersję premium aplikacji.")
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }

            }


        })

        binding.linkTelephone.setOnClickListener {
            openLink("https://ckt.uc.ost112.gov.pl/")
        }

        binding.itd.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_nav_telephone_self,
                Bundle().apply {
                    putString("bookmark", "ITD")
                }
            )

        }
        binding.sg.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_nav_telephone_self,
                Bundle().apply {
                    putString("bookmark", "SG")
                }
            )
        }
        binding.others.setOnClickListener {
            view?.findNavController()?.navigate(
                R.id.action_nav_telephone_self,
                Bundle().apply {
                    putString("bookmark", "OTHERS")
                }
            )
        }



        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

}
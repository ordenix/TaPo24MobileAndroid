package pl.tapo24.twa.ui.road.immunity.immunityList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentImmunityListBinding
import pl.tapo24.twa.ui.story.StoryFragmentArgs

class ImmunityListFragment : Fragment() {

    companion object {
        fun newInstance() = ImmunityListFragment()
    }
    private var _binding: FragmentImmunityListBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ImmunityListViewModel
    var title: String = ""
    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { ImmunityListFragmentArgs.fromBundle(it) }
        title = args!!.immunity.person!!

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ImmunityListViewModel::class.java)
        _binding = FragmentImmunityListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val args = arguments?.let { ImmunityListFragmentArgs.fromBundle(it) }
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        viewModel.immunity = args!!.immunity
        binding.person.text = viewModel.immunity.person
        if (viewModel.immunity.type == "allowTicket") {
            // allowTicket
            binding.preventTicket.visibility = View.GONE
            binding.allowTicket1.visibility = View.VISIBLE
            binding.allowTicket2.visibility = View.VISIBLE
            binding.viewAllowTicket.visibility = View.VISIBLE
            binding.instruction.setOnClickListener {
                navigateToImmunityView(getString(R.string.instruction41), viewModel.immunity.p1!!)
            }
            binding.ticket.setOnClickListener {
                navigateToImmunityView(getString(R.string.ticket), viewModel.immunity.p2!!)

            }
            binding.court.setOnClickListener {
                navigateToImmunityView(getString(R.string.courtProcedure), viewModel.immunity.p3!!)

            }
            binding.instructionOther.setOnClickListener {
                navigateToImmunityView(getString(R.string.instruction41), viewModel.immunity.p4!!)

            }
            binding.courtOther.setOnClickListener {
                navigateToImmunityView(getString(R.string.courtProcedure2), viewModel.immunity.p5!!)

            }

        } else {
            // preventTicket
            binding.preventTicket.visibility = View.VISIBLE
            binding.allowTicket1.visibility = View.GONE
            binding.allowTicket2.visibility = View.GONE
            binding.viewAllowTicket.visibility = View.GONE
            binding.instructionPreventTicket.setOnClickListener {
                navigateToImmunityView(getString(R.string.instruction41), viewModel.immunity.p1!!)

            }
            binding.courtPreventTicket.setOnClickListener {
                navigateToImmunityView(getString(R.string.courtProcedure2), viewModel.immunity.p3!!)

            }
        }

        return root
    }

    private fun navigateToImmunityView(condition: String, list: String) {
        view?.findNavController()?.navigate(R.id.action_nav_immunityList_to_nav_immunityView,
            bundleOf("condition" to condition, "list" to list))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
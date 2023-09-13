package pl.tapo24.twa.ui.road.sign.signDetails

import android.R
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.MainActivity
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.State
import pl.tapo24.twa.databinding.FragmentSignDetailsBinding
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.dbData.entity.Sign
import pl.tapo24.twa.ui.tariff.DialogTariffMore
import java.io.File
@AndroidEntryPoint


class SignDetailsFragment : Fragment() {

    private var _binding: FragmentSignDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SignDetailsViewModel

    lateinit var signDetails: Sign
    private val dialogMore = DialogTariffMore()


    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { SignDetailsFragmentArgs.fromBundle(it) }
        signDetails = args!!.signDetails
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SignDetailsViewModel::class.java)
        _binding = FragmentSignDetailsBinding.inflate(inflater, container, false)
        if (State.enginesType == EnginesType.New) {
            signDetails.linkNew?.let { viewModel.getTariffDetails(it) }

        } else {
            signDetails.linkOld?.let { viewModel.getTariffDetails(it) }
        }
        val root: View = binding.root
        val file: File = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), signDetails.path!!)
        if (file.exists()) {
            val myBitmap = BitmapFactory.decodeFile(file.absolutePath)

            val myImage = binding.imageView16

            myImage.setImageBitmap(myBitmap)

            binding.signDescr.text = signDetails.description
            binding.signTitle.text = "Szczegóły znaku ${signDetails.name}"
        }
        binding.button7.setOnClickListener {
            showDialog()
        }


        viewModel.tariffDetail.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.tariffView.visibility = View.VISIBLE

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
                binding.tariffView.visibility = View.GONE
            }
        })

        requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
        return root

    }


    override fun onPause() {
        if (dialogMore.isVisible) {
            dialogMore.dismiss()
        }
        super.onPause()

    }
    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }


    private fun showDialog() {
        dialogMore.item = viewModel.tariffDetail.value
        //dialogMore.position = viewModel.positionToDialog
        dialogMore.show(childFragmentManager, "More")
//        dialogMore.onAddFavClick = {
//            viewModel.itemToDialog?.let { it1 -> viewModel.clickOnFavorites(it1, viewModel.positionToDialog) }
//        }
//        dialogMore.closeClick = {
//            viewModel.showDialog.value = false
//        }
    }


}
package pl.tapo24.twa.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R

import pl.tapo24.twa.dbData.entity.Sign
import pl.tapo24.twa.ui.road.sign.signList.SignListViewModel
import java.io.File

class SignListAdapter(
    var items: List<Sign>,
    var context2: Context,
    var viewModel: SignListViewModel
): RecyclerView.Adapter<SignListAdapter.SignListHolder>() {
    var onItemClick: ((Sign) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_sign_list, parent, false)
        return SignListHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SignListHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class SignListHolder(val view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageSign)
        val container:ConstraintLayout = view.findViewById(R.id.signelement)
        val name: TextView = view.findViewById(R.id.textView43)
        val description: TextView = view.findViewById(R.id.textView47)



        fun bind(item: Sign) {
            val file: File = if (viewModel.isPublicStorage) {
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "tapo24Don'tDelete/${item.path!!}")
            } else {
                File(context2?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), item.path!!)
            }
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                imageView.setImageBitmap(bitmap)
            }
            container.setOnClickListener {
                onItemClick?.invoke(item)
            }
            name.text= item.name
            description.text = item.description


        }
    }


}
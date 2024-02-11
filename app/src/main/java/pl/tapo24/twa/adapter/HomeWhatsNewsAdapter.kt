package pl.tapo24.twa.adapter

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.data.State
import pl.tapo24.twa.db.entity.WhatsNews
import pl.tapo24.twa.utils.UlListBuilder
import java.nio.charset.Charset
import java.util.*

class HomeWhatsNewsAdapter(
    var items: List<WhatsNews>
): RecyclerView.Adapter<HomeWhatsNewsAdapter.HomeWhatsNewsHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeWhatsNewsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_whats_news, parent, false)
        return HomeWhatsNewsHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HomeWhatsNewsHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class HomeWhatsNewsHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val date: TextView = view.findViewById(R.id.textViewDate)
        private val topic: TextView = view.findViewById(R.id.textViewTitle)
        private val p1: TextView = view.findViewById(R.id.textViewP1)
        private val p2: TextView = view.findViewById(R.id.textViewP2)
        private val p3: TextView = view.findViewById(R.id.textViewP3)
        private val p4: TextView = view.findViewById(R.id.textViewP4)
        private val footerBold: TextView = view.findViewById(R.id.textViewFooterBold)
        private val footer: TextView = view.findViewById(R.id.textViewFooter)
        private val autor: TextView = view.findViewById(R.id.textView2Author)
        private val version: TextView = view.findViewById(R.id.textViewVersion)
        private val web: WebView = view.findViewById(R.id.webView)
        fun bind(item: WhatsNews) {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val newDate = Date((item.date * 1000L).toLong())
                date.text = sdf.format(newDate)
            } catch (e: Exception) {
                e.toString()
            }
            web.setBackgroundColor(Color.TRANSPARENT);
            web.webChromeClient = WebChromeClient()
            web.settings.allowContentAccess = true
            web.settings.javaScriptEnabled = true
            web.settings.domStorageEnabled = true
            web.settings.userAgentString = "Android"
            web.settings.cacheMode
            web.settings.allowFileAccess = true
//            web.setInitialScale(100)
            //web.settings.loadWithOverviewMode = true
            //web.settings.useWideViewPort = true

            web.requestFocus()
            val tconvert = android.util.Base64.encodeToString(item.p1.toByteArray(), android.util.Base64.DEFAULT)
            //web.loadUrl("https://www.instagram.com/p/C11V8UusBMc/")
            //web.loadData( tconvert, "text/html", "base64")
            if (item.p1.contains("instagram-media")) {
                web.loadDataWithBaseURL("https://instagram.com", item.p1, "text/html", "base64", "")
            } else {
                web.loadDataWithBaseURL(null, item.p1, "text/html; charset=utf-8", "UTF-8", null)

            }
            topic.text = item.title
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              //ss.text = (Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                p4.text = (Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
//            }
            p1.text = UlListBuilder().getTextDash(item.p1, false)
            p2.text = UlListBuilder().getTextDash(item.p2)
            p3.text = UlListBuilder().getTextDash(item.p3)
            p4.text = UlListBuilder().getTextDash(item.p4)
            footerBold.text = item.footerBold
            footer.text = item.footer
            autor.text = item.author
            version.text = State.versionName
            if (item.p1.isEmpty()) {
                p1.visibility = GONE
            }
            if (item.p2.isEmpty()) {
                p2.visibility = GONE
            }
            if (item.p3.isEmpty()) {
                p3.visibility = GONE
            }
            if (item.p4.isEmpty()) {
                p4.visibility = GONE
            }

            if (item.footerBold.isEmpty()) {
                footerBold.visibility = GONE
            }
            if (item.footer.isEmpty()) {
                footer.visibility = GONE
            }


        }
    }


}
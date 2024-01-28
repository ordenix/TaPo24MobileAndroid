package pl.tapo24.twa.ui.tariff

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class Listener(
    val layoutManager: LinearLayoutManager
): RecyclerView.OnScrollListener(

) {
    val pageSize = 2

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (!isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= pageSize) {
                loadMoreItems()
            }

        }
    }
    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
}
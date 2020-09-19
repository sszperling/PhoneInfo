package zebaszp.phoneinfo.ui.applist

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

const val CARD_MARGIN_DP = 4f

class PackagesItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    val cardMargin : Int

    init {
        val dm = context.resources.displayMetrics
        cardMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CARD_MARGIN_DP, dm).toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(view)
        if (pos == RecyclerView.NO_POSITION) {
            return
        }
        outRect.set(cardMargin, cardMargin, cardMargin, cardMargin)
        if (pos == 0) {
            outRect.top = 2 * cardMargin
        }
        val adapter = parent.adapter
        if (adapter != null && pos == adapter.itemCount - 1) {
            outRect.bottom = 2 * cardMargin
        }
    }

}
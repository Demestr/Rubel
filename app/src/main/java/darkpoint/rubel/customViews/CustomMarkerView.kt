package darkpoint.rubel.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import darkpoint.rubel.R
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView
/**
 * Constructor. Sets up the MarkerView with a custom layout resource.
 *
 * @param context
 * @param layoutResource the layout resource to use for the MarkerView
 */
(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById<View>(R.id.tvContent) as TextView
    private val tvValue: TextView = findViewById<View>(R.id.tvValue) as TextView

    private var mOffset: MPPointF? = null
    @SuppressLint("SimpleDateFormat")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yy")
        val x = e!!.x.toDouble() * 1000
        val date1 = Date(x.toLong())
        val date = simpleDateFormat.format(date1)
        tvContent.text = date
        tvValue.text = e.y.toString()
    }

    override fun getOffset(): MPPointF {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }

        return mOffset!!
    }
}

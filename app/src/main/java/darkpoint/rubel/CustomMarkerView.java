package darkpoint.rubel;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent, tvValue;
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        double x = (double) e.getX() * 1000;
        Date date1 = new Date((long)x);
        String date = simpleDateFormat.format(date1);
        tvContent.setText(date);
        tvValue.setText(String.valueOf(e.getY()));
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvValue = (TextView) findViewById(R.id.tvValue);
    }
}

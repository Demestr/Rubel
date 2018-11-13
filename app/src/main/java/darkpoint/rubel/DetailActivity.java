package darkpoint.rubel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    int id_cur, color;
    private String startDate, endDate;
    double y_rate, t_rate, tw_rate;
    boolean launch = true;
    Rate rate;
    TextView rate_yesterday, rate_today, rate_tomorrow, rate_y_move, rate_t_move, rate_tw_move,
                rate_yesterday_razn, rate_today_razn, rate_tomorrow_razn;
    BottomNavigationView choice_period;

    LineChart detailChart;
    Toolbar toolbar;
    ProgressBar bar;

    @NonNull
    public static Intent makeIntent(@NonNull Activity activity, @NonNull Rate rate, @NonNull String sDate, @NonNull String eDate, @NonNull int color) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra("rate", rate);
        intent.putExtra("sDate", sDate);
        intent.putExtra("eDate", eDate);
        intent.putExtra("color", color);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.toolbar_detail);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        choice_period = findViewById(R.id.bottomNavigationView);
        choice_period.getMenu().getItem(1).setChecked(true);
        rate_yesterday = findViewById(R.id.txt_yesterday_rate);
        rate_today = findViewById(R.id.txt_today_rate);
        rate_tomorrow = findViewById(R.id.txt_tomorrow_rate);
        rate_y_move = findViewById(R.id.txt_yesterday_move);
        rate_t_move = findViewById(R.id.txt_today_move);
        rate_tw_move = findViewById(R.id.txt_tomorrow_move);
        rate_yesterday_razn = findViewById(R.id.txt_yesterday_razn);
        rate_today_razn = findViewById(R.id.txt_today_razn);
        rate_tomorrow_razn = findViewById(R.id.txt_tomorrow_razn);
        bar = findViewById(R.id.progressBar_detail);
        rate = (Rate) getIntent().getSerializableExtra("rate");
        id_cur = rate.getCur_ID();
        startDate = getIntent().getStringExtra("sDate");
        endDate = getIntent().getStringExtra("eDate");
        color = getIntent().getIntExtra("color", 1);
        detailChart = findViewById(R.id.detailChart);
        ServerAPI api = APIFactory.createApi();
        Call<Currency> currencyCall = api.getCurrency(id_cur);
        currencyCall.enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(Call<Currency> call, Response<Currency> response) {
                Currency currency = response.body();
                getSupportActionBar().setTitle(currency.Cur_Name);
            }

            @Override
            public void onFailure(Call<Currency> call, Throwable t) {

            }
        });
        loadCurrencyDetail(false);

        choice_period.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                final Date EndDate = calendar.getTime();
                endDate = format.format(EndDate);
                switch (menuItem.getItemId()) {
                    case R.id.year:
                        calendar.add(Calendar.DATE, -364);
                        final Date StartDate = calendar.getTime();
                        startDate = format.format(StartDate);
                        loadCurrencyDetail(true);
                        menuItem.setChecked(true);
                        break;
                    case R.id.month:
                        calendar.add(Calendar.MONTH, -1);
                        final Date StartDate2 = calendar.getTime();
                        startDate = format.format(StartDate2);
                        loadCurrencyDetail(true);
                        menuItem.setChecked(true);
                        break;
                    case R.id.week:
                        calendar.add(Calendar.DATE, -7);
                        final Date StartDate3 = calendar.getTime();
                        startDate = format.format(StartDate3);
                        loadCurrencyDetail(true);
                        menuItem.setChecked(true);
                        break;
                }
                return false;
            }
        });
    }

    private void loadCurrencyDetail(boolean restart) {
        LoaderManager.LoaderCallbacks<List<RateShort>> callbacks = new CurrencyCallbacks();
        if (restart) {
            detailChart.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(0, Bundle.EMPTY, callbacks);
        } else {
            detailChart.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(0, Bundle.EMPTY, callbacks);
        }
    }

    private void showDetail(List<RateShort> rateShorts, boolean first_launch) {
        TextView y_label = findViewById(R.id.txt_yesterday);
        TextView t_label = findViewById(R.id.txt_today);
        TextView tw_label = findViewById(R.id.txt_tomorrow);
        int duration;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Entry> entries = new ArrayList<>();
        for (RateShort rate : rateShorts) {
            Date date = rate.getDate();
            long date_long = date.getTime() / 1000;
            entries.add(new Entry(date_long, (float) rate.getCur_OfficialRate()));
        }
        String date = dateFormat.format(rateShorts.get(rateShorts.size() - 1).Date);
        String date_now = dateFormat.format(Calendar.getInstance().getTime());
        if(!date.equals(date_now))
            entries.remove(entries.get(entries.size()-1));
        if(entries.size() > 100)
            duration = 2000;
        else if(entries.size() < 100 && entries.size() > 10)
            duration = 1000;
        else
            duration = 500;

        if(first_launch) {
            y_label.setVisibility(View.VISIBLE);
            t_label.setVisibility(View.VISIBLE);
            tw_label.setVisibility(View.VISIBLE);
            choice_period.setVisibility(View.VISIBLE);
            if (date.equals(date_now)) {
                setCurrencyRates(rateShorts.get(rateShorts.size() - 3), rateShorts.get(rateShorts.size() - 2),
                        rateShorts.get(rateShorts.size() - 1));
            } else {
                setCurrencyRates(rateShorts.get(rateShorts.size() - 4), rateShorts.get(rateShorts.size() - 3),
                        rateShorts.get(rateShorts.size() - 2), rateShorts.get(rateShorts.size() - 1));
            }
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(color);
        detailChart.highlightValue(null);
        detailChart.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
        detailChart.getXAxis().setDrawGridLines(false);
        detailChart.getXAxis().setDrawAxisLine(false);
        detailChart.getAxisLeft().setDrawGridLines(false);
        detailChart.getAxisRight().setDrawGridLines(false);
        detailChart.setScaleEnabled(false);
        detailChart.getAxisRight().setTextColor(Color.WHITE);
        detailChart.getDescription().setEnabled(false);
        detailChart.getAxisLeft().setTextColor(Color.WHITE);
        detailChart.getXAxis().setTextColor(Color.WHITE);
        detailChart.getLegend().setTextColor(Color.WHITE);
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(color);
        dataSet.setHighLightColor(color);
        dataSet.enableDashedHighlightLine(15, 5, 5);
        LineData lineData = new LineData(dataSet);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLabel(String.valueOf(rate.getCur_Scale()) + " " + rate.getCur_Name());
        lineData.setDrawValues(false);
        CustomMarkerView markerView = new CustomMarkerView(this, R.layout.chart_item);
        detailChart.setData(lineData);
        detailChart.setMarker(markerView);
        detailChart.animateX(duration);
        XAxis x = detailChart.getXAxis();
        x.setLabelCount(4, true);
        x.setValueFormatter(new MyXAxisValueFormatter());
        detailChart.setPinchZoom(true);
        detailChart.invalidate();
        launch = false;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {


        private MyXAxisValueFormatter() {
            // format values to 1 decimal digit
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            double value_d = (double) value * 1000;
            return simpleDateFormat.format(value_d);
        }

    }

    private class CurrencyCallbacks implements LoaderManager.LoaderCallbacks<List<RateShort>> {

        @NonNull
        @Override
        public Loader<List<RateShort>> onCreateLoader(int id, @Nullable Bundle args) {
            return new RetrofitCurrencyLoader(DetailActivity.this, id_cur, startDate, endDate);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<RateShort>> loader, List<RateShort> data) {
            if (data != null) {
                showDetail(data, launch);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<RateShort>> loader) {

        }
    }

    void setCurrencyRates(RateShort past, RateShort yesterday, RateShort today) {
        rate_y_move.setVisibility(View.VISIBLE);
        rate_t_move.setVisibility(View.VISIBLE);
        rate_tomorrow.setText("-");
        rate_today.setText(String.valueOf(today.Cur_OfficialRate));
        if(today.Cur_OfficialRate - yesterday.Cur_OfficialRate != 0)
            rate_today_razn.setText(String.valueOf(new BigDecimal(today.Cur_OfficialRate - yesterday.Cur_OfficialRate).setScale(4, RoundingMode.UP)));
        rate_yesterday.setText(String.valueOf(yesterday.Cur_OfficialRate));
        if(yesterday.Cur_OfficialRate - past.Cur_OfficialRate != 0)
            rate_yesterday_razn.setText(String.valueOf(new BigDecimal(yesterday.Cur_OfficialRate - past.Cur_OfficialRate).setScale(4, RoundingMode.UP)));
        if (today.Cur_OfficialRate < yesterday.Cur_OfficialRate) {
            rate_t_move.setText(R.string.cur_down);
            rate_t_move.setTextColor(Color.RED);
            rate_today_razn.setText(rate_today_razn.getText());
            rate_today_razn.setTextColor(Color.RED);
        } else if (today.Cur_OfficialRate > yesterday.Cur_OfficialRate) {
            rate_t_move.setText(R.string.cur_up);
            rate_t_move.setTextColor(Color.GREEN);
            rate_today_razn.setText("+" + rate_today_razn.getText());
            rate_today_razn.setTextColor(Color.GREEN);
        } else {
            rate_t_move.setText("-");
            rate_t_move.setTextColor(Color.WHITE);
            rate_today_razn.setTextColor(Color.WHITE);
        }
        if (yesterday.Cur_OfficialRate < past.Cur_OfficialRate) {
            rate_y_move.setText(R.string.cur_down);
            rate_y_move.setTextColor(Color.RED);
            rate_yesterday_razn.setText(rate_yesterday_razn.getText());
            rate_yesterday_razn.setTextColor(Color.RED);
        } else if (yesterday.Cur_OfficialRate > past.Cur_OfficialRate) {
            rate_y_move.setText(R.string.cur_up);
            rate_y_move.setTextColor(Color.GREEN);
            rate_yesterday_razn.setText("+" + rate_yesterday_razn.getText());
            rate_yesterday_razn.setTextColor(Color.GREEN);
        } else {
            rate_y_move.setText("-");
            rate_y_move.setTextColor(Color.WHITE);
            rate_yesterday_razn.setTextColor(Color.WHITE);
        }
    }

    void setCurrencyRates(RateShort past, RateShort yesterday, RateShort today, RateShort tomorrow) {
        setCurrencyRates(past, yesterday, today);
        rate_tw_move.setVisibility(View.VISIBLE);
        if(tomorrow.Cur_OfficialRate - today.Cur_OfficialRate != 0)
            rate_tomorrow_razn.setText(String.valueOf(new BigDecimal(tomorrow.Cur_OfficialRate - today.Cur_OfficialRate).setScale(4, RoundingMode.UP)));
        rate_tomorrow.setText(String.valueOf(tomorrow.Cur_OfficialRate));
        if (tomorrow.Cur_OfficialRate < today.Cur_OfficialRate) {
            rate_tw_move.setText(R.string.cur_down);
            rate_tw_move.setTextColor(Color.RED);
            rate_yesterday_razn.setText(rate_yesterday_razn.getText());
            rate_tomorrow_razn.setTextColor(Color.RED);
        } else if (tomorrow.Cur_OfficialRate > today.Cur_OfficialRate) {
            rate_tw_move.setText(R.string.cur_up);
            rate_tw_move.setTextColor(Color.GREEN);
            rate_tomorrow_razn.setText("+" + rate_tomorrow_razn.getText());
            rate_tomorrow_razn.setTextColor(Color.GREEN);
        } else {
            rate_tw_move.setText("-");
            rate_tw_move.setTextColor(Color.WHITE);
        }
    }
}

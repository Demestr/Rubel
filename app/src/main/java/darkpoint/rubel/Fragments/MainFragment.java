package darkpoint.rubel.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import darkpoint.rubel.APIFactory;
import darkpoint.rubel.DetailActivity;
import darkpoint.rubel.Rate;
import darkpoint.rubel.R;
import darkpoint.rubel.ServerAPI;
import darkpoint.rubel.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private TextView cur_scale_one, cur_value_one, cur_scale_two, cur_value_two, cur_scale_three,
            cur_abr_one, cur_abr_two, cur_abr_three, cur_value_three, datePicker,
            cur_scale_four, cur_value_four, cur_abr_four;
    private CardView currency_one, currency_two, currency_three, currency_four;
    private FloatingActionButton fab_back, fab_forward;
    private ProgressBar progressBar;
    private LinearLayout cards;
    private int[] four_currency;
    private HashMap<Integer, Rate> rateHashMap;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static MainFragment newInstance(int page) {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final Calendar calendar_detail = Calendar.getInstance();
        four_currency = Setting.loadSetting(this.getActivity());
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        cards = view.findViewById(R.id.include);
        progressBar = view.findViewById(R.id.progressBar_main);
        cur_scale_one = view.findViewById(R.id.currency_one_scale);
        cur_value_one = view.findViewById(R.id.currency_one_value);
        cur_abr_one = view.findViewById(R.id.currency_one_abr);
        cur_scale_two = view.findViewById(R.id.currency_two_scale);
        cur_value_two = view.findViewById(R.id.currency_two_value);
        cur_abr_two = view.findViewById(R.id.currency_two_abr);
        cur_scale_three = view.findViewById(R.id.currency_three_scale);
        cur_value_three = view.findViewById(R.id.currency_three_value);
        cur_abr_three = view.findViewById(R.id.currency_three_abr);
        cur_scale_four = view.findViewById(R.id.currency_four_scale);
        cur_value_four = view.findViewById(R.id.currency_four_value);
        cur_abr_four = view.findViewById(R.id.currency_four_abr);
        datePicker = view.findViewById(R.id.datePicker);
        fab_back = view.findViewById(R.id.fab_backDate);
        fab_forward = view.findViewById(R.id.fab_forwardDate);
        currency_one = view.findViewById(R.id.currency_one);
        currency_two = view.findViewById(R.id.currency_two);
        currency_three = view.findViewById(R.id.currency_three);
        currency_four = view.findViewById(R.id.currency_four);

        rateHashMap = new HashMap<>();

        calendar_detail.add(Calendar.DATE, 1);
        final Date endDate = calendar_detail.getTime();
        calendar_detail.add(Calendar.MONTH, -1);
        calendar_detail.add(Calendar.DATE, -1);
        final Date startDate = calendar_detail.getTime();

        MobileAds.initialize(getContext(), "ca-app-pub-5942497315583191~8880324859");
        final AdView mAdView = (AdView) view.findViewById(R.id.bottom_ad_banner);
        AdRequest mAdRequest = new AdRequest.Builder().build();
        mAdView.loadAd(mAdRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        datePicker.setText(DateFormat.getDateInstance(DateFormat.LONG).format(Calendar.getInstance().getTime()));
        //getCurrenciesPeriod(new int[]{});
        getCurrenciesMain(four_currency, Calendar.getInstance().getTime(), true);

        currency_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DetailActivity.makeIntent(getActivity(), rateHashMap.get(four_currency[0]),
                        simpleDateFormat.format(startDate), simpleDateFormat.format(endDate),
                        ContextCompat.getColor(getContext(), R.color.colorCurrencyOne)));
            }
        });

        currency_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DetailActivity.makeIntent(getActivity(), rateHashMap.get(four_currency[1]),
                        simpleDateFormat.format(startDate), simpleDateFormat.format(endDate),
                        ContextCompat.getColor(getContext(), R.color.colorCurrencyTwo)));
            }
        });

        currency_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DetailActivity.makeIntent(getActivity(), rateHashMap.get(four_currency[2]),
                        simpleDateFormat.format(startDate), simpleDateFormat.format(endDate),
                        ContextCompat.getColor(getContext(), R.color.colorCurrencyThree)));
            }
        });

        currency_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(DetailActivity.makeIntent(getActivity(), rateHashMap.get(four_currency[3]),
                        simpleDateFormat.format(startDate), simpleDateFormat.format(endDate),
                        ContextCompat.getColor(getContext(), R.color.colorCurrencyFour)));
            }
        });

        final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("RestrictedApi")
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                getCurrenciesMain(four_currency, calendar.getTime(), false);
                datePicker.setText(DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime()));
                fab_forward.setVisibility(View.VISIBLE);
                if (calendar.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE) &&
                        calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                        calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
                    fab_forward.setVisibility(View.GONE);
            }
        };

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), d,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTime().getTime());
                pickerDialog.show();
            }
        });
        fab_back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                getCurrenciesMain(four_currency, calendar.getTime(), false);
                datePicker.setText(DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime()));
                fab_forward.setVisibility(View.VISIBLE);
            }
        });

        fab_forward.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, 1);
                getCurrenciesMain(four_currency, calendar.getTime(), false);
                datePicker.setText(DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime()));
                if (calendar.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE) &&
                        calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                        calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
                    fab_forward.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void getCurrenciesMain(final int[] currencies_id, final Date date, final boolean save) {
        ServerAPI serverAPI = APIFactory.createApi();

        for (int i = 0; i < currencies_id.length; i++) {
            final TextView scale, value, abr;
            switch (i) {
                case 0: {
                    scale = cur_scale_one;
                    value = cur_value_one;
                    abr = cur_abr_one;
                    break;
                }
                case 1: {
                    scale = cur_scale_two;
                    value = cur_value_two;
                    abr = cur_abr_two;
                    break;
                }
                case 2: {
                    scale = cur_scale_three;
                    value = cur_value_three;
                    abr = cur_abr_three;
                    break;
                }
                case 3: {
                    scale = cur_scale_four;
                    value = cur_value_four;
                    abr = cur_abr_four;
                    break;
                }
                default: {
                    scale = cur_scale_one;
                    value = cur_value_one;
                    abr = cur_abr_one;
                    break;
                }
            }
            Call<Rate> currencyCall = serverAPI.getRateOnDate(currencies_id[i], simpleDateFormat.format(date));
            final int id_rate = currencies_id[i];
            if (i == 0)
                currencyCall.enqueue(new Callback<Rate>() {
                    @Override
                    public void onResponse(@NonNull Call<Rate> call, @NonNull Response<Rate> response) {
                        if (response.body() != null) {
                            Rate rate = response.body();
                            rateHashMap.put(id_rate, rate);
                            scale.setText(String.valueOf(rate.getCur_Scale()));
                            value.setText(String.valueOf(rate.getCur_OfficialRate()));
                            abr.setText(rate.getCur_Abbreviation());
                            if (rateHashMap.size() == 4) {
                                progressBar.setVisibility(View.GONE);
                                cards.setVisibility(View.VISIBLE);
                            }
                        } else {
                            value.setText("-");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Rate> call, @NonNull Throwable t) {
                        Snackbar.make(progressBar, "Проверьте подключение к сети интернет", Snackbar.LENGTH_LONG);
                    }
                });
            else
                currencyCall.clone().enqueue(new Callback<Rate>() {
                    @Override
                    public void onResponse(Call<Rate> call, Response<Rate> response) {
                        if (response.body() != null) {
                            Rate rate = response.body();
                            rateHashMap.put(id_rate, rate);
                            scale.setText(String.valueOf(rate.getCur_Scale()));
                            value.setText(String.valueOf(rate.getCur_OfficialRate()));
                            abr.setText(rate.getCur_Abbreviation());
                            if (rateHashMap.size() == 4) {
                                progressBar.setVisibility(View.GONE);
                                cards.setVisibility(View.VISIBLE);
                                if(save) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    Setting.saveCacheCurrencyNames(getContext(), rateHashMap.get(currencies_id[0]).getCur_Scale() + " " + rateHashMap.get(currencies_id[0]).getCur_Abbreviation(),
                                            rateHashMap.get(currencies_id[1]).getCur_Scale() + " " + rateHashMap.get(currencies_id[1]).getCur_Abbreviation(),
                                            rateHashMap.get(currencies_id[2]).getCur_Scale() + " " + rateHashMap.get(currencies_id[2]).getCur_Abbreviation(),
                                            rateHashMap.get(currencies_id[3]).getCur_Scale() + " " + rateHashMap.get(currencies_id[3]).getCur_Abbreviation());
                                    Setting.saveCacheCurrencyValues(getContext(), (float) rateHashMap.get(currencies_id[0]).getCur_OfficialRate(), (float) rateHashMap.get(currencies_id[1]).getCur_OfficialRate(),
                                            (float) rateHashMap.get(currencies_id[2]).getCur_OfficialRate(), (float) rateHashMap.get(currencies_id[3]).getCur_OfficialRate());
                                    Setting.saveCacheDate(getContext(), sdf.format(date));
                                }
                            }
                        } else {
                            value.setText("-");
                        }
                    }

                    @Override
                    public void onFailure(Call<Rate> call, Throwable t) {
                        Snackbar.make(progressBar, "Проверьте подключение к сети интернет", Snackbar.LENGTH_LONG);
                    }
                });
        }
    }

}

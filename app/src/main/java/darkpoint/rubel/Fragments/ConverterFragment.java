package darkpoint.rubel.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.OverScroller;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import darkpoint.rubel.APIFactory;
import darkpoint.rubel.MainActivity;
import darkpoint.rubel.R;
import darkpoint.rubel.Rate;
import darkpoint.rubel.ServerAPI;
import darkpoint.rubel.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConverterFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    int mPage;
    int[] four_currencies;
    private TextInputEditText input_rate_byn, input_rate_one, input_rate_two, input_rate_three,
            input_rate_four, input_cur_one, input_cur_two;
    private TextInputLayout input_byn, input_one, input_two, input_three, input_four;
    private List<Rate>currencies, rates1, rates2, rates3, rates4, rates5;
    private boolean cur_one_focus, cur_two_focus;
    ScrollView scrollView;
    ProgressBar progressBar;
    Spinner spinner_cur1, spinner_cur2;
    List<List<Rate>> ratesList = new ArrayList<>();
    List<String> abrs_list = new ArrayList<>();
    HashMap<Integer, Rate> rateHashMap = new HashMap<>();
    HashMap<String, Rate> rateHashMap_spinner = new HashMap<>();
    TextWatcher cur_one_tw, cur_two_tw;
    private Rate rate1_key, rate2_key, rate3_key, rate4_key, rate5_key;
    private CustomTextWatcher watcher_byn, watcher1, watcher2, watcher3, watcher4;

    public static ConverterFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ConverterFragment fragment = new ConverterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_converter, container, false);
        four_currencies = Setting.loadSetting(this.getActivity());
        scrollView = view.findViewById(R.id.conv_view);
        progressBar = view.findViewById(R.id.progressbar_converter);
        spinner_cur1 = view.findViewById(R.id.spinner_cur_one);
        spinner_cur2 = view.findViewById(R.id.spinner_cur_two);
        input_rate_byn = view.findViewById(R.id.editText_byn);
        input_rate_one = view.findViewById(R.id.editText);
        input_rate_two = view.findViewById(R.id.editText_two);
        input_rate_three = view.findViewById(R.id.editText_three);
        input_rate_four = view.findViewById(R.id.editText_four);
        input_cur_one = view.findViewById(R.id.editText_cur_one_conv);
        input_cur_two = view.findViewById(R.id.editText_cur_two_conv);
        input_byn = view.findViewById(R.id.byn);
        input_one = view.findViewById(R.id.one);
        input_two = view.findViewById(R.id.two);
        input_three = view.findViewById(R.id.three);
        input_four = view.findViewById(R.id.four);

        input_cur_one.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                cur_one_focus = b;
            }
        });

        input_cur_two.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            cur_two_focus = b;
            }
        });

        input_rate_byn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    input_rate_byn.addTextChangedListener(watcher_byn);
                }
                else
                {
                    input_rate_byn.removeTextChangedListener(watcher_byn);
                }
            }
        });

        input_rate_one.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    input_rate_one.addTextChangedListener(watcher1);
                }
                else
                {
                    input_rate_one.removeTextChangedListener(watcher1);
                }
            }
        });


        input_rate_two.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    input_rate_two.addTextChangedListener(watcher2);
                }
                else
                {
                    input_rate_two.removeTextChangedListener(watcher2);
                }
            }
        });

        input_rate_three.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    input_rate_three.addTextChangedListener(watcher3);
                }
                else
                {
                    input_rate_three.removeTextChangedListener(watcher3);
                }
            }
        });

        input_rate_four.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    input_rate_four.addTextChangedListener(watcher4);
                }
                else
                {
                    input_rate_four.removeTextChangedListener(watcher4);
                }
            }
        });

        input_rate_byn.setLongClickable(false);
        input_rate_one.setLongClickable(false);
        input_rate_two.setLongClickable(false);
        input_rate_three.setLongClickable(false);
        input_rate_four.setLongClickable(false);

        ServerAPI serverAPI = APIFactory.createApi();

        Call<List<Rate>> currenciesGSON = serverAPI.getAllRates(0);

        currenciesGSON.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                if(response.isSuccessful())
                {
                    if(abrs_list != null)
                        abrs_list.clear();
                    if(cur_one_tw != null) {
                        input_cur_one.removeTextChangedListener(cur_one_tw);
                        cur_one_tw = null;
                    }
                    if(cur_two_tw != null) {
                        input_cur_two.removeTextChangedListener(cur_two_tw);
                        cur_two_tw = null;
                    }
                    currencies = response.body();
                    currencies.add(0, new Rate(666, Calendar.getInstance().getTime(), "BYN", 1, "Белорусский рубль", 1));
                    for (Rate rate : currencies) {
                        rateHashMap.put(rate.getCur_ID(), rate);
                        rateHashMap_spinner.put(rate.getCur_Abbreviation(), rate);
                        abrs_list.add(rate.getCur_Abbreviation());
                    }
                    ArrayAdapter<String> abrs = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_simple, R.id.spinner_abr, abrs_list);
                    spinner_cur1.setAdapter(abrs);
                    spinner_cur2.setAdapter(abrs);
                    spinner_cur2.setSelection(5);
                    initialAdapters(rateHashMap, four_currencies);

                    cur_one_tw = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String id1 =(String) spinner_cur1.getSelectedItem();
                            int id_one = rateHashMap_spinner.get(id1).getCur_ID();
                            String id2 =(String) spinner_cur2.getSelectedItem();
                            int id_two = rateHashMap_spinner.get(id2).getCur_ID();
                            if (s.toString().matches("^[0.]") ) {
                                input_cur_one.setText("");
                                return;
                            }
                            if(s.length() != 0)
                            {
                                if(cur_one_focus) {
                                    input_cur_two.setText(String.valueOf(new BigDecimal(calculateRate(Double
                                                    .valueOf(s.toString()), id_one,
                                            id_two)).setScale(2, RoundingMode.UP)));
                                }
                            }
                            else
                            {
                                if(cur_one_focus) {
                                    input_cur_two.setText("");
                                }
                            }
                        }
                    };

                    cur_two_tw = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String id1 =(String) spinner_cur1.getSelectedItem();
                            int id_one = rateHashMap_spinner.get(id1).getCur_ID();
                            String id2 =(String) spinner_cur2.getSelectedItem();
                            int id_two = rateHashMap_spinner.get(id2).getCur_ID();
                            if (s.toString().matches("^[0.]") ) {
                                input_cur_two.setText("");
                                return;
                            }
                            if(s.length() != 0)
                            {
                                if(cur_two_focus) {
                                    input_cur_one.setText(String.valueOf(new BigDecimal(calculateRate(Double
                                                    .valueOf(s.toString()), id_two,
                                            id_one)).setScale(2, RoundingMode.UP)));
                                }
                            }
                            else
                            {
                                if(cur_two_focus) {
                                    input_cur_one.setText("");
                                }
                            }
                        }
                    };

                    input_cur_one.addTextChangedListener(cur_one_tw);

                    input_cur_two.addTextChangedListener(cur_two_tw);

                    spinner_cur1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String id1 =(String) spinner_cur1.getSelectedItem();
                            int id_one = rateHashMap_spinner.get(id1).getCur_ID();
                            String id2 =(String) spinner_cur2.getSelectedItem();
                            int id_two = rateHashMap_spinner.get(id2).getCur_ID();
                            if(input_cur_two.getText().length() != 0)
                            input_cur_one.setText(String.valueOf(new BigDecimal(calculateRate(Double
                                            .valueOf(input_cur_two.getText().toString()), id_two,
                                    id_one)).setScale(2, RoundingMode.UP)));
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }

                    });

                    spinner_cur2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String id1 =(String) spinner_cur1.getSelectedItem();
                            int id_one = rateHashMap_spinner.get(id1).getCur_ID();
                            String id2 =(String) spinner_cur2.getSelectedItem();
                            int id_two = rateHashMap_spinner.get(id2).getCur_ID();
                            if(input_cur_one.getText().length() != 0)
                            input_cur_two.setText(String.valueOf(new BigDecimal(calculateRate(Double
                                            .valueOf(input_cur_one.getText().toString()), id_one,
                                    id_two)).setScale(2, RoundingMode.UP)));
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                Log.d("result", "onResponse: " + t.getLocalizedMessage());
            }
        });
        return view;
    }




    double calculateRate(double value, int old_cur, int new_cur)
    {
        Rate rate1, rate2;
        rate1 = rateHashMap.get(old_cur);
        rate2 = rateHashMap.get(new_cur);

        double rate1_value, rate2_value, rate_final, rate_koef;
        rate1_value = 1/rate1.getCur_OfficialRate()*rate1.getCur_Scale();
        rate2_value = 1/rate2.getCur_OfficialRate()*rate2.getCur_Scale();
        rate_koef = rate2_value / rate1_value;
        rate_final = rate_koef * value;
        return rate_final;
    }

    void initialAdapters(HashMap<Integer, Rate> rateHashMap, int[] ids)
    {
        List<Rate> rates = new ArrayList<>();
        List<Rate> spinner_list = new ArrayList<>();
        //spinner_list =;
        rates.add(rateHashMap.get(666));
        for(int i=0; i < ids.length; i++)
        {
            rates.add(rateHashMap.get(ids[i]));
        }
        rate1_key = rates.get(0);
        rates1 = new ArrayList<>(rates);
        rates1.remove(0);
        rate2_key = rates.get(1);
        rates2 = new ArrayList<>(rates);
        rates2.remove(1);
        rate3_key = rates.get(2);
        rates3 = new ArrayList<>(rates);
        rates3.remove(2);
        rate4_key = rates.get(3);
        rates4 = new ArrayList<>(rates);
        rates4.remove(3);
        rate5_key = rates.get(4);
        rates5 = new ArrayList<>(rates);
        rates5.remove(4);

        input_byn.setHint(rate1_key.getCur_Abbreviation());
        input_one.setHint(rate2_key.getCur_Abbreviation());
        input_two.setHint(rate3_key.getCur_Abbreviation());
        input_three.setHint(rate4_key.getCur_Abbreviation());
        input_four.setHint(rate5_key.getCur_Abbreviation());
        watcher_byn = new CustomTextWatcher(input_rate_byn, new TextInputEditText[]{input_rate_one, input_rate_two, input_rate_three, input_rate_four}, rate1_key, rates1);
        watcher1 = new CustomTextWatcher(input_rate_one, new TextInputEditText[]{input_rate_byn, input_rate_two, input_rate_three, input_rate_four}, rate2_key, rates2);
        watcher2 = new CustomTextWatcher(input_rate_two, new TextInputEditText[]{input_rate_byn, input_rate_one, input_rate_three, input_rate_four}, rate3_key, rates3);
        watcher3 = new CustomTextWatcher(input_rate_three, new TextInputEditText[]{input_rate_byn, input_rate_one, input_rate_two, input_rate_four}, rate4_key, rates4);
        watcher4 = new CustomTextWatcher(input_rate_four, new TextInputEditText[]{input_rate_byn, input_rate_one, input_rate_two, input_rate_three}, rate5_key, rates5);

        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    //void updateEditCurrency(double value, ){}

    private class CustomTextWatcher implements TextWatcher
    {
        TextInputEditText this_edit;
        TextInputEditText[] mValues;
        Rate rate_changing;
        List<Rate> rates_changing;

        public CustomTextWatcher(TextInputEditText this_edit, TextInputEditText[] inputEditTexts, Rate rate_input, List<Rate> rates_input)
        {
            mValues = inputEditTexts;
            this.this_edit = this_edit;
            this.rate_changing = rate_input;
            this.rates_changing = rates_input;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().matches("^0") ) {
                this_edit.setText("");
                return;
            }
            if (editable.toString().matches("^[.]") ) {
                this_edit.setText("");
                return;
            }
            if(editable.length() != 0) {
                mValues[0].setText(String.valueOf(new BigDecimal(calculateRate(Double
                                .valueOf(editable.toString()), rate_changing.getCur_ID(),
                        rates_changing.get(0).getCur_ID())).setScale(2, RoundingMode.UP)));

                mValues[1].setText(String.valueOf(new BigDecimal(calculateRate(Double
                                .valueOf(editable.toString()), rate_changing.getCur_ID(),
                        rates_changing.get(1).getCur_ID())).setScale(2, RoundingMode.UP)));

                mValues[2].setText(String.valueOf(new BigDecimal(calculateRate(Double
                                .valueOf(editable.toString()), rate_changing.getCur_ID(),
                        rates_changing.get(2).getCur_ID())).setScale(2, RoundingMode.UP)));

                mValues[3].setText(String.valueOf(new BigDecimal(calculateRate(Double
                                .valueOf(editable.toString()), rate_changing.getCur_ID(),
                        rates_changing.get(3).getCur_ID())).setScale(2, RoundingMode.UP)));
            }
            else
            {
                mValues[0].setText("");
                mValues[1].setText("");
                mValues[2].setText("");
                mValues[3].setText("");
            }
        }
    }
}

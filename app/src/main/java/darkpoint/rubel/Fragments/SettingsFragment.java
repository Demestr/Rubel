package darkpoint.rubel.Fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import darkpoint.rubel.APIFactory;
import darkpoint.rubel.CustomChip;
import darkpoint.rubel.R;
import darkpoint.rubel.Rate;
import darkpoint.rubel.ServerAPI;
import darkpoint.rubel.Setting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    int mPage, counter_checked, color, color_one, color_two, color_three, color_four;
    ChipGroup group;
    LinearLayout ll;
    ProgressBar bar;
    View divider1, divider2;
    List<ColorStateList> colors = new ArrayList<>();
    List<Integer> temp_ids = new ArrayList<>();
    TextView cur1_abr, cur2_abr, cur3_abr, cur4_abr, cur1_name, cur2_name, cur3_name, cur4_name;
    View color1, color2, color3, color4;
    FloatingActionButton fab;
    int[] ids;
    private List<Rate> currencies;
    private HashMap<Integer,Rate> rateHashMap = new HashMap<>();

    public static SettingsFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SettingsFragment fragment = new SettingsFragment();
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
        color_one = R.color.colorCurrencyOne;
        color_two = R.color.colorCurrencyTwo;
        color_three = R.color.colorCurrencyThree;
        color_four = R.color.colorCurrencyFour;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ll = view.findViewById(R.id.setting_layout);
        bar = view.findViewById(R.id.progressBar_setting);
        divider1 = view.findViewById(R.id.divider_cur_setting);
        divider2 = view.findViewById(R.id.divider_cur_setting2);
        group = view.findViewById(R.id.currencies_group);
        cur1_abr = view.findViewById(R.id.cur_one_abr);
        cur2_abr = view.findViewById(R.id.cur_two_abr);
        cur3_abr = view.findViewById(R.id.cur_three_abr);
        cur4_abr = view.findViewById(R.id.cur_four_abr);
        cur1_name = view.findViewById(R.id.cur_one_name);
        cur2_name = view.findViewById(R.id.cur_two_name);
        cur3_name = view.findViewById(R.id.cur_three_name);
        cur4_name = view.findViewById(R.id.cur_four_name);
        color1 = view.findViewById(R.id.cur_one_color);
        color2 = view.findViewById(R.id.cur_two_color);
        color3 = view.findViewById(R.id.cur_three_color);
        color4 = view.findViewById(R.id.cur_four_color);
        fab = view.findViewById(R.id.fab_save);
        ids = Setting.loadSetting(getContext());

        final ServerAPI serverAPI = APIFactory.createApi();

        Call<List<Rate>> currenciesGSON = serverAPI.getAllRates(0);

        currenciesGSON.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                if(response.isSuccessful())
                {
                    currencies = response.body();
                    for (Rate rate : currencies) {
                        rateHashMap.put(rate.getCur_ID(), rate);
                    }
                    loadSetting(rateHashMap);
                    bar.setVisibility(View.GONE);
                    divider1.setVisibility(View.VISIBLE);
                    divider2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                Log.d("result", "onResponse: " + t.getLocalizedMessage());
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                    saveSetting();
                    Snackbar.make(view, "Валюты сохранены. Потяните для обновления данных", Snackbar.LENGTH_LONG).show();
                    fab.setVisibility(View.GONE);
            }
        });
        return view;
    }

    void loadSetting(HashMap<Integer, Rate> map)
    {
        temp_ids.clear();
        colors.clear();
        for (final Rate rate : map.values()) {
            CustomChip chip = new CustomChip(getContext());
            chip.setText(rate.getCur_Abbreviation());
            chip.setChipBackgroundColorResource(R.color.colorPrimaryDark);
            chip.setTextColor(ContextCompat.getColor(getContext(), R.color.colorSelectedTab));
            if(rate.getCur_ID() == ids[0] || rate.getCur_ID() == ids[1] || rate.getCur_ID() == ids[2] || rate.getCur_ID() == ids[3]) {
                if(rate.getCur_ID() == ids[0]) {
                    color = color_one;
                     chip.setSELECTED_ID(0);
                     cur1_abr.setText(rate.getCur_Abbreviation());
                     cur1_name.setText(rate.getCur_Scale() + " " + rate.getCur_Name());
                }
                if(rate.getCur_ID() == ids[1]){
                    color = color_two;
                    chip.setSELECTED_ID(1);
                    cur2_abr.setText(rate.getCur_Abbreviation());
                    cur2_name.setText(rate.getCur_Scale() + " " + rate.getCur_Name());
                }
                if(rate.getCur_ID() == ids[2]) {
                    color = color_three;
                    chip.setSELECTED_ID(2);
                    cur3_abr.setText(rate.getCur_Abbreviation());
                    cur3_name.setText(rate.getCur_Scale() + " " + rate.getCur_Name());
                }
                if(rate.getCur_ID() == ids[3]) {
                    color = color_four;
                    chip.setSELECTED_ID(3);
                    cur4_abr.setText(rate.getCur_Abbreviation());
                    cur4_name.setText(rate.getCur_Scale() + " " + rate.getCur_Name());
                }
                chip.setChipBackgroundColorResource(color);
                chip.setCheckable(true);
                chip.setChecked(true);
            }
            chip.setOnCheckedChangeListener(listener);

            group.addView(chip);
            group.animate();
        }
        ll.setVisibility(View.VISIBLE);
        counter_checked = 4;
    }

    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            CustomChip this_chip = (CustomChip) compoundButton;
            TextView abr_txt, name_txt;
            View color;
            String temp_abr = "";
            String temp_name = "";
            if(b)
                counter_checked++;
            else {
                counter_checked--;
                colors.add(this_chip.getChipBackgroundColor());
                temp_ids.add(this_chip.getSELECTED_ID());
            }
            switch (temp_ids.get(temp_ids.size() - 1))
            {
                case 0: {abr_txt = getActivity().findViewById(R.id.cur_one_abr);
                        name_txt = getActivity().findViewById(R.id.cur_one_name);
                        color = color1;
                        break;}
                case 1: {abr_txt = getActivity().findViewById(R.id.cur_two_abr);
                        name_txt = getActivity().findViewById(R.id.cur_two_name);
                        color = color2;
                        break;}
                case 2: {abr_txt = getActivity().findViewById(R.id.cur_three_abr);
                        name_txt = getActivity().findViewById(R.id.cur_three_name);
                        color = color3;
                        break;}
                case 3: {abr_txt = getActivity().findViewById(R.id.cur_four_abr);
                        name_txt = getActivity().findViewById(R.id.cur_four_name);
                        color = color4;
                        break;}
                default: {abr_txt = getActivity().findViewById(R.id.cur_one_abr);
                        name_txt = getActivity().findViewById(R.id.cur_one_name);
                        color = color1;
                        break;}
            }
            if(counter_checked == 4) {
                for (int child = 0; child < group.getChildCount(); child++)
                {
                    Chip chip_temp = (Chip) group.getChildAt(child);
                    if(!chip_temp.isChecked())
                    {
                        chip_temp.setCheckable(false);
                    }
                }
            }
            else {
                for (int child = 0; child < group.getChildCount(); child++)
                {
                    Chip chip = (Chip) group.getChildAt(child);
                    chip.setCheckable(true);
                }
                fab.setVisibility(View.GONE);
            }
            if (counter_checked <= 4 && b)
            {
                String abr = compoundButton.getText().toString();
                for (Rate rate : currencies) {
                    if(rate.getCur_Abbreviation().equals(abr))
                    {
                        ids[temp_ids.get(temp_ids.size() - 1)] = rate.getCur_ID();
                        temp_abr = rate.getCur_Abbreviation();
                        temp_name = rate.getCur_Scale() + " " +  rate.getCur_Name();
                    }
                }
            }

            if(b) {
                this_chip.setChipBackgroundColor(colors.get(colors.size() - 1));
                this_chip.setSELECTED_ID(temp_ids.get(temp_ids.size() -1));
                colors.remove(colors.get(colors.size() - 1));
                temp_ids.remove(temp_ids.get(temp_ids.size() - 1));
                color.setVisibility(View.VISIBLE);
                abr_txt.setVisibility(View.VISIBLE);
                name_txt.setVisibility(View.VISIBLE);
                abr_txt.setText(temp_abr);
                name_txt.setText(temp_name);
            }
            else {
                this_chip.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                color.setVisibility(View.GONE);
                abr_txt.setVisibility(View.GONE);
                name_txt.setVisibility(View.GONE);
                abr_txt.setText("");
                name_txt.setText("");
            }
            if(counter_checked == 4) {
                int[] temp_ids = Setting.loadSetting(getContext());
                if(!Arrays.equals(ids, temp_ids))
                    fab.setVisibility(View.VISIBLE);
            }
            else {
                fab.setVisibility(View.GONE);
            }
        }
    };

    private void saveSetting()
    {
        if(counter_checked == 4)
            Setting.saveSetting(getContext(), ids);
    }
}

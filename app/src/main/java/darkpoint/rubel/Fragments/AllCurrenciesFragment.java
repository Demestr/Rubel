package darkpoint.rubel.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import darkpoint.rubel.APIFactory;
import darkpoint.rubel.Rate;
import darkpoint.rubel.CurrencyAdapter;
import darkpoint.rubel.R;
import darkpoint.rubel.ServerAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCurrenciesFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    int mPage;
    List<Rate> currencies = new ArrayList<>();

    public static AllCurrenciesFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AllCurrenciesFragment fragment = new AllCurrenciesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_currencies, container, false);

        final RecyclerView currency_list = view.findViewById(R.id.currencies_list);
        currency_list.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        currency_list.setLayoutManager(llm);

//        JSONGet jsonGet = new JSONGet("http://www.nbrb.by/API/ExRates/Rates?Periodicity=0");
//        currencies = jsonGet.getCurrencies();



        ServerAPI serverAPI = APIFactory.createApi();

        Call<List<Rate>> currenciesGSON = serverAPI.getAllRates(0);

        currenciesGSON.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                if(response.isSuccessful())
                {
                    CurrencyAdapter currencyAdapter = new CurrencyAdapter(response.body());
                    currency_list.setAdapter(currencyAdapter);
                    currencies = response.body();
                    Log.d("result", "onResponse: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                Log.d("result", "onResponse: " + t.getLocalizedMessage());
            }
        });


//        CurrencyAdapter currencyAdapter = new CurrencyAdapter(currencies);
//        currency_list.setAdapter(currencyAdapter);
        return view;
    }
}

package darkpoint.rubel;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {


    private List<Rate> rateList;
    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private List<Rate> initializeData(){
        rateList = new ArrayList<>();
//        rateList.add(new Rate(1, 1.988));
//        rateList.add(new Rate(1, 2.134));
//        rateList.add(new Rate(100, 3.321));
        return rateList;
    }

    public CurrencyAdapter(List<Rate> currencies)
    {
        this.rateList = currencies;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_currencies_item, viewGroup, false);
        CurrencyViewHolder currencyViewHolder = new CurrencyViewHolder(view);
        return currencyViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder currencyViewHolder, int i) {
        currencyViewHolder.currencyUnit.setText(String.valueOf(rateList.get(i).Cur_Scale));
        currencyViewHolder.currencyValue.setText(String.valueOf(rateList.get(i).Cur_OfficialRate));
        currencyViewHolder.cur_name.setText(rateList.get(i).Cur_Name);
        currencyViewHolder.currencyAbr.setText(rateList.get(i).Cur_Abbreviation);
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        TextView currencyUnit, currencyValue, cur_name, currencyAbr;
        CurrencyViewHolder(View itemView) {
            super(itemView);
            currencyUnit = (TextView)itemView.findViewById(R.id.item_scale);
            currencyValue = (TextView)itemView.findViewById(R.id.item_value);
            cur_name = itemView.findViewById(R.id.item_name);
            currencyAbr = itemView.findViewById(R.id.item_abr);
        }
    }
}

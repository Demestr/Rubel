package darkpoint.rubel;

import android.content.Context;

import java.util.List;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Artur Vasilov
 */
public class RetrofitCurrencyLoader extends Loader<List<RateShort>> {

    private final Call<List<RateShort>> mCall;

    @Nullable
    private List<RateShort> mRate;

    RetrofitCurrencyLoader(Context context, int id, String sDate, String eDate) {
        super(context);
        mCall = APIFactory.createApi().getRatePedriod(id, sDate, eDate);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mRate != null) {
            deliverResult(mRate);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mCall.enqueue(new Callback<List<RateShort>>() {
            @Override
            public void onResponse(Call<List<RateShort>> call, Response<List<RateShort>> response) {
                mRate = response.body();
                deliverResult(mRate);
            }

            @Override
            public void onFailure(Call<List<RateShort>> call, Throwable t) {
                deliverResult(null);
            }
        });
    }

    @Override
    protected void onStopLoading() {
        mCall.cancel();
        super.onStopLoading();
    }
}


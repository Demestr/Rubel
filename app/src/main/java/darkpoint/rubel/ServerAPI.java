package darkpoint.rubel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerAPI {

    @GET("Rates/{cur_id}")
    Call<Rate> getRate(@Path("cur_id") int cur_id);

    @GET("Currencies")
    Call<List<Currency>> getAllCurrencies();

    @GET("Currencies/{cur_id}")
    Call<Currency> getCurrency(@Path("cur_id") int cur_id);

    @GET("Rates/{cur_id}")
    Call<Rate> getRateOnDate(@Path("cur_id") int cur_id, @Query("onDate") String onDate);

    @GET("Rates/")
    Call<List<Rate>> getAllRates(@Query("Periodicity") int period);

    @GET("Rates/Dynamics/{id}")
    Call<List<RateShort>> getRatePedriod(@Path("id") int cur_id, @Query("startDate") String sDate, @Query("endDate") String eDate);
}

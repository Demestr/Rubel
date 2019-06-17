package darkpoint.rubel.network

import darkpoint.rubel.model.Currency
import darkpoint.rubel.model.Rate
import darkpoint.rubel.model.RateShort
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerAPI {

    @get:GET("Currencies")
    val allCurrenciesAsync: Call<List<Currency>>

    @GET("Rates/{cur_id}")
    fun getRateAsync(@Path("cur_id") cur_id: Int): Deferred<Rate>

    @GET("Currencies/{cur_id}")
    fun getCurrencyAsync(@Path("cur_id") cur_id: Int): Deferred<Currency>

    @GET("Rates/{cur_id}")
    fun getRateOnDateAsync(@Path("cur_id") cur_id: Int, @Query("onDate") onDate: String): Deferred<Rate>

    @GET("Rates/")
    fun getAllRatesAsync(@Query("Periodicity") period: Int): Deferred<List<Rate>>

    @GET("Rates/Dynamics/{id}")
    fun getRatePeriodAsync(@Path("id") cur_id: Int, @Query("startDate") sDate: String, @Query("endDate") eDate: String): Deferred<List<RateShort>>
}

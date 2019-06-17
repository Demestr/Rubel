package darkpoint.rubel.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object APIFactory {

    fun createApi(): ServerAPI {

        //val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.nbrb.by/API/ExRates/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        return retrofit.create(ServerAPI::class.java)
    }
}

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
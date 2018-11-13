package darkpoint.rubel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class APIFactory {

    public static ServerAPI createApi() {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.nbrb.by/API/ExRates/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ServerAPI.class);
    }
}

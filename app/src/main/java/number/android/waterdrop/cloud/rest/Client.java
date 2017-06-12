package number.android.waterdrop.cloud.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Routes REST_CLIENT;
    /**
     * Development Local Sever url
     */
    private static String LOC = "http://10.0.2.2/waterdrop/public/api/v1/";

    /**
     * Development URL
     */
    private static String DEV = "http://54.85.202.66/dev/waterdrop/public/api/v1/";

    /**
     * Production URL
     */
    private static String PRO = "http://54.85.202.66/waterdrop/public/api/v1/";

    static { setupRestClient(); }

    public static Routes get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                .addInterceptor(logging) //new ResponseInterceptor()
//                               .addInterceptor(new ResponseInterceptor())
                                .readTimeout(30, TimeUnit.SECONDS)
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEV)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        REST_CLIENT = retrofit.create(Routes.class);
    }
}
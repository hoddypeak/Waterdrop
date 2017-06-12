package number.android.waterdrop.cloud.rest;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResponseInterceptor implements Interceptor {

    public ResponseInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .header("Content-Type", "application/json")
                .header("API-KEY", "waterdrop")
                .build();

        Log.d("*********", "**********************       REQUEST START     **********************");
        Log.d("*********", "REQUEST URL -> "+request.url());
        Log.d("*********", "REQUEST HEADERS -> "+request.headers());
        Log.d("*********", "**********************       REQUEST END     **********************");

        Response response = chain.proceed(chain.request());
        Log.d("*********", "**********************       RESPONSE START     **********************");
        Log.d("*********", "RESPONSE CODE -> "+ response.code());
        Log.d("*********", "RESPONSE HEADERS -> "+ response.headers());
        Log.d("*********", "RESPONSE BODY : "+ response.body().string());
        Log.d("*********", "**********************       RESPONSE END     **********************");


        return chain.proceed(request);
    }
}
package co.myahia.rssreader.data.remote;

import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private static Retrofit retrofit;

    private static OkHttpClient getOkHttp() {
        Builder okHttpBuilder = new Builder();
        okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(50, TimeUnit.SECONDS);
        return okHttpBuilder.build();
    }

    private static Retrofit buildRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getBasUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttp()).build();
        }
        return retrofit;
    }

    @Singleton
    @Provides
    public RSSApi providesApiService() {
        if (retrofit == null) {
            buildRetrofit();
        }
        return (RSSApi) retrofit.create(RSSApi.class);
    }

    private static String getBasUrl() {
        return "https://newsapi.org/v2/";
    }
}

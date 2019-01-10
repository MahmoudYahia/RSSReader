package co.myahia.rssreader.data.remote;

import co.myahia.rssreader.data.remote.model.enumes.CategoryType;
import co.myahia.rssreader.data.remote.response.GetArticlesResponse;
import co.myahia.rssreader.data.remote.response.GetSourceProvidersResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RSSApi {
    @GET("top-headlines")
    Observable<GetArticlesResponse> getArticles(@Header("X-Api-Key") String key, @Query("sources") String str2);

    @GET("sources")
    Observable<GetSourceProvidersResponse> getProviders(@Header("X-Api-Key") String key, @Query("language") String lang, @Query("category") CategoryType cat);
}

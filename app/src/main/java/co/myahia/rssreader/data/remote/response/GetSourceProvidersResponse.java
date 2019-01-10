package co.myahia.rssreader.data.remote.response;

import co.myahia.rssreader.data.remote.model.NewsProvider;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetSourceProvidersResponse {
    @SerializedName("sources")
    @Expose
    private List<NewsProvider> sourcesProvider = null;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NewsProvider> getSourcesProvider() {
        return this.sourcesProvider;
    }

    public void setSourcesProvider(List<NewsProvider> sourcesProvider) {
        this.sourcesProvider = sourcesProvider;
    }
}

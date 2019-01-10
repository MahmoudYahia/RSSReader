package co.myahia.rssreader.data.remote.response;

import co.myahia.rssreader.data.remote.model.ApiArticle;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetArticlesResponse {
    @SerializedName("articles")
    @Expose
    private List<ApiArticle> apiArticles;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("totalResults")
    @Expose
    private Integer totalResults;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalResults() {
        return this.totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<ApiArticle> getApiArticles() {
        return this.apiArticles;
    }

    public void setApiArticles(List<ApiArticle> apiArticles) {
        this.apiArticles = apiArticles;
    }
}

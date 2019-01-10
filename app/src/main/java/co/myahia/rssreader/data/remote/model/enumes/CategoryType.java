package co.myahia.rssreader.data.remote.model.enumes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by M.YAHIA on 09/01/2019.
 */
public enum CategoryType {
    @Expose
    @SerializedName("entertainment")
    ENTERTAINMENT,
    @Expose
    @SerializedName("sports")
    SPORTS,
    @Expose
    @SerializedName("technology")
    TECHNOLOGY,
    @Expose
    @SerializedName("business")
    BUSINESS,
    @Expose
    @SerializedName("general")
    GENERAL,
    @Expose
    @SerializedName("health")
    HEALTH
}

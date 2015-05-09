package co.kotori.socialcount.api;

import retrofit.RestAdapter;
import retrofit.appengine.UrlFetchClient;
import retrofit.http.GET;
import retrofit.http.Query;

import java.io.Serializable;

/**
 * Created by kaiinui on 2015/05/09.
 */
public class FacebookApiFactory {
    public static FacebookApi makeApi() {
        return new RestAdapter.Builder()
                .setClient(new UrlFetchClient())
                .setEndpoint(FacebookApi.BASE_URL)
                .build()
                .create(FacebookApi.class);
    }

    public interface FacebookApi {
        public static final String BASE_URL = "http://graph.facebook.com";

        @GET("/")
        public FacebookCount getCount(@Query("id") String url);
    }

    public static class FacebookCount implements Serializable {
        public static final long serialVersionUID = 1L;

        public String id;
        public int shares;
        public int comments;
    }
}

package co.kotori.socialcount.api;

import retrofit.RestAdapter;
import retrofit.appengine.UrlFetchClient;
import retrofit.http.GET;
import retrofit.http.Query;

import java.io.Serializable;

/**
 * Created by kaiinui on 2015/05/09.
 */
public class TwitterApiFactory {
    public static TwitterApi makeApi() {
        return new RestAdapter.Builder()
                .setClient(new UrlFetchClient())
                .setEndpoint(TwitterApi.BASE_URL)
                .build()
                .create(TwitterApi.class);
    }

    public interface TwitterApi {
        public static final String BASE_URL = "http://urls.api.twitter.com/1";

        @GET("/urls/count.json")
        public TwitterCount getCount(@Query("url") String url);
    }

    public static class TwitterCount implements Serializable {
        public static final long serialVersionUID = 1L;

        public int count;
        public String url;
    }
}

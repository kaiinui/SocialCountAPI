package co.kotori.socialcount;

import co.kotori.socialcount.api.FacebookApiFactory;
import co.kotori.socialcount.api.PocketApiFactory;
import co.kotori.socialcount.api.TwitterApiFactory;
import co.kotori.socialcount.response.Counts;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiCacheControl;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.memcache.Expiration;
import org.slim3.memcache.Memcache;

/**
 * Created by kaiinui on 2015/05/09.
 */
@Api
@ApiCacheControl(type = "public", maxAge = SocialCountApi.MAX_AGE_SECOND)
public class SocialCountApi {
    public static final int MAX_AGE_SECOND = 10 * 60;
    public static final Expiration MAX_AGE_EXPIRE = Expiration.byDeltaSeconds(MAX_AGE_SECOND);

    @ApiMethod
    public Counts getCounts(@Named("url") String url) {
        final String twitterKey = "Twitter.get(" + url + ")";
        TwitterApiFactory.TwitterCount twitterCount = Memcache.get(twitterKey);
        if (twitterCount == null) {
            try {
                twitterCount = TwitterApiFactory.makeApi().getCount(url);
                Memcache.put(twitterKey, twitterCount, MAX_AGE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final String facebookKey = "Facebook.get(" + url + ")";
        FacebookApiFactory.FacebookCount facebookCount = Memcache.get(facebookKey);
        if (facebookCount == null) {
            try {
                facebookCount = FacebookApiFactory.makeApi().getCount(url);
                Memcache.put(facebookKey, facebookCount, MAX_AGE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final String pocketKey = "Pocket.get(" + url + ")";
        PocketApiFactory.PocketCount pocketCount = Memcache.get(pocketKey);
        if (pocketCount == null) {
            try {
                pocketCount = PocketApiFactory.makeApi().getCount(url);
                Memcache.put(pocketKey, pocketCount, MAX_AGE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final Counts counts =  new Counts();
        if (facebookCount != null) {
            counts.facebook = facebookCount.shares;
        }
        if (twitterCount != null) {
            counts.twitter = twitterCount.count;
        }
        if (pocketCount != null) {
            counts.pocket = pocketCount.count;
        }

        return counts;
    }
}

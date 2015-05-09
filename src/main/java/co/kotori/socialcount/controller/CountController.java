package co.kotori.socialcount.controller;

import co.kotori.socialcount.SocialCountApi;
import co.kotori.socialcount.response.Counts;
import com.google.appengine.repackaged.com.google.gson.Gson;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

/**
 * Created by kaiinui on 2015/05/09.
 */
public class CountController extends Controller {
    @Override
    protected Navigation run() throws Exception {
        final String url = asString("url");
        final String callback = asString("callback");

        final Counts counts = new SocialCountApi().getCounts(url);

        String payload = new Gson().toJson(counts);
        if (callback != null) {
            payload = callback + "(" + payload + ");";
        }

        response.setHeader("Cache-Control", "public, max-age=" + SocialCountApi.MAX_AGE_SECOND);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(payload);
        return null;
    }
}

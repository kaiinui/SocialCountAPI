package co.kotori.socialcount.api;

import retrofit.RestAdapter;
import retrofit.appengine.UrlFetchClient;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.io.*;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kaiinui on 2015/05/09.
 */
public class PocketApiFactory {
    public static PocketApi makeApi() {
        return new RestAdapter.Builder()
                .setClient(new UrlFetchClient())
                .setEndpoint(PocketApi.BASE_URL)
                .setConverter(new Converter() {
                    @Override
                    public Object fromBody(TypedInput body, Type type) throws ConversionException {
                        int count = 0;
                        try {
                            final String payload = fromStream(body.in());
                            // http://www.regexr.com/3avg7
                            final Pattern pattern = Pattern.compile("id=\"cnt\">(\\d*)<");
                            final Matcher matcher = pattern.matcher(payload);
                            if (matcher.find()) {
                                final String countStr = matcher.group(1);
                                count = Integer.parseInt(countStr);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                            throw new ConversionException(e.getMessage());
                        }

                        return new PocketCount(count);
                    }

                    @Override
                    public TypedOutput toBody(Object object) {
                        return null;
                    }

                    public String fromStream(InputStream in) throws IOException {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder out = new StringBuilder();
                        String newLine = System.getProperty("line.separator");
                        String line;
                        while ((line = reader.readLine()) != null) {
                            out.append(line);
                            out.append(newLine);
                        }
                        return out.toString();
                    }
                })
                .build()
                .create(PocketApi.class);
    }

    public interface PocketApi {
        public static final String BASE_URL = "http://widgets.getpocket.com/v1";

        @GET("/button?v=1&count=horizontal")
        public PocketCount getCount(@Query("url") String url);
    }

    public static class PocketCount implements Serializable {
        public static final long serialVersionUID = 1L;

        public PocketCount(int count) {
            this.count = count;
        }

        public int count;
    }
}

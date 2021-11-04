package ru.otus.hw12.util;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class WebServerUtil {

    public static final String COOKIE_HEADER = "Cookie";

    private static final String COOKIE_NAME_JSESSIONID = "JSESSIONID";

    private WebServerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String buildUrl(String host, String path, String... pathParams) {
        StringBuilder builder = new StringBuilder().append(host).append(path);
        Optional.ofNullable(pathParams).ifPresent(params -> Arrays.stream(params).forEach(param -> builder.append("/").append(param)));
        return builder.toString();
    }

    public static HttpCookie login(String url, String login, String password) throws Exception {
        HttpClient http = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .build();
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .uri(URI.create(String.format("%s?login=%s&password=%s", url, login, password)))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            List<HttpCookie> cookies = http.cookieHandler()
                    .map(h -> (CookieManager) h)
                    .map(m -> m.getCookieStore().getCookies()).orElseGet(ArrayList::new);
            return cookies.stream().filter(c -> c.getName().equalsIgnoreCase(COOKIE_NAME_JSESSIONID)).findFirst().orElse(null);
        }
        return null;
    }

}

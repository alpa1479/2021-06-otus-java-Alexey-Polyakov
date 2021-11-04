package ru.otus.hw12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw12.core.template.TemplateProcessor;
import ru.otus.hw12.crm.model.Client;
import ru.otus.hw12.crm.service.AuthenticationService;
import ru.otus.hw12.crm.service.DbServiceClient;
import ru.otus.hw12.server.ClientsWebServer;
import ru.otus.hw12.server.ClientsWebServerImpl;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static ru.otus.hw12.util.WebServerUtil.*;

@DisplayName("Clients web server should ")
class ClientsWebServerImplTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";

    private static final String LOGIN_URL = "login";
    private static final String API_CLIENTS_URL = "api/clients";

    private static final String DEFAULT_ADMIN_LOGIN = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin";
    private static final String INCORRECT_ADMIN_LOGIN = "unknown-admin";

    private static final long DEFAULT_CLIENT_ID = 1L;
    private static final String DEFAULT_CLIENT_NAME = "admin";
    private static final Client DEFAULT_CLIENT = new Client(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_NAME);

    private static Gson gson;
    private static HttpClient http;
    private static ClientsWebServer webServer;

    @BeforeAll
    static void setUp() throws Exception {
        http = HttpClient.newHttpClient();

        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);
        DbServiceClient dbServiceClient = mock(DbServiceClient.class);
        AuthenticationService authenticationService = mock(AuthenticationService.class);

        given(authenticationService.authenticate(DEFAULT_ADMIN_LOGIN, DEFAULT_ADMIN_PASSWORD)).willReturn(true);
        given(authenticationService.authenticate(INCORRECT_ADMIN_LOGIN, DEFAULT_ADMIN_PASSWORD)).willReturn(false);
        given(dbServiceClient.getClient(DEFAULT_CLIENT_ID)).willReturn(Optional.of(DEFAULT_CLIENT));

        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        webServer = new ClientsWebServerImpl(WEB_SERVER_PORT, gson, templateProcessor, dbServiceClient, authenticationService);
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName("should return session id when logging in with correct credentials")
    @Test
    void shouldReturnJSessionIdWhenLoggingInWithCorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), DEFAULT_ADMIN_LOGIN, DEFAULT_ADMIN_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();
    }

    @DisplayName("should not return session id when logging in with incorrect credentials")
    @Test
    void shouldNotReturnJSessionIdWhenLoggingInWithIncorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), INCORRECT_ADMIN_LOGIN, DEFAULT_ADMIN_PASSWORD);
        assertThat(jSessionIdCookie).isNull();
    }

    @DisplayName("should return correct data on request for client by id if admin logged in")
    @Test
    void shouldReturnCorrectUserWhenAuthorized() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), DEFAULT_ADMIN_LOGIN, DEFAULT_ADMIN_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, API_CLIENTS_URL, String.valueOf(DEFAULT_CLIENT_ID))))
                .setHeader(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body()).isEqualTo(gson.toJson(DEFAULT_CLIENT));
    }
}

package ru.otus.hw12.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.hw12.core.template.TemplateProcessor;
import ru.otus.hw12.core.util.FileSystemUtil;
import ru.otus.hw12.crm.controller.ClientsRestServlet;
import ru.otus.hw12.crm.controller.ClientsServlet;
import ru.otus.hw12.crm.controller.LoginServlet;
import ru.otus.hw12.crm.filter.AuthorizationFilter;
import ru.otus.hw12.crm.service.AuthenticationService;
import ru.otus.hw12.crm.service.DbServiceClient;

import java.util.Arrays;

public class ClientsWebServerImpl implements ClientsWebServer {

    private static final String STATIC_RESOURCES_DIR_NAME = "static";

    private final Gson gson;
    private final Server server;
    private final TemplateProcessor templateProcessor;
    private final DbServiceClient dbServiceClient;
    private final AuthenticationService authenticationService;

    public ClientsWebServerImpl(int port, Gson gson, TemplateProcessor templateProcessor,
                                DbServiceClient dbServiceClient, AuthenticationService authenticationService) {
        this.gson = gson;
        this.server = new Server(port);
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.authenticationService = authenticationService;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = initResourceHandler();
        ServletContextHandler servletContextHandler = initServletContextHandler();
        applySecurity(servletContextHandler, "/clients", "/api/clients/*");
        HandlerList handlers = new HandlerList(resourceHandler, servletContextHandler);
        server.setHandler(handlers);
    }

    private ResourceHandler initResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase(FileSystemUtil.toAbsolutePath(STATIC_RESOURCES_DIR_NAME));
        return resourceHandler;
    }

    private ServletContextHandler initServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new ClientsRestServlet(gson, dbServiceClient)), "/api/clients/*");
        return servletContextHandler;
    }

    private void applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authenticationService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
    }
}

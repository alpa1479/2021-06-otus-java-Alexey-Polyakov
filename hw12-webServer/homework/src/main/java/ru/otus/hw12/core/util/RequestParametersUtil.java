package ru.otus.hw12.core.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestParametersUtil {

    private static final int ID_PATH_PARAM_POSITION = 1;

    public static long getIdPathParameter(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String[] path = pathInfo.split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}

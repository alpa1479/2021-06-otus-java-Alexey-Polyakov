package ru.otus.hw12.core.template;

import java.io.IOException;
import java.util.Map;

public interface TemplateProcessor {

    String getPage(String filename, Map<String, Object> templateParameters) throws IOException;
}

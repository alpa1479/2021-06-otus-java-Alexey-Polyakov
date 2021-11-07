package ru.otus.hw12.core.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateProcessorImpl implements TemplateProcessor {

    private static final String UTF_8 = "UTF-8";

    private final Configuration configuration;

    public TemplateProcessorImpl(String templatesDir) {
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(TemplateProcessorImpl.class, templatesDir);
        configuration.setDefaultEncoding(UTF_8);
    }

    @Override
    public String getPage(String filename, Map<String, Object> templateParameters) throws IOException {
        try (Writer writer = new StringWriter()) {
            Template template = configuration.getTemplate(filename);
            template.process(templateParameters, writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}

package ru.otus.hw16.testcontainers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainersConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestContainersConfig.class);

    public static class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer> {
        private static CustomPostgreSQLContainer container;
        private static final String IMAGE_VERSION = "postgres:13";

        public CustomPostgreSQLContainer() {
            super(IMAGE_VERSION);
        }

        public static CustomPostgreSQLContainer getInstance() {
            if (container == null) {
                container = new CustomPostgreSQLContainer();
            }
            return container;
        }

        @Override
        public void start() {
            super.start();
            var url = container.getJdbcUrl() + "&stringtype=unspecified";
            System.setProperty("spring.datasource.demo-db.jdbcUrl", url);
            System.setProperty("spring.datasource.demo-db.username", container.getUsername());
            System.setProperty("spring.datasource.demo-db.password", container.getPassword());

            logger.info("postgres in docker started: url={}", url);
        }

        @Override
        public void stop() {
        }
    }
}

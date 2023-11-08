import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.TestcontainersConfiguration;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static org.apache.camel.util.CollectionHelper.mapOf;

public class PostgresTargetDatabaseTestResource <T extends GenericContainer> implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresTargetDatabaseTestResource.class);

    private static final int POSTGRES_PORT = 5432;
    private static final String POSTGRES_IMAGE = "docker.io/postgres:15.0";

    private static final String POSTGRES_TARGET_DB_NAME = "target_db";
    private static final String POSTGRES_TARGET_PASSWORD = "1234567@8_target";
    private static final String POSTGRES_TARGET_USER = "ETL_target_user";

    private GenericContainer<?> targetDbContainer;

    @Override
    public Map<String, String> start() {
        LOG.info(TestcontainersConfiguration.getInstance().toString());

        targetDbContainer = new GenericContainer<>(POSTGRES_IMAGE)
                .withExposedPorts(POSTGRES_PORT)
                .withEnv("POSTGRES_USER", POSTGRES_TARGET_USER)
                .withEnv("POSTGRES_PASSWORD", POSTGRES_TARGET_PASSWORD)
                .withEnv("POSTGRES_DB", POSTGRES_TARGET_DB_NAME)
                .withClasspathResourceMapping("init-target-db.sql", "/docker-entrypoint-initdb.d/init-target-db.sql", BindMode.READ_ONLY)
                .withLogConsumer(new Slf4jLogConsumer(LOG)).waitingFor(Wait.forListeningPort());
        targetDbContainer.start();

        // Print Postgres server connectivity information
        String targetJbdcUrl = String.format("jdbc:postgresql://%s:%s/%s",targetDbContainer.getHost(), targetDbContainer.getMappedPort(POSTGRES_PORT), POSTGRES_TARGET_DB_NAME);
        LOG.info("The test target_db could be accessed through the following JDBC url: "+ targetJbdcUrl);

        return mapOf("quarkus.datasource.target_db.jdbc.url", targetJbdcUrl);
    }

    @Override
    public void stop() {
        try {
            if (targetDbContainer != null) {
                targetDbContainer.stop();
            }
        } catch (Exception ex) {
            LOG.error("An issue occured while stopping the targetDbContainer", ex);
        }
    }

}

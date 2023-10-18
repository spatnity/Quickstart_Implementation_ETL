

import java.sql.Connection;
import java.sql.Statement;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class jdbcResource {

    @Inject
    @DataSource("source_db") 
    AgroalDataSource sourceDataSource;

    void createSourceTable(@Observes StartupEvent event) throws Exception {
        try (Connection con = sourceDataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                con.setAutoCommit(true);
                statement.execute("CREATE TABLE IF NOT EXISTS Source ("
                        + "id SERIAL PRIMARY KEY, "
                        + "hotel_name VARCHAR(255), "
                        + "price DECIMAL, "
                        + "review VARCHAR(255))");
            }
        }
    }

    @Inject
    @DataSource("target_db") 
    AgroalDataSource targetDataSource;

    void createTargetTable(@Observes StartupEvent event) throws Exception {
        try (Connection con = targetDataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                con.setAutoCommit(true);
                statement.execute("CREATE TABLE IF NOT EXISTS Target ("
                        + "id SERIAL PRIMARY KEY, "
                        + "hotel_name VARCHAR(255), "
                        + "price DECIMAL, "
                        + "review INT)");
            }
        }
    }
}

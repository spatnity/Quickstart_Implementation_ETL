import java.sql.ResultSet;
import java.sql.SQLException;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("reviewService")
@ApplicationScoped
@RegisterForReflection
public class JdbcResource {

    @Inject
    @DataSource("target_db")
    AgroalDataSource targetDb;

    String getHotelReviews() throws SQLException {

        StringBuilder sb = new StringBuilder();

        ResultSet rs = targetDb.getConnection().createStatement().executeQuery("SELECT (hotel_name, review) FROM Target");

        while (rs.next()) {
            sb.append(rs.getString(1));
        }

        return sb.toString();
    }
}

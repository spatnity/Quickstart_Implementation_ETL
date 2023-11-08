import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

@QuarkusTest
@QuarkusTestResource(PostgresSourceDatabaseTestResource.class)
@QuarkusTestResource(PostgresTargetDatabaseTestResource.class)
public class JdbcEtlTest {

    @Test
    public void etlBridgeShouldTransferValuesBetweenDatebases() {
        await().atMost(30L, TimeUnit.SECONDS).pollDelay(500, TimeUnit.MILLISECONDS).until(() -> {
            String hotelReviews = RestAssured
                    .get("/getHotelReviews")
                    .then()
                    .extract().asString();

            return "(Grand,1)(Middle,0)(Small,-1)".equals(hotelReviews);
        });
    }
}

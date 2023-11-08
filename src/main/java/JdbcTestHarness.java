import org.apache.camel.builder.RouteBuilder;

public class JdbcTestHarness extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("platform-http:/getHotelReviews")
                .bean("reviewService", "getHotelReviews");
    }
}

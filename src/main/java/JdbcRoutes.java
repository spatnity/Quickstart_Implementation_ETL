import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;

public class JdbcRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Define a mapping for the review values
        HashMap<String, Integer> reviewMapping = new HashMap<>();
        reviewMapping.put("best", 1);
        reviewMapping.put("good", 0);
        reviewMapping.put("worst", -1);

        from("timer://insertCamel?period=1000?repeatCount={{etl.timer.repeatcount}}")
            .setBody().simple("DELETE FROM Target")
            .to("jdbc:target_db")
            .setBody().simple("SELECT * FROM Source")
            .to("jdbc:source_db")
            .log("Selecting source data")
            .split(body())
            .process(exchange -> {
                Map<String, Object> sourceData = exchange.getIn().getBody(Map.class);
                String review = (String) sourceData.get("review");
                int mappedReview = reviewMapping.getOrDefault(review, 0);
                sourceData.put("review", mappedReview);
            })
            .setBody().simple("INSERT INTO Target (id, hotel_name, price, review) VALUES(${body[id]}, '${body[hotel_name]}', ${body[price]}, ${body[review]})")
            .to("jdbc:target_db")
            .log("Transformed and inserted data into target");
    }
}


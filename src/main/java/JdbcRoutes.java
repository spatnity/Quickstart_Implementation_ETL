import java.util.HashMap;
import java.util.List;
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

        from("timer://insertCamel?period=1000")
            .setBody().simple("SELECT * FROM Source")
            .to("jdbc:source_db")
            .log("Selecting source data")
            .process(exchange -> {
                List<Map<String, Object>> sourceData = exchange.getIn().getBody(List.class);

                // Iterate over the source data and apply the review mapping
                for (Map<String, Object> row : sourceData) {
                    String review = (String) row.get("review");
                    int mappedReview = reviewMapping.getOrDefault(review, 0);
                    row.put("review", mappedReview);
                }
            })
            .setBody().simple("INSERT INTO Target")
            .to("jdbc:target_db")
            .log("Transformed and inserted data into target");
    }
}


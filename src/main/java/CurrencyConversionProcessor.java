import java.math.BigDecimal;
import org.apache.camel.Processor;

import org.apache.camel.Exchange;

public class CurrencyConversionProcessor implements Processor {

      @Override
      public void process(Exchange exchange) throws Exception {
        // Extract the salary value from the source database
        BigDecimal originalSalary = exchange.getIn().getBody(BigDecimal.class);

        // Perform currency conversion logic here
        // You may fetch exchange rates, perform the conversion, and update the salary value

        // For example, convert USD to EUR with a fixed conversion rate of 0.85
        BigDecimal convertedSalary = originalSalary.multiply(new BigDecimal("0.85"));

        // Set the converted salary back in the body
        exchange.getIn().setBody(convertedSalary);
    }

}

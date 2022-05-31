package ca.josue.aws.lambda;

import ca.josue.aws.lambda.dto.Customer;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReadCustomerLambda {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public APIGatewayProxyResponseEvent getCustomers() throws JsonProcessingException {
        // scan all customers on DynamoDB
        ScanResult scanResult = dynamoDB.scan(new ScanRequest()
                .withTableName(System.getenv("CUSTOMERS_TABLE")));

        List<Customer> customers = scanResult.getItems().stream().map(item -> new Customer(
                item.get("firstName").getS(),
                item.get("lastName").getS(),
                Integer.parseInt(item.get("rewardPoints").getN())
        )).collect(Collectors.toList());

        String jsonOutput = objectMapper.writeValueAsString(customers);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(jsonOutput);
    }
}

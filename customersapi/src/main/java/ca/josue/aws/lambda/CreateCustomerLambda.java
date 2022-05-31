package ca.josue.aws.lambda;

import ca.josue.aws.lambda.dto.Customer;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class CreateCustomerLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent createCustomer(APIGatewayProxyRequestEvent request) throws JsonProcessingException {
        // get the body from the request
        Customer customer = objectMapper.readValue(request.getBody(), Customer.class);

        // create table if not exists
        Table table = dynamoDB.getTable(System.getenv("CUSTOMERS_TABLE"));

        // create item
        Item item = new Item()
                .withPrimaryKey("id", UUID.randomUUID().toString())
                .withString("firstName", customer.firstName)
                .withString("lastName", customer.lastName)
                .withInt("rewardPoints", customer.rewardPoints);

        // save item
        table.putItem(item);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(201)
                .withBody("Customer " + customer.firstName + " " + customer.lastName + " created");
    }
}

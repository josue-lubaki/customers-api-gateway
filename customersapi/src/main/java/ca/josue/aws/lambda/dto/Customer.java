package ca.josue.aws.lambda.dto;

public class Customer {
    public String firstName;
    public String lastName;
    public int rewardPoints;

    public Customer() {
    }

    public Customer(String firstName, String lastName, int rewardPoints) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.rewardPoints = rewardPoints;
    }
}

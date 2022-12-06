package com.levi9.springbootawslambda.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@Data
@DynamoDbBean
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunnyName {
    String id;
    String name;
    Instant generatedAt;

    @DynamoDbPartitionKey //primary key
    public String getId() {
        return id;
    }
}
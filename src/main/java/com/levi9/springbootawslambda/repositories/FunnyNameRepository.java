package com.levi9.springbootawslambda.repositories;

import com.levi9.springbootawslambda.StreamLambdaHandler;
import com.levi9.springbootawslambda.entities.FunnyName;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
public class FunnyNameRepository {
    public void saveName(FunnyName name) {
        StreamLambdaHandler.table.putItem(name);
    }

    public Optional<FunnyName> getName(String key) {
        Key key1 = Key.builder()
                .partitionValue(key)
                .build();
        return Optional.ofNullable(StreamLambdaHandler.table.getItem(r -> r.key(key1)));
    }
}
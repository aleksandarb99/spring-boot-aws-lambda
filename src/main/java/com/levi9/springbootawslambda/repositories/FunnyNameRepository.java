package com.levi9.springbootawslambda.repositories;

import com.levi9.springbootawslambda.entities.FunnyName;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Instant;
import java.util.Optional;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;

@Repository
public class FunnyNameRepository {
    private static final Region region = Region.EU_CENTRAL_1;
    private static final DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .build();

    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    private final TableSchema<FunnyName> tableSchema =
            StaticTableSchema.builder(FunnyName.class)
                    .newItemSupplier(FunnyName::new)
                    .addAttribute(String.class, a -> a.name("id")
                            .getter(FunnyName::getId)
                            .setter(FunnyName::setId)
                            .tags(primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("name")
                            .getter(FunnyName::getName)
                            .setter(FunnyName::setName))
                    .addAttribute(Instant.class, a -> a.name("generatedAt")
                            .getter(FunnyName::getGeneratedAt)
                            .setter(FunnyName::setGeneratedAt))

                    .build();
    private final DynamoDbTable<FunnyName> NAME_TABLE = enhancedClient.table("names", tableSchema);

    public void saveName(FunnyName name) {
        NAME_TABLE.putItem(name);
    }

    public Optional<FunnyName> getName(String key) {
        Key key1 = Key.builder()
                .partitionValue(key)
                .build();
        return Optional.ofNullable(NAME_TABLE.getItem(r -> r.key(key1)));
    }
}
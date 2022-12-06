package com.levi9.springbootawslambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.levi9.springbootawslambda.entities.FunnyName;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    public static DynamoDbTable<FunnyName> table;


    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(SpringBootAwsLambdaApplication.class);
            // If you are using HTTP APIs with the version 2.0 of the proxy model, use the getHttpApiV2ProxyHandler
            // method: handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(Application.class);
            DynamoDbClient ddb = DynamoDbClient.builder()
                    .region(Region.EU_CENTRAL_1)
                    .build();
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(ddb)
                    .build();

            TableSchema<FunnyName> tableSchema =
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

            table = enhancedClient.table("names", tableSchema);
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
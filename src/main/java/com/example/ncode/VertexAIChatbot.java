package com.example.ncode;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.aiplatform.v1.PredictRequest;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.PredictionServiceSettings;
import com.google.cloud.aiplatform.v1.PredictResponse;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class VertexAIChatbot {
    private static final String PROJECT_ID = "niveus-llm";
    private static final String LOCATION = "asia-south1";
    private static final String MODEL_ID = "text-bison@002";
    private static final String JSON_KEY_PATH = "C:/Users/sinch/IdeaProjects/ncode/src/main/resources/key.json";

    public static String getVertexAIResponse(String prompt) {
        try {
            // Load the credentials from the JSON key file
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(JSON_KEY_PATH));

            // Create the settings with explicit credentials provider
            PredictionServiceSettings settings = PredictionServiceSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            // Create client with settings that include credentials
            try (PredictionServiceClient predictionServiceClient = PredictionServiceClient.create(settings)) {
                String endpoint = String.format("projects/%s/locations/%s/publishers/google/models/%s",
                        PROJECT_ID, LOCATION, MODEL_ID);

                Struct inputStruct = Struct.newBuilder()
                        .putFields("prompt", Value.newBuilder().setStringValue(prompt).build())
                        .putFields("temperature", Value.newBuilder().setNumberValue(0.7).build())
                        .putFields("maxOutputTokens", Value.newBuilder().setNumberValue(200).build())
                        .build();

                Value instance = Value.newBuilder().setStructValue(inputStruct).build();
                List<Value> instances = List.of(instance);

                PredictRequest request = PredictRequest.newBuilder()
                        .setEndpoint(endpoint)
                        .addAllInstances(instances)
                        .build();

                PredictResponse response = predictionServiceClient.predict(request);

                if (!response.getPredictionsList().isEmpty()) {
                    Struct struct = response.getPredictions(0).getStructValue();
                    if (struct.containsFields("content")) {
                        return struct.getFieldsOrThrow("content").getStringValue();
                    }
                }
                return "No response received.";
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        String prompt = "write a java function to add 2 numbers";
        String response = getVertexAIResponse(prompt);
        System.out.println("Response: " + response);
    }
}
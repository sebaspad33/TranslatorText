package pe.edu.vallegrande.TranslateTextEdu.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
public class TranslatorDetecterService {

    private static final String TRANSLATOR_KEY = "48024b3b8b7444fc933567b3bd62b07d";
    private static final String TRANSLATOR_LOCATION = "westus";
    private static final String TRANSLATOR_ENDPOINT = "https://api.cognitive.microsofttranslator.com/detect?api-version=3.0";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;

    public TranslatorDetecterService() {
        this.client = new OkHttpClient();
    }

    public Mono<String> detectLanguage(String text) {
        return Mono.fromCallable(() -> {
            String requestBodyContent = "[{\"Text\": \"" + text + "\"}]";
            RequestBody body = RequestBody.create(requestBodyContent, JSON);

            Request request = new Request.Builder()
                    .url(TRANSLATOR_ENDPOINT)
                    .post(body)
                    .addHeader("Ocp-Apim-Subscription-Key", TRANSLATOR_KEY)
                    .addHeader("Ocp-Apim-Subscription-Region", TRANSLATOR_LOCATION)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    return parseDetectedLanguage(responseBody);
                } else {
                    log.error("Unexpected response code: {}", response.code());
                    throw new IOException("Unexpected response code: " + response.code());
                }
            }
        }).onErrorMap(error -> {
            log.error("Error detecting language: {}", error.getMessage());
            return new IOException("Error detecting language", error);
        });
    }

    private String parseDetectedLanguage(String responseBody) {
        JsonElement jsonElement = JsonParser.parseString(responseBody);

        if (!jsonElement.isJsonArray()) {
            log.error("Unexpected JSON format: not an array");
            throw new RuntimeException("Unexpected JSON format");
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        if (jsonArray.size() == 0) {
            log.error("Empty response array");
            throw new RuntimeException("Empty response array");
        }

        JsonObject detection = jsonArray.get(0).getAsJsonObject();
        if (!detection.has("language")) {
            log.error("No 'language' key found in the response");
            throw new RuntimeException("No 'language' key found in the response");
        }

        return detection.get("language").getAsString();
    }
}
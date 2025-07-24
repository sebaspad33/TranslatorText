package pe.edu.vallegrande.TranslateTextEdu.service;

import org.springframework.stereotype.Service;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TranslatorLenguajesService {

    private static final String TRANSLATOR_KEY = "48024b3b8b7444fc933567b3bd62b07d";
    private static final String TRANSLATOR_LOCATION = "westus";
    private static final String TRANSLATOR_ENDPOINT = "https://api.cognitive.microsofttranslator.com/";
    private static final String TRANSLATOR_ROUTE = "/languages?api-version=3.0";
    private static final String TRANSLATOR_URL = TRANSLATOR_ENDPOINT.concat(TRANSLATOR_ROUTE);

    private final OkHttpClient client;

    public TranslatorLenguajesService() {
        this.client = new OkHttpClient();
    }

    public Mono<Map<String, String>> getSupportedLanguages() {
        return Mono.fromCallable(() -> {
            Request request = new Request.Builder()
                    .url(TRANSLATOR_URL)
                    .get()
                    .addHeader("Ocp-Apim-Subscription-Key", TRANSLATOR_KEY)
                    .addHeader("Ocp-Apim-Subscription-Region", TRANSLATOR_LOCATION)
                    .addHeader("Content-type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    return parseLanguages(responseBody);
                } else {
                    throw new IOException("Unexpected response code: " + response);
                }
            }
        }).onErrorMap(error -> {
            log.error("Error fetching supported languages: {}", error.getMessage());
            return new IOException("Error fetching supported languages", error);
        });
    }

    private Map<String, String> parseLanguages(String responseBody) {
        Map<String, String> languagesMap = new HashMap<>();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonObject languages = jsonObject.getAsJsonObject("translation");

        for (Map.Entry<String, JsonElement> entry : languages.entrySet()) {
            String languageCode = entry.getKey();
            String languageName = entry.getValue().getAsJsonObject().get("name").getAsString();
            languagesMap.put(languageCode, languageName);
        }

        return languagesMap;
    }
}
package pe.edu.vallegrande.TranslateTextEdu.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pe.edu.vallegrande.TranslateTextEdu.model.Translation;
import pe.edu.vallegrande.TranslateTextEdu.repository.TranslationTextRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TranslatorTextService {

	private static final String TRANSLATOR_KEY = "48024b3b8b7444fc933567b3bd62b07d";
	private static final String TRANSLATOR_LOCATION = "westus";
	private static final String TRANSLATOR_ENDPOINT = "https://api.cognitive.microsofttranslator.com/";
	private static final String TRANSLATOR_ROUTE = "/translate?api-version=3.0";
	private static final String TRANSLATOR_URL = TRANSLATOR_ENDPOINT.concat(TRANSLATOR_ROUTE);

	private final OkHttpClient client;
	private final TranslationTextRepository translatorTextRepository;

	public TranslatorTextService(TranslationTextRepository translatorTextRepository) {
		this.client = new OkHttpClient();
		this.translatorTextRepository = translatorTextRepository;
	}

	public Mono<String> translateText(String text, String from, String to) {
		return Mono.fromCallable(() -> {
			MediaType mediaType = MediaType.parse("application/json");
			String requestBody = "[{\"Text\": \"" + text + "\"}]";
			String urlWithParams = TRANSLATOR_URL + "&from=" + from + "&to=" + to;
			RequestBody body = RequestBody.create(requestBody.getBytes(StandardCharsets.UTF_8), mediaType);
			Request request = new Request.Builder().url(urlWithParams).post(body)
					.addHeader("Ocp-Apim-Subscription-Key", TRANSLATOR_KEY)
					.addHeader("Ocp-Apim-Subscription-Region", TRANSLATOR_LOCATION)
					.addHeader("Content-type", "application/json").build();
			try (Response response = client.newCall(request).execute()) {
				if (response.isSuccessful()) {
					String responseBody = response.body().string();
					String translatedText = extractTranslatedText(responseBody);
					return translatedText != null ? translatedText : "Translation not found";
				} else {
					throw new IOException("Unexpected response code: " + response);
				}
			}
		}).onErrorMap(error -> {
			log.error("Error translating text: {}", error.getMessage());
			return new IOException("Error translating text", error);
		});
	}
	private String extractTranslatedText(String responseBody) {
		JsonArray translationsArray = JsonParser.parseString(responseBody).getAsJsonArray();
		if (translationsArray.size() > 0) {
			JsonArray translations = translationsArray.get(0).getAsJsonObject().getAsJsonArray("translations");
			if (translations.size() > 0) {
				JsonElement translation = translations.get(0);
				return translation.getAsJsonObject().get("text").getAsString();
			}
		}
		return null;
	}

	public Mono<Translation> editTranslation(Long id, String newText, String from, String to) {
        return translatorTextRepository.findById(id)
            .flatMap(translation -> {
                
                translation.setRequest_text(newText);
                translation.setFrom_lang(from);
                translation.setTo_lang(to);

                
                return translateText(newText, from, to)
                    .flatMap(translatedText -> {
                        
                        translation.setTranslated_text(translatedText);
                        return translatorTextRepository.save(translation); 
                    });
            });
    }

	public Flux<Translation> findAllActives() {
		return translatorTextRepository.findAllByStatusOrderByIdDesc("A");
	}	
	public Flux<Translation> findAllInactives() {
		return translatorTextRepository.findAllByStatusOrderByIdDesc("I");
	}
	public Mono<Translation> findById(Long id) {
		return translatorTextRepository.findById(id);
	}
	@Transactional
	public Mono<Translation> save(Translation translation) {
		translation.setStatus("A"); 
		return translatorTextRepository.save(translation);
	}

	public Mono<Translation> activate(Long id) {
		return translatorTextRepository.activeTranslation(id)
				.then(translatorTextRepository.findById(id));
	}
	public Mono<Translation> desactivar(Long id) {
		return translatorTextRepository.inactiveTranslation(id)
				.then(translatorTextRepository.findById(id));
	}

	public Mono<Boolean> deleteTranslation(Long id) {
		return translatorTextRepository.findById(id)
				.flatMap(existingTranslation ->
						translatorTextRepository.deleteById(id)
								.thenReturn(true)
				)
				.defaultIfEmpty(false);
	}
}
package pe.edu.vallegrande.TranslateTextEdu.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.TranslateTextEdu.model.TranslateRequestBody;
import pe.edu.vallegrande.TranslateTextEdu.repository.TranslationTextRepository;
import pe.edu.vallegrande.TranslateTextEdu.service.TranslatorTextService;
import pe.edu.vallegrande.TranslateTextEdu.model.Translation;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/translate")
@AllArgsConstructor
public class TranslatorTextRest {

	private final TranslatorTextService translatorTextService;
	private final TranslationTextRepository translatorTextRepository;

	@GetMapping("/ping")
	public Mono<ResponseEntity<String>> pingDatabase() {
		return Mono.fromCallable(() -> {
					return translatorTextRepository.count().block();
				})
				.map(count -> ResponseEntity.ok("Database connection is working."))
				.onErrorResume(error -> {
					log.error("Database connection error: {}", error.getMessage());
					return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Database connection failed."));
				});
	}

	@GetMapping("/last")
	public Mono<ResponseEntity<Translation>> getLastTranslation() {
		return translatorTextRepository.findTopByOrderByIdDesc()
				.map(translation -> ResponseEntity.ok().body(translation))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	@GetMapping("/all")
	public Flux<Translation> getAllTranslations() {
		return translatorTextRepository.findAll();
	}

	@GetMapping("/actives")
	public Flux<Translation> getAllActives() {
		return translatorTextService.findAllActives();
	}

	@GetMapping("/inactives")
	public Flux<Translation> getAllInactives() {
		return translatorTextService.findAllInactives();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Translation>> getTranslationById(@PathVariable Long id) {
		return translatorTextService.findById(id).map(translation -> ResponseEntity.ok().body(translation))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	@PostMapping
	public Mono<ResponseEntity<Translation>> translateText(@RequestBody TranslateRequestBody requestBody) {
		String text = requestBody.getText();
		String from = requestBody.getFrom();
		String to = requestBody.getTo();
		return translatorTextService.translateText(text, from, to)
				.flatMap(translatedText -> {
					Translation translation = new Translation();
					translation.setRequest_text(text);
					translation.setTranslated_text(translatedText);
					translation.setFrom_lang(from);
					translation.setTo_lang(to);

					return translatorTextService.save(translation)
							.map(savedTranslation -> ResponseEntity.status(HttpStatus.OK).body(savedTranslation));
				})
				.onErrorResume(error -> {
					log.error("Error translating text: {}", error.getMessage());
					return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
				});
	}

	@PutMapping("/edit/{id}")
	public Mono<ResponseEntity<Translation>> editTranslation(@PathVariable Long id,
															 @RequestBody Map<String, String> requestBody) {
		String newText = requestBody.get("request_text");
		String from = requestBody.get("from_lang");
		String to = requestBody.get("to_lang");

		return translatorTextService.editTranslation(id, newText, from, to)
				.map(updatedTranslation -> ResponseEntity.ok().body(updatedTranslation))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/activate/{id}")
	public Mono<ResponseEntity<Translation>> activate(@PathVariable Long id) {
		return translatorTextService.activate(id)
				.map(updatedTranslation -> ResponseEntity.ok(updatedTranslation))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/delete-log/{id}")
	public Mono<ResponseEntity<String>> deleteTranslation(@PathVariable Long id) {
		return translatorTextService.desactivar(id)
				.then(Mono.just(ResponseEntity.ok("Translation logically deleted successfully")))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Translation not found")));
	}

	@DeleteMapping("/delete-fis/{id}")
	public Mono<ResponseEntity<String>> deleteTranslationPermanently(@PathVariable Long id) {
		return translatorTextService.deleteTranslation(id)
				.map(success -> {
					if (success) {
						return ResponseEntity.ok("Translation fisic deleted successfully");
					} else {
						return ResponseEntity.notFound().build();
					}
				});
	}
}
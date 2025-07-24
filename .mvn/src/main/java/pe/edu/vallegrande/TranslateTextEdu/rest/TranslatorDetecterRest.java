package pe.edu.vallegrande.TranslateTextEdu.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.TranslateTextEdu.service.TranslatorDetecterService;
import reactor.core.publisher.Mono;
import java.util.Map;
@RestController
@RequestMapping("/detect")
@AllArgsConstructor
public class TranslatorDetecterRest {

    private final TranslatorDetecterService translatorDetecterService;

    @PostMapping
    public Mono<ResponseEntity<String>> detectLanguage(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        return translatorDetecterService.detectLanguage(text)
                .map(language -> ResponseEntity.ok().body(language))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).body("Error detecting language")));
    }
}

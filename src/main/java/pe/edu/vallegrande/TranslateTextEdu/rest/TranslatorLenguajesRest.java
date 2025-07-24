package pe.edu.vallegrande.TranslateTextEdu.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.vallegrande.TranslateTextEdu.service.TranslatorLenguajesService;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/lenguajes")
@AllArgsConstructor
public class TranslatorLenguajesRest {

    private final TranslatorLenguajesService translatorLenguajesService;

    @GetMapping
    public Mono<ResponseEntity<Map<String, String>>> getSupportedLanguages() {
        return translatorLenguajesService.getSupportedLanguages()
                .map(languagesMap -> ResponseEntity.ok().body(languagesMap))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500).body(null)));
    }

}

package pe.edu.vallegrande.TranslateTextEdu.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.TranslateTextEdu.model.Translation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TranslationTextRepository extends ReactiveCrudRepository<Translation, Long> {

	Flux<Translation> findAllByStatus(String status);

	@Query("SELECT * FROM translations WHERE status = :status ORDER BY id DESC")
	Flux<Translation> findAllByStatusOrderByIdDesc(String status);

	@Modifying
	@Query("update translations set status = 'I' where id = :id")
	Mono<Void> inactiveTranslation(Long id);

	@Modifying
	@Query("update translations set status = 'A' where id = :id")
	Mono<Void> activeTranslation(Long id);

	Mono<Translation> findTopByOrderByIdDesc();
}


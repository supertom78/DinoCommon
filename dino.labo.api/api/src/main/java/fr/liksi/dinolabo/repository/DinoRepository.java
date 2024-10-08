package fr.liksi.dinolabo.repository;

import fr.liksi.dinolabo.repository.model.Dino;
import fr.liksi.dinolabo.repository.model.DinoRecordDto;
import fr.liksi.dinolabo.configuration.headers.model.Parc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DinoRepository extends JpaRepository<Dino, UUID> {

    List<DinoRecordDto> findAllByParc(Parc parc);

}

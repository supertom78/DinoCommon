package fr.liksi.dino.repository;

import fr.liksi.dino.dto.DinoRecordDto;
import fr.liksi.dino.repository.model.Dino;
import fr.liksi.shared.kernel.Parc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DinoRepository extends JpaRepository<Dino, UUID> {

    List<DinoRecordDto> findAllByParc(Parc parc);

}

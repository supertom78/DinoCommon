package fr.liksi.dinorepo.repository;

import fr.liksi.dinorepo.repository.model.Dinotype;
import fr.liksi.dinorepo.repository.model.DinotypeRecordDto;
import fr.liksi.utils.model.Parc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DinotypeRepository extends JpaRepository<Dinotype, UUID> {

    List<DinotypeRecordDto> findAllByParc(Parc parc);

}

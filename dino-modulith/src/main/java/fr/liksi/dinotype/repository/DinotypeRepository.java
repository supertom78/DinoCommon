package fr.liksi.dinotype.repository;

import fr.liksi.dinotype.repository.model.Dinotype;
import fr.liksi.shared.kernel.Parc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DinotypeRepository extends JpaRepository<Dinotype, UUID> {

    List<Dinotype> findAllByParc(Parc parc);

}

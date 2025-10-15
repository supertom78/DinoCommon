package fr.liksi.dinotype.repository;

import fr.liksi.dinotype.repository.model.Dinotype;
import fr.liksi.shared.kernel.Era;
import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class DinotypeRepositoryTest {

    @Autowired
    private DinotypeRepository dinotypeRepository;

    @BeforeEach
    void setUp() {
        dinotypeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        dinotypeRepository.deleteAll();
    }

    @Test
    void when_findAllByParc_then_return_all_dinotypes_of_parc() {
        // Given
        Dinotype dinotype1 = new Dinotype();
        dinotype1.guid(UUID.randomUUID());
        dinotype1.era(Era.CRETACEOUS);
        dinotype1.species("Eoraptor");
        dinotype1.parc(Parc.HAWAII);
        dinotypeRepository.save(dinotype1);

        Dinotype dinotype2 = new Dinotype();
        dinotype2.guid(UUID.randomUUID());
        dinotype2.era(Era.CRETACEOUS);
        dinotype2.species("Eoraptor");
        dinotype2.parc(Parc.HAWAII);
        dinotypeRepository.save(dinotype2);

        Dinotype dinotype3 = new Dinotype();
        dinotype3.guid(UUID.randomUUID());
        dinotype3.era(Era.JURASSIC);
        dinotype3.species("T-REX");
        dinotype3.parc(Parc.BELLEILE);
        dinotypeRepository.save(dinotype3);

        // When
        var result = dinotypeRepository.findAllByParc(Parc.HAWAII);

        // Then
        assertEquals(2, result.size());
    }


}

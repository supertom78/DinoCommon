package fr.liksi.dino.repository;

import fr.liksi.shared.kernel.Parc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class DinoRepositoryTest {

    @Autowired
    private DinoRepository dinoRepository;


    @Test
    void when_findAllByParc_then_return_all_dino_of_parc() {
        // Given
        // When
        final var hawaiiDinos = dinoRepository.findAllByParc(Parc.HAWAII);
        final var belleIlle = dinoRepository.findAllByParc(Parc.BELLEILE);

        // Then
        assertEquals(5, hawaiiDinos.size());
        assertTrue(hawaiiDinos.stream().anyMatch(dino -> dino.parc().equals(Parc.HAWAII)));

        assertEquals(2, belleIlle.size());
        assertTrue(belleIlle.stream().anyMatch(dino -> dino.parc().equals(Parc.BELLEILE)));
    }


}

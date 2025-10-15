package fr.liksi.kpi.domain;

import fr.liksi.kpi.domain.api.CreateKpis;
import fr.liksi.kpi.domain.api.model.Kpis;
import fr.liksi.kpi.domain.spi.DinoInventory;
import fr.liksi.kpi.domain.spi.DinoTypeInventory;
import fr.liksi.kpi.domain.spi.model.Dino;
import org.springframework.stereotype.Service;


@Service
public class KpisCreator implements CreateKpis {

    private final DinoInventory dinoInventory;
    private final DinoTypeInventory dinoTypeInventory;

    public KpisCreator(DinoInventory dinoInventory, DinoTypeInventory dinoTypeInventory) {
        this.dinoInventory = dinoInventory;
        this.dinoTypeInventory = dinoTypeInventory;
    }

    @Override
    public Kpis consolidate() {
        final var dinotypes = dinoTypeInventory.getAll();
        final var dinos = dinoInventory.getAll();

        final var nbDinoCreated = dinos.stream()
                .filter(Dino::isAlive)
                .count();

        final var nbDinoInCreation = dinos.stream()
                .filter(Dino::isInCreation)
                .count();

        return new Kpis(dinotypes.size() - nbDinoCreated, nbDinoInCreation, nbDinoCreated);
    }
}

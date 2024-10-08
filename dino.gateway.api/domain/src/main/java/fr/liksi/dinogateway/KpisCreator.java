package fr.liksi.dinogateway;

import fr.liksi.ddd.DomainService;
import fr.liksi.dinogateway.api.CreateKpis;
import fr.liksi.dinogateway.spi.DinoInventory;
import fr.liksi.dinogateway.spi.DinoTypeInventory;


@DomainService
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

        return new Kpis(dinotypes.size()-nbDinoCreated, nbDinoInCreation, nbDinoCreated);
    }
}

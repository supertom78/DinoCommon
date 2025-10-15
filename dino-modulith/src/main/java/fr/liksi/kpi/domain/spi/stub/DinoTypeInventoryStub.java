package fr.liksi.kpi.domain.spi.stub;

import fr.liksi.kpi.domain.spi.DinoTypeInventory;
import fr.liksi.kpi.domain.spi.model.DinoType;

import java.util.List;

public class DinoTypeInventoryStub implements DinoTypeInventory {
    @Override
    public List<DinoType> getAll() {
        return List.of(
                new DinoType("Plateosaurus", "Plateosauridae"),
                new DinoType("Stegosaurus", "Stegosauridae"),
                new DinoType("Tyrannosaurus", "Tyrannosauridae"),
                new DinoType("Triceratops", "Ceratopsidae"),
                new DinoType("Velociraptor", "Dromaeosauridae")
        );
    }
}

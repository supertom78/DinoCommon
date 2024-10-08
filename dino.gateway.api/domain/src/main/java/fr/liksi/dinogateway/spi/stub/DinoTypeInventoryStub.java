package fr.liksi.dinogateway.spi.stub;

import fr.liksi.ddd.Stub;
import fr.liksi.dinogateway.DinoType;
import fr.liksi.dinogateway.spi.DinoTypeInventory;

import java.util.List;

@Stub
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

package fr.liksi.kpi.domain.spi.stub;

import fr.liksi.kpi.domain.spi.DinoInventory;
import fr.liksi.kpi.domain.spi.model.Dino;
import fr.liksi.kpi.domain.spi.model.DinoStatusEnum;

import java.util.List;

public class DinoInventoryStub implements DinoInventory {
    @Override
    public List<Dino> getAll() {
        return List.of(
                new Dino("didi", "Plateosaurus", DinoStatusEnum.EGG),
                new Dino("stego", "Stegosaurus", DinoStatusEnum.ADN),
                new Dino("tyty", "Tyrannosaurus", DinoStatusEnum.BABY),
                new Dino("tritri", "Triceratops", DinoStatusEnum.EGG),
                new Dino("veve", "Velociraptor", DinoStatusEnum.FROZEN)
        );
    }
}

package fr.liksi.dinogateway.spi.stub;

import fr.liksi.ddd.Stub;
import fr.liksi.dinogateway.Dino;
import fr.liksi.dinogateway.DinoStatusEnum;
import fr.liksi.dinogateway.spi.DinoInventory;

import java.util.List;

@Stub
public class DinoInventoryStub implements DinoInventory {
    @Override
    public List<Dino> getAll() {
        return List.of(
                new Dino("didi", "Plateosaurus", DinoStatusEnum.EGG),
                new Dino("stego","Stegosaurus", DinoStatusEnum.ADN),
                new Dino("tyty", "Tyrannosaurus", DinoStatusEnum.BABY),
                new Dino("tritri", "Triceratops", DinoStatusEnum.EGG),
                new Dino("veve","Velociraptor", DinoStatusEnum.FROZEN)
        );
    }
}

package fr.liksi.kpi.domain.spi.model;

public record Dino(String name, String species, DinoStatusEnum status) {

    public boolean isAlive() {
        return status == DinoStatusEnum.ADULT || status == DinoStatusEnum.BABY;
    }

    public boolean isInCreation() {
        return status == DinoStatusEnum.ADN || status == DinoStatusEnum.EGG;
    }
}

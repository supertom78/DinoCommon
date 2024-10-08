package fr.liksi.dinorepo.repository.model;

import fr.liksi.utils.model.Parc;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Dinotype {

    @Id
    private UUID guid;
    private String species;
    private String family;
    @Enumerated(EnumType.STRING)
    private Era era;
    @Enumerated(EnumType.STRING)
    private Parc parc;

    public UUID getGuid() {
        return guid;
    }

    public Dinotype guid(UUID guid) {
        this.guid = guid;
        return this;
    }

    public String getSpecies() {
        return species;
    }

    public Dinotype species(String species) {
        this.species = species;
        return this;
    }

    public String getFamily() {
        return family;
    }

    public Dinotype family(String family) {
        this.family = family;
        return this;
    }

    public Era getEra() {
        return era;
    }

    public Dinotype era(Era era) {
        this.era = era;
        return this;
    }

    public Parc getParc() {
        return parc;
    }

    public Dinotype parc(Parc parc) {
        this.parc = parc;
        return this;
    }
}

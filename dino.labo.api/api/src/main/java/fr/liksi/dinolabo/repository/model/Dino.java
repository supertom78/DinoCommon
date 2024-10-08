package fr.liksi.dinolabo.repository.model;

import fr.liksi.utils.model.Parc;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Dino {

    @Id
    private UUID guid;
    private String name;
    private String species;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Parc parc;

    public UUID getGuid() {
        return guid;
    }

    public Dino guid(UUID guid) {
        this.guid = guid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Dino name(String name) {
        this.name = name;
        return this;
    }

    public String getSpecies() {
        return species;
    }

    public Dino species(String species) {
        this.species = species;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Dino status(Status status) {
        this.status = status;
        return this;
    }

    public Parc getParc() {
        return parc;
    }

    public Dino parc(Parc parc) {
        this.parc = parc;
        return this;
    }
}

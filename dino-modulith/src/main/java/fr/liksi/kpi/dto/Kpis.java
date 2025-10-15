package fr.liksi.kpi.dto;

public record Kpis(long nbDinoToCreate, long nbDinoInCreation, long nbDinoCreated) {

    public Kpis(fr.liksi.kpi.domain.api.model.Kpis kpis) {
        this(kpis.nbDinoToCreate(), kpis.nbDinoInCreation(), kpis.nbDinoCreated());
    }
}

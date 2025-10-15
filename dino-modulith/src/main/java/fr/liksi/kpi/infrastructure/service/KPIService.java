package fr.liksi.kpi.infrastructure.service;

import fr.liksi.kpi.KpiApi;
import fr.liksi.kpi.dto.Kpis;
import fr.liksi.kpi.domain.api.CreateKpis;
import org.springframework.stereotype.Service;

@Service
public class KPIService implements KpiApi {

    private final CreateKpis createKpis;

    public KPIService(CreateKpis createKpis) {
        this.createKpis = createKpis;
    }

    @Override
    public Kpis getAllKpis() {
        return new Kpis(createKpis.consolidate());
    }
}

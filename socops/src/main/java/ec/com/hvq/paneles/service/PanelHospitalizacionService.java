package ec.com.hvq.paneles.service;

import ec.com.hvq.paneles.dto.HospitalizacionRowDto;
import ec.com.hvq.paneles.dto.UnidadDto;
import ec.com.hvq.paneles.repository.PanelHospitalizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PanelHospitalizacionService {

    private final PanelHospitalizacionRepository repository;

    public PanelHospitalizacionService(PanelHospitalizacionRepository repository) {
        this.repository = repository;
    }

    public List<UnidadDto> getUnidades() {
        return repository.findUnidades();
    }

    public List<HospitalizacionRowDto> getTablaPorUnidad(String sector) {
        return repository.findTablaBySector(sector);
    }
}

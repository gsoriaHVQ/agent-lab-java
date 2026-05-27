package ec.com.hvq.paneles.controller;

import ec.com.hvq.paneles.dto.HospitalizacionRowDto;
import ec.com.hvq.paneles.service.PanelHospitalizacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/paneles")
public class PanelHospitalizacionController {

    private final PanelHospitalizacionService service;

    public PanelHospitalizacionController(PanelHospitalizacionService service) {
        this.service = service;
    }

    @GetMapping("/panelH")
    public String panelHospitalizacion(Model model) {
        model.addAttribute("titulo", "PANEL HOSPITALIZACION");
        model.addAttribute("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("year", Year.now().getValue());
        model.addAttribute("unidades", service.getUnidades());
        return "paneles/panel_hospi";
    }

    @PostMapping("/panel_tabla")
    @ResponseBody
    public List<HospitalizacionRowDto> getTabla(@RequestParam("sector") String sector) {
        return service.getTablaPorUnidad(sector);
    }
}

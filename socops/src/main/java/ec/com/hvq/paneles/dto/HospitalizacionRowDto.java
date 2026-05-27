package ec.com.hvq.paneles.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HospitalizacionRowDto(
        String fecha,
        @JsonProperty("cd_atencion")
        Long cdAtencion,
        String habitacion,
        @JsonProperty("cd_paciente")
        Long cdPaciente,
        String exi,
        String exl,
        String cardio,
        String fisio,
        Integer interconsulta,
        String cirugia,
        @JsonProperty("fecha_alta")
        String fechaAlta,
        String enfermera,
        String pintar,
        String alta
) {
}

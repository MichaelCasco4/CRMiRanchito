package org.example.crmiranchito.model.reportes;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import org.openxava.annotations.Chart;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.ReadOnly;



@Getter
@Setter
@Entity

@View(members =
"Filtros { desde; hasta } " +
"Satisfaccion del cliente { encuestasPromedio } " +
"Uso de mesas { mesaUso } ")

public class ReporteSatisfaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    private LocalDate desde;

    @Required
    private LocalDate hasta;

    @Chart(type = ChartType.BAR)
    @ListProperties("categoria, promedio")
    @ReadOnly
    @ElementCollection
    private List<ItemEncuesta> encuestasPromedio;

    @Chart(type = ChartType.BAR)
    @ListProperties("mesa, cantidad")
    @ReadOnly
    @ElementCollection
    private List<ItemMesaUso> mesaUso;

    @PrePersist
    @PreUpdate
    private void CargarDatos(){

        encuestasPromedio = ReporteService.obtenerPromediosEncuestas(desde, hasta);
        mesaUso = ReporteService.obtenerUsoMesas(desde, hasta);

    }




}

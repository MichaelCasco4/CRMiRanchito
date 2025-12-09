package org.example.crmiranchito.model.encuesta;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.Stereotype;
import org.openxava.annotations.View;
import org.openxava.annotations.Tab;
import org.openxava.jpa.XPersistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter

@Tab(properties = "id")

@View(members =
        "Reporte: { " +
                "   graficoPromedioCliente; " +
                "   graficoPromedioMensual; " +
                "   graficoCategorias; " +
                "}"
)
public class ReporteSatisfaccion {

    @Id
    @Hidden
    private String id = UUID.randomUUID().toString();

    @Stereotype("CHART")
    @ReadOnly
    private String graficoPromedioCliente;

    @Stereotype("CHART")
    @ReadOnly
    private String graficoPromedioMensual;

    @Stereotype("CHART")
    @ReadOnly
    private String graficoCategorias;


    /* ==================================================
       FUNCIÓN PARA CARGAR Y GENERAR LOS GRÁFICOS
       ================================================== */
    public void cargarGraficos() {

        /* ==================================================
           1) PROMEDIO POR CLIENTE
           ================================================== */
        List<Object[]> clientes = XPersistence.getManager()
                .createQuery(
                        "SELECT r.cliente.nombre, " +
                                "AVG((e.calificacionGeneral + e.comida + e.atencion + e.tiempoServicio + e.ambiente) / 5.0) " +
                                "FROM Reserva r JOIN r.encuestas e " +
                                "GROUP BY r.cliente.nombre",
                        Object[].class
                )
                .getResultList();

        StringBuilder json1 = new StringBuilder("[['Cliente','Promedio']");

        for (Object[] row : clientes) {
            json1.append(",['")
                    .append(row[0])      // nombre con comillas
                    .append("',")
                    .append(row[1])      // NUMERO SIN COMILLAS
                    .append("]");
        }

        json1.append("]");
        graficoPromedioCliente = json1.toString();


        /* ==================================================
           2) PROMEDIO POR MES
           ================================================== */
        List<Object[]> meses = XPersistence.getManager()
                .createQuery(
                        "SELECT MONTH(e.fechaRespuesta), " +
                                "AVG((e.calificacionGeneral + e.comida + e.atencion + e.tiempoServicio + e.ambiente) / 5.0) " +
                                "FROM Reserva r JOIN r.encuestas e " +
                                "GROUP BY MONTH(e.fechaRespuesta)",
                        Object[].class
                )
                .getResultList();

        StringBuilder json2 = new StringBuilder("[['Mes','Promedio']");

        for (Object[] row : meses) {
            json2.append(",['Mes ")
                    .append(row[0])
                    .append("',")
                    .append(row[1])
                    .append("]");
        }

        json2.append("]");
        graficoPromedioMensual = json2.toString();


        /* ==================================================
           3) PROMEDIO POR CATEGORÍA
           ================================================== */
        Object[] categorias = (Object[]) XPersistence.getManager()
                .createQuery(
                        "SELECT AVG(e.comida), AVG(e.atencion), AVG(e.ambiente) " +
                                "FROM Reserva r JOIN r.encuestas e"
                )
                .getSingleResult();

        graficoCategorias =
                "[['Categoria','Promedio'], " +
                        "['Comida'," + categorias[0] + "]," +
                        "['Atencion'," + categorias[1] + "]," +
                        "['Ambiente'," + categorias[2] + "]]";
    }
}

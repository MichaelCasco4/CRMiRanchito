package org.example.crmiranchito.model.comunicacion;

import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.Tabs;
import org.openxava.annotations.View;

import javax.persistence.*;

@Entity
@Table(name = "canal_comunicacion")
@Getter
@Setter
@View(members = "Canal { nombre }")
@Tabs(@Tab(properties = "nombre"))
public class CanalComunicacion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Required
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
}

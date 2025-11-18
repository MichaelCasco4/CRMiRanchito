package org.example.crmiranchito.model.comunicacion;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.enums.CanalComunicacion;
import org.example.crmiranchito.model.Auditable;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.Tabs;
import org.openxava.annotations.View;

import javax.persistence.*;

@Entity
@Getter
@Setter
@View(members = "Canal { nombre } ")
@Tabs(@Tab(properties = "nombre"))
public class CanalComunicacionC extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Required
    private CanalComunicacion nombre;
}

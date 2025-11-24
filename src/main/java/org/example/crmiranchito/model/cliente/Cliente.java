package org.example.crmiranchito.model.cliente;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.View;
import org.openxava.annotations.Stereotype;

import javax.persistence.*;

@Entity
@Table(name = "cliente")
@View(name = "Simple",
        members = "nombre, telefono"
)
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String nombre;

    @Stereotype("PHONE")
    @Column(length = 50)
    private String telefono;

    // other fields (dni, email, etc.) and audit fields can remain here
}


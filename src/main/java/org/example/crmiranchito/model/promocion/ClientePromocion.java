package org.example.crmiranchito.model.promocion;


import lombok.Getter;
import lombok.Setter;
import org.example.crmiranchito.model.Auditable;
import org.example.crmiranchito.model.cliente.Cliente;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ClientePromocion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Promocion promocion;

    private boolean enviada;

    private java.util.Date fechaEnvio;
}

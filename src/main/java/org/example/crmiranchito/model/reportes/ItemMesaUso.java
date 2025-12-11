package org.example.crmiranchito.model.reportes;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter

public class ItemMesaUso {

    private String mesa;

    private Long cantidad;

    public ItemMesaUso() {}

    public ItemMesaUso(String mesa, Long cantidad) {
        this.mesa = mesa;
        this.cantidad = cantidad;
    }

}

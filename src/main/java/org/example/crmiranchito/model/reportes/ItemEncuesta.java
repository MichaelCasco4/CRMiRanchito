package org.example.crmiranchito.model.reportes;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter

public class ItemEncuesta {

    private String categoria;

    private Double promedio;

    public ItemEncuesta() {

    }

    public ItemEncuesta(String categoria, Double promedio){
        this.categoria = categoria;
        this.promedio = promedio;
    }


}

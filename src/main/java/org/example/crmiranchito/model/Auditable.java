package org.example.crmiranchito.model;


import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.ReadOnly;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class Auditable {

    @ReadOnly
    @Column(length=100)
    private String createdBy;

    @ReadOnly
    private LocalDateTime createdOn;

    @ReadOnly
    @Column(length = 100)
    private String modifiedBy;

    @ReadOnly
    private LocalDateTime modifiedOn;

    @PrePersist
    protected void onCreate(){

        this.createdOn = LocalDateTime.now();
        this.modifiedOn = this.createdOn;
        // TODO: asignar createdBy desde el contexto de seguridad
        // this.modifiedBy = SecurityUtils.getCurrentUsername();

    }

    @PreUpdate
    protected void onUpdate(){
        this.modifiedOn = LocalDateTime.now();
        // TODO: asignar modifiedBy desde el contexto de seguridad
        // this,modifiedBy = SecurityUtils.getCurrentUsername();
    }
}

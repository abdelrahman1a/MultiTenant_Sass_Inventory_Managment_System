package com.ecommerce.inventory_managment_system.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id" , nullable = false , updatable = false)
    private String id;

    @CreatedDate
    @Column(name = "created_at" , nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column()
    private LocalDateTime updatedAt;


    @CreatedBy
    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", insertable = false)
    private String updatedBy;


    @Column(name = "deleted", nullable = false)
    private Boolean deleted;


    @PrePersist
    protected  void onCreate()
    {
        if(this.deleted == null)
        {
            this.deleted = false;
        }
    }



}

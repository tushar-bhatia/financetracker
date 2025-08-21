package com.finance.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "category")
public class Category {

        @Positive(message = "Id must be a positive integer", groups = OnUpdate.class)
        @NotNull(message = "Id can not be null", groups = OnUpdate.class)
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false,  updatable = false, insertable = false)
        Integer id;

        @NotBlank(message = "Please provide a valid non empty name")
        @Column(nullable = false, unique = true)
        String name;

        public Category() {}

        public Category(String name) {
            this.name = name;
        }

        public Category(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
}

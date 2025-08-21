package com.finance.backend.dao;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {

    private Specification<T> specification;

    public SpecificationBuilder() {
        specification = Specification.where(null);
    }

    public SpecificationBuilder<T> add(Specification<T> newSpecification) {
        if (newSpecification != null) this.specification = this.specification.and(newSpecification);
        return this;
    }

    public Specification<T> build() {
        return this.specification;
    }
}

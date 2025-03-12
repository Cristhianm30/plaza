package com.pragma.powerup.application.mapper;


import com.pragma.powerup.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICategoryMapper {
    default String categoryToString(Category category) {
        return (category != null) ? category.getName() : null;
    }
}

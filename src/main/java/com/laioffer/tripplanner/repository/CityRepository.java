package com.laioffer.tripplanner.repository;

import com.laioffer.tripplanner.entity.CityEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CityRepository extends ListCrudRepository<CityEntity, Long> {
    Optional<CityEntity> findByName(String name);
}


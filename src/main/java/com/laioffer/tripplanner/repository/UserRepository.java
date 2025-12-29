package com.laioffer.tripplanner.repository;

import com.laioffer.tripplanner.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
}
package com.example.repository;

import com.example.enums.UserStatus;
import com.example.entity.ProfileEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {

    List<ProfileEntity> findAll();

    @Query("select count (userId) from ProfileEntity ")
    Long countByUserId();

    boolean existsByUserId(Long userId);

    Optional<ProfileEntity> findByUserId(Long userId);

    List<ProfileEntity> findByStatus(UserStatus status);
}

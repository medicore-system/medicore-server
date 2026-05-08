package com.medicore.api.repositories;

import com.medicore.api.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, String> {
}

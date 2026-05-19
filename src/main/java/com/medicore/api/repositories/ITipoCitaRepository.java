package com.medicore.api.repositories;

import com.medicore.api.entities.TipoCita;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoCitaRepository extends JpaRepository<TipoCita, Integer> {

}

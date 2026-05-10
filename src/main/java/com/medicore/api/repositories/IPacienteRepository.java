package com.medicore.api.repositories;

import com.medicore.api.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPacienteRepository  extends JpaRepository<Usuario, String> {
}

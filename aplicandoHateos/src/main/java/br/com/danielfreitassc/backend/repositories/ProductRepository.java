package br.com.danielfreitassc.backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.danielfreitassc.backend.models.ProductEntity;
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>{
    
}

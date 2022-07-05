package uz.pdp.springadvanced.springjwtwithdbemailauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.Product;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.projection.ProductProjection;

import java.util.UUID;

@RepositoryRestResource(path = "product", collectionResourceRel = "list", excerptProjection = ProductProjection.class)
public interface ProductRepository extends JpaRepository<Product, UUID> {
}

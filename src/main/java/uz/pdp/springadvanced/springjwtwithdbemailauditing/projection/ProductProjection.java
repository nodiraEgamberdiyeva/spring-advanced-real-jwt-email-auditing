package uz.pdp.springadvanced.springjwtwithdbemailauditing.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.Product;

@Projection(types = Product.class)
public class ProductProjection {
}

package front.inyecmotor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/producto/all") // La ruta de tu endpoint en el servidor Spring Boot
    Call<List<Producto>> getProductos();


    @GET("/marca/all")
    Call<List<Marca>> getMarcas(); // Define el nuevo endpoint para obtener las marcas

    @GET("/tipo/all")
    Call<List<TipoDTO>> getTipos(); // Define el nuevo endpoint para obtener las tipos

    @POST("/producto/crear")
    Call<ProductoCreateDTO> createProducto(@Body ProductoCreateDTO newProducto); // Define el nuevo endpoint para obtener las tipos
}


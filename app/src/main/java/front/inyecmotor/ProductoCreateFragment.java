package front.inyecmotor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import front.inyecmotor.ApiService;
import front.inyecmotor.Marca;
import front.inyecmotor.Modelo;
import front.inyecmotor.Producto;
import front.inyecmotor.ProductoCreateDTO;
import front.inyecmotor.Proveedor;
import front.inyecmotor.ProveedoresFragment;
import front.inyecmotor.R;
import front.inyecmotor.Tipo;
import front.inyecmotor.TipoDTO;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
public class ProductoCreateFragment extends Fragment {

    private static final String BASE_URL = "http://192.168.0.103:8080"; // Cambia a la URL de tu servidor

    private EditText etNombre, etCodigo, etStockMin, etStockMaximo, etStockActual, etPrecioVenta, etPrecioCompra;
    private Button btnSubmit,  btnSelectTipo;

    private ApiService apiService;

    //son los que nos traemos en la llamada al fetch son todos los all
    private List<Tipo> tipos;

    //son los que se seleccionaron
    private boolean[] selectedTipos;

    private List<Tipo> selectedProductTipos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crear_producto, container, false);

        etNombre = view.findViewById(R.id.etNombre);
        etCodigo = view.findViewById(R.id.etCodigo);
        etStockMin = view.findViewById(R.id.etStockMin);
        etStockMaximo = view.findViewById(R.id.etStockMaximo);
        etStockActual = view.findViewById(R.id.etStockActual);
        etPrecioVenta = view.findViewById(R.id.etPrecioVenta);
        etPrecioCompra = view.findViewById(R.id.etPrecioCompra);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSelectTipo = view.findViewById(R.id.btnSelectTipo);

        selectedTipos = new boolean[tipos.size()];
        selectedProductTipos = new ArrayList<Tipo>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Obtener tipos de productos al crear la vista
        fetchProductTipos();

        btnSelectTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiSelectDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProducto();
            }
        });

        return view;
    }

    private void fetchProductTipos() {
        Call<List<TipoDTO>> call = apiService.getTipos();
        call.enqueue(new Callback<List<TipoDTO>>() {
            @Override
            public void onResponse(Call<List<TipoDTO>> call, Response<List<TipoDTO>> response) {
                if (response.isSuccessful()) {
                    List<TipoDTO> tipoDtoList = response.body();
                    tipoDtoList.forEach(tipoDto -> {
                        Tipo newTipo =tipoDto.toTipo();
                        tipos.add(newTipo);});

                    selectedTipos = new boolean[tipos.size()];
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch product types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TipoDTO>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to fetch product types", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMultiSelectDialog() {

        String[] tipoNames = new String[tipos.size()];
        for (int i = 0; i < tipos.size(); i++) {
            tipoNames[i] = tipos.get(i).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selecciona los tipos de producto")
                .setMultiChoiceItems(tipoNames, selectedTipos, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedTipos[which] = isChecked;
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Implementa la lógica después de seleccionar los tipos y hacer clic en Aceptar
                        // Puedes recorrer selectedTipos para obtener los tipos seleccionados
                        selectedProductTipos.clear(); // Limpiar la lista de tipos seleccionados antes de agregar los nuevos
                        for (int i = 0; i < selectedTipos.length; i++) {
                            if (selectedTipos[i]) {
                                selectedProductTipos.add(tipos.get(i));
                            }
                        }
                        // Actualiza la interfaz de usuario si es necesario
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en Cancelar, si es necesario
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void createProducto() {
        String nombre = etNombre.getText().toString();
        String codigo = etCodigo.getText().toString();
        int stockMin = Integer.parseInt(etStockMin.getText().toString());
        int stockMaximo = Integer.parseInt(etStockMaximo.getText().toString());
        int stockActual = Integer.parseInt(etStockActual.getText().toString());
        double precioVenta = Double.parseDouble(etPrecioVenta.getText().toString());
        double precioCompra = Double.parseDouble(etPrecioCompra.getText().toString());

        // Aquí puedes ajustar cómo se envían los tipos seleccionados al backend
        List<Integer> tipoIds = new ArrayList<>();
        for (Tipo tipo : selectedProductTipos) {
            tipoIds.add(tipo.getId());
        }

        ProductoCreateDTO newProducto = new ProductoCreateDTO(999999, codigo,nombre, precioCompra,precioVenta, stockActual, stockMaximo,stockMin,tipoIds);

        Call<ProductoCreateDTO> call = apiService.createProducto(newProducto);
        call.enqueue(new Callback<ProductoCreateDTO>() {
            @Override
            public void onResponse(Call<ProductoCreateDTO> call, Response<ProductoCreateDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error al crear el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductoCreateDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Error al crear el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

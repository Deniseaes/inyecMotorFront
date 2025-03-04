package front.inyecmotor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductosFragment extends Fragment {

    private static final String BASE_URL = "http://192.168.0.103:8080"; // Cambia a la URL de tu servidor
    private OnButtonClickListener buttonClickListener;

    public interface OnButtonClickListener {
        void onButtonClick();
    }

    public void setButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productos_fragment, container, false);

        Button getProductosButton = view.findViewById(R.id.productoContainer);
        if (getProductosButton != null) {
            getProductosButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchProductos();
                }
            });
        }

        Button btnOpenProductForm = view.findViewById(R.id.nuevoProducto);
        btnOpenProductForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClick();
                }
            }
        });

        return view;
    }

    private void fetchProductos() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Producto>> call = apiService.getProductos();

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    //probando en consola la lista
                    System.out.println(productos);
                    StringBuilder toastMessage = new StringBuilder();
                    for (Producto producto : productos) {
                        toastMessage.append("ID: ").append(producto.getId())
                                .append(", Nombre: ").append(producto.getNombre())
                                .append(", Precio: ").append(producto.getPrecioCosto())
                                .append("\n");
                    }
                    Toast.makeText(getContext(), toastMessage.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Respuesta no exitosa", Toast.LENGTH_SHORT).show();
                    Log.d("HTTP Status Code", "Code: " + response.code());
                    Log.d("HTTP Response", "Response Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

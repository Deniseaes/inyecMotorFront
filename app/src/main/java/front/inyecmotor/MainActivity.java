package front.inyecmotor;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_CLOSE = 0;
    private static final int POS_MENU_PRINCIPAL = 1;
    private static final int POS_PRODUCTOS = 2;
    private static final int POS_PROVEEDORES = 3;
    private static final int POS_MODELOS = 4;
    private static final int POS_MARCAS = 5;
    private static final int POS_LOGOUT = 7;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_CLOSE),
                createItemFor(POS_MENU_PRINCIPAL).setChecked(true),
                createItemFor(POS_PRODUCTOS),
                createItemFor(POS_PROVEEDORES),
                createItemFor(POS_MODELOS),
                createItemFor(POS_MARCAS),
                new SpaceItem(260),
                createItemFor(POS_LOGOUT)
        ));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_MENU_PRINCIPAL);


    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.red))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.red))
                .withSelectedTextTint(color(R.color.red));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (position == POS_MENU_PRINCIPAL) {
            MenuPrincipalFragment menuPrincipalFragment = new MenuPrincipalFragment();
            transaction.replace(R.id.container, menuPrincipalFragment);
        } else if (position == POS_PRODUCTOS) {
            ProductosFragment productosFragment = new ProductosFragment();
            transaction.replace(R.id.container, productosFragment);
        } else if (position == POS_PROVEEDORES) {
            ProveedoresFragment proveedoresFragment = new ProveedoresFragment();
            transaction.replace(R.id.container, proveedoresFragment);
        } else if (position == POS_MODELOS) {
            ModelosFragment modelosFragment = new ModelosFragment();
            transaction.replace(R.id.container, modelosFragment);
        } else if (position == POS_MARCAS) {
            MarcasFragment marcasFragment = new MarcasFragment();
            transaction.replace(R.id.container, marcasFragment);
        } else if (position == POS_LOGOUT) {
            finish();
        }

        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openProductForm() {
        ProductoCreateFragment fragment = new ProductoCreateFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null); // Esto permite volver al Fragment anterior al presionar el botón Atrás
        fragmentTransaction.commit();
    }
}

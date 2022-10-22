package uteq.solutions.navdrawer.ui.productos;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.helper.DecimalDigitsInputFilter;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsListItem;

public class fragment_addproducto extends Fragment {

    private TabLayout tabLayout;
    FrameLayout frameLayout1;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private fragment_addproductopart1 addproductopart1;
    private fragment_addproductopart2 addproductopart2;


    ProgressBar pb ;

    public fragment_addproducto() {   }

    public static fragment_addproducto newInstance(String param1, String param2) {
        fragment_addproducto fragment = new fragment_addproducto();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View r=inflater.inflate(R.layout.fragment_addproducto, container, false);

        //viewPager = r.findViewById(R.id.viewPager);
        tabLayout = r.findViewById(R.id.tab_layout);
        frameLayout1=(FrameLayout)r.findViewById(R.id.frameLayout);

        addproductopart1 = fragment_addproductopart1.newInstance("Add");
        addproductopart2 = fragment_addproductopart2.newInstance("Add");

        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, addproductopart1);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();


        pb = (ProgressBar) r.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, tab.getPosition()==0?addproductopart1:addproductopart2,"Add");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return r;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.app_bar_search).setVisible(false);
        menu.findItem(R.id.mnu_bar_setting).setVisible(false);
        menu.findItem(R.id.mnu_bar_filtro).setVisible(false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //addproductopart2.cbEstadoly.setVisibility(View.GONE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_bar_save:
                addProducto();
                return true;
            default:
                break;
        }

        return false;
    }

    private boolean addProducto (){

        pb.setVisibility(View.VISIBLE);
        addproductopart1.txtbbarcodely.setError(null);
        addproductopart1.txtdescly.setError(null);       addproductopart1.cbSybCatly.setError(null);
        if( addproductopart2.CbUnidadLy!=null && addproductopart2.cbBodegaly!=null &&
                addproductopart2.cbEnvasely!=null && addproductopart2.cbICELy!=null) {
            addproductopart2.CbUnidadLy.setError(null);
            addproductopart2.cbBodegaly.setError(null);
            addproductopart2.cbEnvasely.setError(null);
            addproductopart2.cbICELy.setError(null);
        }

        String resp=validateParams();
        if(!resp.equals("")){
            pb.setVisibility(View.GONE);
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Error en el Formulario")
                    .setIcon(R.drawable.ic_baseline_error_24)
                    .setMessage(resp)
                    .setPositiveButton("Ok", null)
                    .show();

            return false;
        }

        Map<String, String> params = new HashMap();
        params.put("descripcion", addproductopart1.txtdesc.getText().toString().trim());
        params.put("barcode", addproductopart1.txtbarcode.getText().toString().trim());
        params.put("subcategoria", addproductopart1.itemSubCat.toString());
        params.put("impuesto", addproductopart1.itemImpuesto.toString());
        params.put("clase", addproductopart1.itemClase.toString());
        params.put("costo", addproductopart1.txtcosto.getText().toString().trim());
        params.put("precio_unidad", addproductopart1.txtpvp.getText().toString().trim());
        params.put("precio_mayorista", addproductopart1.txtpvpm.getText().toString().trim());

        if (addproductopart2.itemICE>0)     params.put("ice", addproductopart2.itemICE.toString());
        if (addproductopart2.itemUnidad>0)  params.put("unidad", addproductopart2.itemUnidad.toString());
        if (addproductopart2.itemEnvase>0)  params.put("envase", addproductopart2.itemEnvase.toString());
        params.put("unidadporenvase", "1");

        if(addproductopart2.txtdescuento!=null) {
            if (!addproductopart2.txtdescuento.getText().toString().equals(""))
                params.put("descuento", addproductopart2.txtdescuento.getText().toString().trim());
        }

        if(addproductopart2.txtstockmin!=null) {
            if (!addproductopart2.txtstockmin.getText().toString().equals(""))
                params.put("stockmin", addproductopart2.txtstockmin.getText().toString().trim());
        }

        if(addproductopart2.txtstockmax!=null) {
            if (!addproductopart2.txtstockmax.getText().toString().equals(""))
                params.put("stockmax", addproductopart2.txtstockmax.getText().toString().trim());
        }
        if (addproductopart2.itemBodega>0)   params.put("ubicacion", addproductopart2.itemBodega.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_AddProducto, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Snackbar snackbar = Snackbar.make(getView(), "Producto creado Correctamente!!", Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();

                        pb.setVisibility(View.GONE);
                    }
                },new HTTPErrorResponseDialog(getContext(), pb)
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UIHelper.getAuthHearders();
            }
        });

        pb.setVisibility(View.GONE);
        return true;
    }

    private String validateParams(){

        if(addproductopart1.txtbarcode.getText().toString().equals("")){
            addproductopart1.txtbbarcodely.setError("Ingrese un código"); return "Ingrese un Código"; }
        if(addproductopart1.txtdesc.getText().toString().equals("")) {
            addproductopart1.txtdescly.setError("Ingrese una descripción");  return "Ingrese una descripción";
        }
        if(addproductopart1.txtcosto.getText().toString().equals("")) {
            addproductopart1.txtcostoly.setError("Ingrese Valor Costo");  return "Ingrese Valor Costo";
        }
        if(addproductopart1.txtpvp.getText().toString().equals("")) {
            addproductopart1.txtpvply.setError("Ingrese Valor PVP");  return "Ingrese Valor PVP";
        }
        if(addproductopart1.txtpvpm.getText().toString().equals("")) {
            addproductopart1.txtpvpmly.setError("Ingrese Valor PVP Mayorista");  return "Ingrese Valor PVP Mayorista";
        }


        if(addproductopart1.itemSubCat<1) {
            addproductopart1.cbSybCatly.setError("Seleccione SubCategoría");
            return "Seleccione SubCategoría del producto";
        }

        if(addproductopart1.itemImpuesto<1) {
            addproductopart1.cbImpuesto.setError("Seleccione Impuesto");
            return "Seleccione Impuesto del producto";
        }



        return  "";
    }



}
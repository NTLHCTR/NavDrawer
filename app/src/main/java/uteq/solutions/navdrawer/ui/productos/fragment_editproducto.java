package uteq.solutions.navdrawer.ui.productos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.UIHelper;

public class fragment_editproducto extends Fragment {

    private TabLayout tabLayout;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private fragment_addproductopart1 addproductopart1;
    private fragment_addproductopart2 addproductopart2;

    ProgressBar pb ;

    public int IDProducto=-1;

    public fragment_editproducto() {   }

    public static fragment_editproducto newInstance(String param1, String param2) {
        fragment_editproducto fragment = new fragment_editproducto();
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

        View r=inflater.inflate(R.layout.fragment_editproducto, container, false);

        tabLayout = r.findViewById(R.id.tab_layout);
        frameLayout=(FrameLayout)r.findViewById(R.id.frameLayout);

        pb = (ProgressBar) r.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(addproductopart1!=null && addproductopart2!=null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frameLayout, tab.getPosition() == 0 ? addproductopart1 : addproductopart2);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {   }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {   }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_bar_save:
                editProducto();
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        IDProducto =getArguments().getInt("IDProducto");
        if(IDProducto>0)
            getProducto();
        else{
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Error")
                    .setIcon(R.drawable.ic_baseline_error_24)
                    .setMessage("Parámetro ID Producto NO Válido")
                    .setPositiveButton("Ok", null)
                    .show();
        }


    }

    private boolean editProducto (){
        pb.setVisibility(View.VISIBLE);
        addproductopart1.txtbbarcodely.setError(null);
        addproductopart1.txtdescly.setError(null);
        addproductopart1.cbSybCatly.setError(null);
        if(addproductopart2.CbUnidadLy!=null && addproductopart2.cbBodegaly!=null &&
                addproductopart2.cbEnvasely!=null &&  addproductopart2.cbEstadoly!=null ) {
            addproductopart2.CbUnidadLy.setError(null);
            addproductopart2.cbBodegaly.setError(null);
            addproductopart2.cbEnvasely.setError(null);
            addproductopart2.cbEstadoly.setError(null);
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
        if(!addproductopart1.txtbarcode.getText().toString().trim().equals(addproductopart1.barcodeOriginal))
             params.put("barcode", addproductopart1.txtbarcode.getText().toString().trim());

        params.put("subcategoria", addproductopart1.itemSubCat.toString());
        params.put("impuesto", addproductopart1.itemImpuesto.toString());
        params.put("clase", addproductopart1.itemClase.toString());
        params.put("costo", addproductopart1.txtcosto.getText().toString().trim());
        params.put("precio_unidad", addproductopart1.txtpvp.getText().toString().trim());
        params.put("precio_mayorista", addproductopart1.txtpvpm.getText().toString().trim());

        if (addproductopart2.itemUnidad>0)   params.put("unidad", addproductopart2.itemUnidad.toString());
        if (addproductopart2.itemEnvase>0)  params.put("envase", addproductopart2.itemEnvase.toString());
        params.put("unidadporenvase", "1");

        if(addproductopart2.txtstockmin!=null) {
            if (!addproductopart2.txtstockmin.getText().toString().equals(""))
                params.put("stockmin", addproductopart2.txtstockmin.getText().toString().trim());
        }

        if(addproductopart2.txtstockmax!=null) {
            if (!addproductopart2.txtstockmax.getText().toString().equals(""))
                params.put("stockmax", addproductopart2.txtstockmax.getText().toString().trim());
        }

        if (addproductopart2.itemBodega>0)   params.put("ubicacion", addproductopart2.itemBodega.toString());
        if (addproductopart2.itemEstado>0) params.put("estado", addproductopart2.itemEstado.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_EditProducto + IDProducto, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        addproductopart1.barcodeOriginal = addproductopart1.txtbarcode.getText().toString().trim();
                        Snackbar snackbar = Snackbar.make(getView(), "Producto editado Correctamente!!", Snackbar.LENGTH_LONG);
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

   private void showProduct(JSONObject jsonProducto){

       try {

           addproductopart1 = fragment_addproductopart1.newInstance("Upd",jsonProducto.getString("descripcion"),
                   jsonProducto.getString("barcode"),jsonProducto.getString("costo"),
                   jsonProducto.getString("precio_unidad"),jsonProducto.getString("precio_mayorista"),
                   jsonProducto.getInt("p_idcategoria"),jsonProducto.getInt("subcategoria"),
                   jsonProducto.getInt("impuesto"), jsonProducto.getInt("clase"));

           addproductopart2 = fragment_addproductopart2.newInstance("Upd",
                   jsonProducto.isNull("stockmin")?null:jsonProducto.getString("stockmin"),
                   jsonProducto.isNull("stockmax")?null:jsonProducto.getString("stockmax"),
                   jsonProducto.isNull("envase")?-1:jsonProducto.getInt("envase"),
                   jsonProducto.isNull("unidad")?-1:jsonProducto.getInt("unidad"),
                   jsonProducto.isNull("ubicacion")?-1:jsonProducto.getInt("ubicacion"),
                   jsonProducto.getInt("estado"));


           fragmentManager = getParentFragmentManager();
           fragmentTransaction = fragmentManager.beginTransaction();
           fragmentTransaction.replace(R.id.frameLayout, addproductopart1);
           fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
           fragmentTransaction.commit();


       } catch (JSONException e) {
           Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
       }
   }


    private boolean getProducto (){
        pb.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.GET, GlobalInfo.URL_GetProducto + IDProducto, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pb.setVisibility(View.GONE);
                        try {
                            JSONObject jsonProducto=  response.getJSONArray("Producto").getJSONObject(0);
                            showProduct(jsonProducto);

                        } catch (JSONException e) {
                            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }


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
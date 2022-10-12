package uteq.solutions.navdrawer.ui.productos;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.adapter.adapter_ListaProductos;
import uteq.solutions.navdrawer.dialogs.fragment_filtrosproductos;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.SwipeHelper;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsProducto;


public class fragment_ListaProductos extends Fragment implements  fragment_filtrosproductos.IfiltrosProductos
        , View.OnClickListener{

    adapter_ListaProductos adapter;
    RecyclerView rvListaProductos;
    TextView emptyView;

    FloatingActionButton fabbAddProd;

    Integer  itemCat=-1;
    Integer  itemSubCat=-1;
    Integer  itemBodega=-1;
    Integer  itemEstado=-1;
    Integer  itemOrden=-1;
    Integer  itemModoOrden=-1;
    Integer  itemFuente=1;
    String   Busq="";

    ProgressBar pb ;



    private void setReestablecerProducto(clsProducto item, Integer pos){
        if(item.ID<=0) {
            Toast.makeText(getContext(), "ID Producto NO Válido", Toast.LENGTH_SHORT).show();
            return;
        }

        pb.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap();
        params.put("estado", GlobalInfo.CONST_estadoVigenteProductos.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_ActualizaEstadoProducto + "/" + item.ID, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pb.setVisibility(View.GONE);
                        adapter.restoreItem(item, pos);
                        rvListaProductos.scrollToPosition(pos);
                    }
                },new HTTPErrorResponseDialog(getContext(), pb)
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UIHelper.getAuthHearders();
            }
        });
    }

    private void setEliminarProducto(clsProducto item, Integer pos){
        if(item.ID<=0) {
            Toast.makeText(getContext(), "ID Producto NO Válido", Toast.LENGTH_SHORT).show();
            return;
        }

        pb.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap();
        params.put("estado", GlobalInfo.CONST_estadoNOVigenteProductos.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_ActualizaEstadoProducto + "/" + item.ID, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pb.setVisibility(View.GONE);
                        adapter.removeItem(pos);
                        Snackbar snackbar = Snackbar.make(rvListaProductos, "Producto eliminado!.", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setReestablecerProducto(item, pos);
                                }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                },new HTTPErrorResponseDialog(getContext(), pb)
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UIHelper.getAuthHearders();
            }
        });
    }

    private void getListaProductos (){
        pb.setVisibility(View.VISIBLE);
        rvListaProductos.setAdapter(null);

        Map<String, String> params = new HashMap();
        params.put("fuente", itemFuente==-1?"1":itemFuente.toString());
        params.put("estado", itemEstado==-1?"1":itemEstado.toString());
        if(!Busq.equals("")) params.put("busq", Busq.trim());
        if(itemCat>0)  params.put("cat", itemCat.toString());
        if(itemSubCat>0)  params.put("subcat", itemSubCat.toString());
        if(itemBodega>0)  params.put("bod", itemBodega.toString());
        if(itemOrden>0)  params.put("orden", itemOrden.toString());
        if(itemModoOrden>0)  params.put("modoorden", itemModoOrden.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_ListaProductos, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<clsProducto> lstProductos = new ArrayList<clsProducto> ();
                        try{
                            JSONArray JSONlistaUsuarios=  response.getJSONArray("productos");
                            lstProductos = clsProducto.JsonObjectsBuild(JSONlistaUsuarios);

                            adapter = new adapter_ListaProductos(getContext(), lstProductos);
                            rvListaProductos.setAdapter(adapter);

                        }catch (JSONException e){
                           Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        pb.setVisibility(View.GONE);
                    }
                },new HTTPErrorResponseDialog(getContext(), pb)
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return UIHelper.getAuthHearders();
                    }
                });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Filtros por Default
        itemEstado=GlobalInfo.CONST_estadoVigenteProductos;
        itemOrden=1;    itemModoOrden=1;       itemFuente =1;
        Busq="";

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listaproductos, container, false);

        fabbAddProd = (FloatingActionButton) root.findViewById(R.id.fabbAddProd);
        pb = (ProgressBar) root.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);

        rvListaProductos = root.findViewById(R.id.rvListaProductos);
        rvListaProductos.setHasFixedSize(true);
        rvListaProductos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListaProductos.setItemAnimator(new DefaultItemAnimator());

        rvListaProductos.addItemDecoration(new DividerItemDecoration(rvListaProductos.getContext(), LinearLayout.VERTICAL));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(),
                R.anim.layout_animation_down_to_up);
        rvListaProductos.setLayoutAnimation(animation);



        SwipeHelper swipeHelper = new SwipeHelper(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_delete_24),
                        android.R.color.white,
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                final clsProducto item = adapter.getData().get(pos);
                                new MaterialAlertDialogBuilder(getContext())
                                        .setTitle("Eliminar Producto")
                                        .setIcon(R.drawable.ic_baseline_help_outline_24)
                                        .setMessage("¿Desea realmente  eliminar el Producto " + item.descripcion +"?")
                                        .setNegativeButton("NO",null)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                setEliminarProducto(item, pos);
                                            }
                                        })
                                        .show();
                               }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_edit_24) ,
                        android.R.color.white,
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                final clsProducto item = adapter.getData().get(pos);
                                Bundle bundle = new Bundle();
                                bundle.putInt("IDProducto", item.ID);
                                Navigation.findNavController(getView()).navigate(R.id.nav_EditProducto, bundle);
                            }
                        }
                ));

            }
        };
        swipeHelper.attachToRecyclerView(rvListaProductos);

        // emptyView = root.findViewById(R.id.emptyView);
        Busq="";
        getListaProductos();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabbAddProd.setOnClickListener(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.mnu_bar_save).setVisible(false);
        menu.findItem(R.id.mnu_bar_setting).setVisible(false);

        MenuItem mSearch = menu.findItem(R.id.app_bar_search);
        mSearch.setVisible(true);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Busq="";
                getListaProductos();
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.equals("")) {
                    Busq = query;
                    getListaProductos();
                }
               return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_bar_filtro:
                fragment_filtrosproductos dialogo =
                        fragment_filtrosproductos.newInstance(itemFuente, itemCat,itemSubCat,itemBodega,
                                itemEstado, itemOrden, itemModoOrden);
                dialogo.setTargetFragment(this, 300);
                dialogo.show(getParentFragmentManager(), "DialogoFiltros");
                return true;
            default:
                break;
        }

        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onFinishFiltrosProductosDialog(String accion, Integer Fuente, Integer Cat, Integer SubCat,
                                               Integer Bodega, Integer Estado, Integer Orden,
                                               Integer ModoOrden) {
        if(accion.equals("Filtrar")){
            itemFuente = Fuente;
            itemCat=Cat;        itemSubCat=SubCat;     itemBodega=Bodega;
            itemEstado=Estado;  itemOrden=Orden;       itemModoOrden=ModoOrden;
            getListaProductos();
        }
    }


    @Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(R.id.nav_AddProducto);
    }
}
package uteq.solutions.navdrawer.ui.clientes;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import uteq.solutions.navdrawer.adapter.adapter_ListaClientes;
import uteq.solutions.navdrawer.adapter.adapter_ListaProductos;
import uteq.solutions.navdrawer.dialogs.fragment_filtrosclientes;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.SwipeHelper;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsProducto;


public class fragment_ListaClientes extends Fragment implements  View.OnClickListener,
        fragment_filtrosclientes.IfiltrosClientes{

    adapter_ListaClientes adapter;
    RecyclerView rvListaClientes;
    TextView emptyView;

    FloatingActionButton fabbAddCliente;

    Integer  itemTipoCliente=-1;
    Integer  itemTipoID=-1;
    Integer  itemGenero=-1;
    Integer  itemEstado=-1;
    Integer  itemOrden=-1;
    Integer  itemModoOrden=-1;
    Integer itemFuente =-1;

    String   Busq="";
    ProgressBar pb ;

   private void setReestablecerCliente(clsCliente item, Integer pos){
        if(item.ID<=0) {
            Toast.makeText(getContext(), "ID Cliente NO Válido", Toast.LENGTH_SHORT).show();
            return;
        }

        pb.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap();
        params.put("estado", GlobalInfo.CONST_estadoVigenteClientes.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_ActualizaEstadoCliente + "/" + item.ID, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pb.setVisibility(View.GONE);
                        adapter.restoreItem(item, pos);
                        rvListaClientes.scrollToPosition(pos);
                    }
                },new HTTPErrorResponseDialog(getContext(), pb)
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UIHelper.getAuthHearders();
            }
        });
    }

    private void setEliminarCliente(clsCliente item, Integer pos){
        if(item.ID<=0) {
            Toast.makeText(getContext(), "ID Cliente NO Válido", Toast.LENGTH_SHORT).show();
            return;
        }

        pb.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap();
        params.put("estado", GlobalInfo.CONST_estadoNOVigenteClientes.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_ActualizaEstadoCliente + "/" + item.ID, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pb.setVisibility(View.GONE);
                        adapter.removeItem(pos);
                        Snackbar snackbar = Snackbar.make(rvListaClientes, "Cliente eliminado!.", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setReestablecerCliente(item, pos);
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

    private void getListaClientes (){
        pb.setVisibility(View.VISIBLE);
        rvListaClientes.setAdapter(null);


        Map<String, String> params = new HashMap();
        params.put("fuente", itemFuente==-1?"1":itemFuente.toString());
        params.put("estado", itemEstado==-1?"1":itemEstado.toString());
        if(!Busq.equals("")) params.put("busq", Busq.trim());
        if(itemGenero>0) params.put("gen", itemGenero.toString());
        if(itemTipoCliente>0)  params.put("tipocliente", itemTipoCliente.toString());
        if(itemTipoID>0)  params.put("tipoid", itemTipoID.toString());
        if(itemOrden>0)  params.put("orden", itemOrden.toString());
        if(itemModoOrden>0)  params.put("modoorden", itemModoOrden.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_ListaClientes, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<clsCliente> lstClientes = new ArrayList<clsCliente> ();
                        try{
                            JSONArray JSONlistaUsuarios=  response.getJSONArray("clientes");
                            lstClientes = clsCliente.JsonObjectsBuild(JSONlistaUsuarios);

                            adapter = new adapter_ListaClientes(getContext(), lstClientes);
                            rvListaClientes.setAdapter(adapter);

                        }catch (JSONException e){
                            e.printStackTrace();
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
        itemEstado=GlobalInfo.CONST_estadoVigenteClientes;
        itemOrden=1;    itemModoOrden=1;       itemFuente =1;
        Busq="";

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listaclientes, container, false);

        fabbAddCliente = (FloatingActionButton) root.findViewById(R.id.fabbAddProd);
        pb = (ProgressBar) root.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);

        rvListaClientes = root.findViewById(R.id.rvListaClientes);
        rvListaClientes.setHasFixedSize(true);
        rvListaClientes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListaClientes.setItemAnimator(new DefaultItemAnimator());

        rvListaClientes.addItemDecoration(new DividerItemDecoration(rvListaClientes.getContext(), LinearLayout.VERTICAL));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(),
                R.anim.layout_animation_down_to_up);
        rvListaClientes.setLayoutAnimation(animation);



        SwipeHelper swipeHelper = new SwipeHelper(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_delete_24),
                        android.R.color.white,
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                final clsCliente item = adapter.getData().get(pos);
                                new MaterialAlertDialogBuilder(getContext())
                                        .setTitle("Eliminar Cliente")
                                        .setIcon(R.drawable.ic_baseline_help_outline_24)
                                        .setMessage("¿Desea realmente  eliminar el Cliente " + item.nombre +"?")
                                        .setNegativeButton("NO",null)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                setEliminarCliente(item, pos);
                                            }
                                        })
                                        .show();
                               }
                        }
                ));
                underlayButtons.add(new UnderlayButton(
                        "Edit",
                        ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_edit_24) ,
                        android.R.color.white,
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                final clsCliente item = adapter.getData().get(pos);
                                Bundle bundle = new Bundle();
                                bundle.putInt("IDCliente", item.ID);
                                Navigation.findNavController(getView()).navigate(R.id.nav_EditCliente, bundle);
                            }
                        }
                ));

            }
        };
        swipeHelper.attachToRecyclerView(rvListaClientes);

        // emptyView = root.findViewById(R.id.emptyView);
        Busq="";
        getListaClientes();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabbAddCliente.setOnClickListener(this);

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
                getListaClientes();
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.equals("")) {
                    Busq = query;
                    getListaClientes();
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
                fragment_filtrosclientes dialogo =
                        fragment_filtrosclientes.newInstance(itemFuente, itemTipoCliente,itemTipoID
                                ,itemGenero, itemEstado, itemOrden, itemModoOrden);
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
    public void onClick(View view) {
       Navigation.findNavController(view).navigate(R.id.nav_AddCliente);
    }

    @Override
    public void onFinishFiltrosClientesDialog(String accion, Integer Fuente, Integer TipoCliente, Integer TipoID, Integer Genero, Integer Estado, Integer Orden, Integer ModoOrden) {
        if(accion.equals("Filtrar")){
            itemFuente = Fuente;
            itemTipoCliente=TipoCliente;        itemTipoID=TipoID;     itemGenero=Genero;
            itemEstado=Estado;  itemOrden=Orden;       itemModoOrden=ModoOrden;
            getListaClientes();
        }
    }
}
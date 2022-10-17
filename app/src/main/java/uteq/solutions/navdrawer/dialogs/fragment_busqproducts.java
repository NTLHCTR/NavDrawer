package uteq.solutions.navdrawer.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
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
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.adapter.AdapterItemProductoClickListener;
import uteq.solutions.navdrawer.adapter.adapter_BusqClientes;
import uteq.solutions.navdrawer.adapter.adapter_BusqProducts;
import uteq.solutions.navdrawer.adapter.adapter_ListaProductos;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsListItem;
import uteq.solutions.navdrawer.model.clsProducto;

public class fragment_busqproducts extends DialogFragment implements AdapterItemProductoClickListener {

    Button btCancelar, tbBuscar;
    MaterialButtonToggleGroup tgModoOrden;
    AutoCompleteTextView cbOrden;

    Integer  itemOrden;
    Integer  itemModoOrden;
    ProgressBar pb ;
    TextView txtBusq;

    adapter_BusqProducts adapter;
    RecyclerView rvListaProducts;

    @Override
    public void onClick(View view, clsProducto producto) {
        IBusqProductos listener = (IBusqProductos) getTargetFragment();
        listener.onFinishBusqProductsDialog("OK", producto);
        dismiss();
    }

    public interface IBusqProductos {
        void onFinishBusqProductsDialog(String accion, clsProducto producto);
    }

    public static fragment_busqproducts newInstance()
    {
        fragment_busqproducts f = new fragment_busqproducts();
        Bundle args = new Bundle();
       /* args.putInt("itemFuente",itemFuente);
        args.putInt("itemTipoCliente",itemTipoCliente);
        args.putInt("itemTipoID",itemTipoID);
        args.putInt("itemGenero",itemGenero);
        args.putInt("itemEstado",itemEstado);
        args.putInt("itemOrden",itemOrden);
        args.putInt("itemModoOrden",itemModoOrden);*/
        f.setArguments(args);
        return f;
    }

    public fragment_busqproducts() {    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args=getArguments();
        /*itemFuente=args.getInt("itemFuente");
        itemTipoCliente=args.getInt("itemTipoCliente");
        itemTipoID=args.getInt("itemTipoID");
        itemGenero=args.getInt("itemGenero");
        itemEstado=args.getInt("itemEstado");
        itemOrden=args.getInt("itemOrden");
        itemModoOrden=args.getInt("itemModoOrden");*/

        return crearDialogoFiltrosClientes();
    }


    private Dialog crearDialogoFiltrosClientes(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_busqproducts,null);

        tbBuscar = (Button) v.findViewById(R.id.btBusqPBuscar);
        btCancelar = (Button) v.findViewById(R.id.btCancelarBusqP);
        txtBusq = (TextView) v.findViewById(R.id.txtBusqPBusq);

        cbOrden = (AutoCompleteTextView) v.findViewById(R.id.actvCampoOrden);
        tgModoOrden = (MaterialButtonToggleGroup) v.findViewById(R.id.tgModoOrden);


        rvListaProducts = v.findViewById(R.id.rvListaProductos);
        rvListaProducts.setHasFixedSize(true);
        rvListaProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListaProducts.setItemAnimator(new DefaultItemAnimator());

        rvListaProducts.addItemDecoration(new DividerItemDecoration(rvListaProducts.getContext(), LinearLayout.VERTICAL));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(),
                R.anim.layout_animation_down_to_up);
        rvListaProducts.setLayoutAnimation(animation);


        itemOrden=6; itemModoOrden=1;

        pb = (ProgressBar) v.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);

        builder.setView(v);
        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<clsListItem> listCampoOrden = new ArrayList<clsListItem>();
        listCampoOrden.add(new clsListItem(1,"Descripción",R.drawable.ic_baseline_calendar_month_24));
        listCampoOrden.add(new clsListItem(2,"Categoría", R.drawable.ic_baseline_person_24));
        listCampoOrden.add(new clsListItem(3,"Fecha Creación",R.drawable.ic_baseline_attach_money_24));
        listCampoOrden.add(new clsListItem(4,"Clase", R.drawable.ic_baseline_person_24));
        listCampoOrden.add(new clsListItem(5,"Precio", R.drawable.ic_baseline_trending_up_24));
        UIHelper.fillCombo(cbOrden, listCampoOrden, 1);
        tgModoOrden.check(R.id.btUpOrderListaClientes);

        return inflater.inflate(R.layout.fragment_filtrosclientes, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* tgClase.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked) {
                    if (checkedId == R.id.btBusqPBien)
                        itemClase = 1;
                    else if (checkedId == R.id.btBusqPServicio)
                        itemClase = 2;
                    else
                        itemClase = 0;
                }
            }
        });*/


        tgModoOrden.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId==R.id.btUpOrderListaClientes)     itemModoOrden = isChecked?1:2;
            }
        });

        cbOrden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemOrden = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });


        tbBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListaProductos ();
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IBusqProductos listener = (IBusqProductos) getTargetFragment();
                listener.onFinishBusqProductsDialog("Cancelar", null);
                dismiss();
            }
        });
    }

    private void loadListOnRV (ArrayList<clsProducto> lstProductos){
        adapter = new adapter_BusqProducts(getContext(), lstProductos);
        rvListaProducts.setAdapter(adapter);
       adapter.setClickListener(this);
    }

    private void getListaProductos (){
        String Busq= txtBusq.getText().toString().trim();
        if(Busq.equals("")){
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Error en el Formulario")
                    .setIcon(R.drawable.ic_baseline_error_24)
                    .setMessage("Ingrese un texto a buscar")
                    .setPositiveButton("Ok", null)
                    .show();
            return;
        }

        pb.setVisibility(View.VISIBLE);
        rvListaProducts.setAdapter(null);

        Map<String, String> params = new HashMap();
        params.put("busq", Busq.trim());
       // if(itemClase>0)  params.put("clase", itemClase.toString());
        if(itemOrden>0)  params.put("orden", itemOrden.toString());
        if(itemModoOrden>0)  params.put("modoorden", itemModoOrden.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_BusqProductos, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<clsProducto> lstProductos = new ArrayList<clsProducto> ();
                        try{

                            JSONArray JSONlistaUsuarios=  response.getJSONArray("productos");
                            lstProductos = clsProducto.JsonObjectsBuild(JSONlistaUsuarios);

                            loadListOnRV (lstProductos);

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

}
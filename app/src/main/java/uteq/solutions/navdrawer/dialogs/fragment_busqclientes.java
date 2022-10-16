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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.adapter.AdapterItemClickListener;
import uteq.solutions.navdrawer.adapter.adapter_BusqClientes;
import uteq.solutions.navdrawer.adapter.adapter_ListaClientes;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsListItem;

public class fragment_busqclientes extends DialogFragment implements AdapterItemClickListener{

    Button btCancelar, tbBuscar;
    MaterialButtonToggleGroup tgModoOrden;
    AutoCompleteTextView cbOrden;

    Integer  itemOrden;
    Integer  itemModoOrden;
    ProgressBar pb ;
    TextView txtBusq;

    adapter_BusqClientes adapter;
    RecyclerView rvListaClientes;

    @Override
    public void onClick(View view, clsCliente cliente) {
        IBusqClientes listener = (IBusqClientes) getTargetFragment();
        listener.onFinishBusqClientesDialog("OK", cliente);
        dismiss();
    }

    public interface IBusqClientes {
        void onFinishBusqClientesDialog(String accion, clsCliente cliente);
    }

    public static fragment_busqclientes newInstance()
    {
        fragment_busqclientes f = new fragment_busqclientes();
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

    public fragment_busqclientes() {    }

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
        View v=inflater.inflate(R.layout.fragment_busqclientes,null);

        tbBuscar = (Button) v.findViewById(R.id.btBusqCBuscar);
        btCancelar = (Button) v.findViewById(R.id.btCancelarBusqClientes);
        txtBusq = (TextView) v.findViewById(R.id.txtBusqCIdCliente);

        cbOrden = (AutoCompleteTextView) v.findViewById(R.id.actvCampoOrden);
        tgModoOrden = (MaterialButtonToggleGroup) v.findViewById(R.id.tgModoOrden);

        rvListaClientes = v.findViewById(R.id.rvListaClientes);
        rvListaClientes.setHasFixedSize(true);
        rvListaClientes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListaClientes.setItemAnimator(new DefaultItemAnimator());

        rvListaClientes.addItemDecoration(new DividerItemDecoration(rvListaClientes.getContext(), LinearLayout.VERTICAL));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(),
                R.anim.layout_animation_down_to_up);
        rvListaClientes.setLayoutAnimation(animation);


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<clsListItem> listCampoOrden = new ArrayList<clsListItem>();
        listCampoOrden.add(new clsListItem(1,"Fecha Creación",R.drawable.ic_baseline_calendar_month_24));
        listCampoOrden.add(new clsListItem(2,"Tipo Cliente", R.drawable.ic_baseline_person_24));
        listCampoOrden.add(new clsListItem(3,"Top",R.drawable.ic_baseline_attach_money_24));
        listCampoOrden.add(new clsListItem(4,"Género", R.drawable.ic_baseline_person_24));
        listCampoOrden.add(new clsListItem(5,"Tipo Identificacion", R.drawable.ic_baseline_trending_up_24));
        listCampoOrden.add(new clsListItem(6,"Nombre",R.drawable.ic_baseline_person_24));
        UIHelper.fillCombo(cbOrden, listCampoOrden, 6);
        tgModoOrden.check(R.id.btUpOrderListaClientes);

        return inflater.inflate(R.layout.fragment_filtrosclientes, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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


                /*IfiltrosClientes listener = (IfiltrosClientes) getTargetFragment();
                listener.onFinishFiltrosClientesDialog("Filtrar", itemFuente, itemTipoCliente,itemTipoID,
                                                    itemGenero,itemEstado, itemOrden,itemModoOrden);
                dismiss();*/

        tbBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListaClientes ();
            }
        });
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IBusqClientes listener = (IBusqClientes) getTargetFragment();
                listener.onFinishBusqClientesDialog("Cancelar", null);
                dismiss();
            }
        });
    }

    private void loadListOnRV (ArrayList<clsCliente> lstClientes){
        adapter = new adapter_BusqClientes(getContext(), lstClientes);
        rvListaClientes.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    private void getListaClientes (){
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
        rvListaClientes.setAdapter(null);

        Map<String, String> params = new HashMap();
        params.put("busq", Busq.trim());
        if(itemOrden>0)  params.put("orden", itemOrden.toString());
        if(itemModoOrden>0)  params.put("modoorden", itemModoOrden.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_BusqClientes, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<clsCliente> lstClientes = new ArrayList<clsCliente> ();
                        try{
                            JSONArray JSONlistaUsuarios=  response.getJSONArray("clientes");
                            lstClientes = clsCliente.JsonObjectsBuild(JSONlistaUsuarios);

                            loadListOnRV (lstClientes);

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










}
package uteq.solutions.navdrawer.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.adapter.adaptador_ListItem;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsListItem;

public class fragment_filtrosclientes extends DialogFragment {

    Button btCancelar;
    Button btOK;
    MaterialButtonToggleGroup tgModoOrden, tgFuente;
    AutoCompleteTextView cbOrden;
    AutoCompleteTextView cbTipoCliente;
    AutoCompleteTextView cbTipoID;
    AutoCompleteTextView cbGenero;
    AutoCompleteTextView cbEstado;


    Integer  itemTipoCliente;
    Integer  itemTipoID;
    Integer  itemGenero;
    Integer  itemEstado;
    Integer  itemOrden;
    Integer  itemModoOrden;
    Integer itemFuente;

        public interface IfiltrosClientes {
        void onFinishFiltrosClientesDialog(String accion, Integer Fuente, Integer TipoCliente, Integer TipoID,
                                            Integer Genero, Integer Estado, Integer Orden,
                                            Integer ModoOrden);
    }

    public static fragment_filtrosclientes newInstance(Integer itemFuente, Integer itemTipoCliente,
                                                       Integer itemTipoID,
                                                       Integer itemGenero, Integer itemEstado,
                                                       Integer  itemOrden, Integer  itemModoOrden)
    {
        fragment_filtrosclientes f = new fragment_filtrosclientes();
        Bundle args = new Bundle();
        args.putInt("itemFuente",itemFuente);
        args.putInt("itemTipoCliente",itemTipoCliente);
        args.putInt("itemTipoID",itemTipoID);
        args.putInt("itemGenero",itemGenero);
        args.putInt("itemEstado",itemEstado);
        args.putInt("itemOrden",itemOrden);
        args.putInt("itemModoOrden",itemModoOrden);

        f.setArguments(args);
        return f;
    }

    public fragment_filtrosclientes() {    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args=getArguments();
        itemFuente=args.getInt("itemFuente");
        itemTipoCliente=args.getInt("itemTipoCliente");
        itemTipoID=args.getInt("itemTipoID");
        itemGenero=args.getInt("itemGenero");
        itemEstado=args.getInt("itemEstado");
        itemOrden=args.getInt("itemOrden");
        itemModoOrden=args.getInt("itemModoOrden");

        return crearDialogoFiltrosClientes();
    }


    private Dialog crearDialogoFiltrosClientes(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_filtrosclientes,null);

        btCancelar = (Button) v.findViewById(R.id.btCancelarfiltroClientes);
        btOK = (Button) v.findViewById(R.id.btOkFiltroClientes);
        cbOrden = (AutoCompleteTextView) v.findViewById(R.id.actvCampoOrden);
        tgModoOrden = (MaterialButtonToggleGroup) v.findViewById(R.id.tgModoOrden);
        tgFuente = (MaterialButtonToggleGroup) v.findViewById(R.id.tgFuente);

        cbTipoCliente = (AutoCompleteTextView) v.findViewById(R.id.actvFiltroTipoCliente);
        cbTipoID = (AutoCompleteTextView) v.findViewById(R.id.actvFiltroTipoID);
        cbGenero = (AutoCompleteTextView) v.findViewById(R.id.actvFiltroGenero);
        cbEstado = (AutoCompleteTextView) v.findViewById(R.id.actvFiltroEstado);

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

        tgFuente.check(itemFuente==1?R.id.btFuenteTop:R.id.btFuenteTodos);

        ArrayList<clsListItem> listCampoOrden = new ArrayList<clsListItem>();
        listCampoOrden.add(new clsListItem(1,"Fecha Creación",R.drawable.ic_baseline_calendar_month_24));
        listCampoOrden.add(new clsListItem(2,"Tipo Cliente", R.drawable.ic_baseline_person_24));
        listCampoOrden.add(new clsListItem(3,"Top",R.drawable.ic_baseline_attach_money_24));
        listCampoOrden.add(new clsListItem(4,"Género", R.drawable.ic_baseline_person_24));
        listCampoOrden.add(new clsListItem(5,"Tipo Identificacion", R.drawable.ic_baseline_trending_up_24));
        listCampoOrden.add(new clsListItem(6,"Nombre",R.drawable.ic_baseline_person_24));
        UIHelper.fillCombo(cbOrden, listCampoOrden, itemOrden);


        UIHelper.fillCombo(cbGenero, "gen", -1, itemGenero,1);
        UIHelper.fillCombo(cbTipoCliente, "tipocliente", -1, itemTipoCliente,1);
        UIHelper.fillCombo(cbTipoID, "tipoid",-1 , itemTipoID,1);
        UIHelper.fillCombo(cbEstado, "estadocliente", -1, itemEstado,1);

        tgModoOrden.check(itemModoOrden==1?R.id.btUpOrderListaClientes:R.id.btDownOrderListaClientes);

        return inflater.inflate(R.layout.fragment_filtrosclientes, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tgFuente.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId==R.id.btFuenteTop)     itemFuente = isChecked?1:2;
            }
        });

        tgModoOrden.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId==R.id.btUpOrderListaClientes)     itemModoOrden = isChecked?1:2;
            }
        });

        cbTipoCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clsListItem item = (clsListItem) adapterView.getItemAtPosition(i);
                itemTipoCliente = item.ID;
            }
        });

        cbTipoID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemTipoID = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });


        cbEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemEstado = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbGenero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemGenero = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbOrden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemOrden = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IfiltrosClientes listener = (IfiltrosClientes) getTargetFragment();
                listener.onFinishFiltrosClientesDialog("Filtrar", itemFuente, itemTipoCliente,itemTipoID,
                                                    itemGenero,itemEstado, itemOrden,itemModoOrden);
                dismiss();
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IfiltrosClientes listener = (IfiltrosClientes) getTargetFragment();
                listener.onFinishFiltrosClientesDialog("Cancelar", null,null,null,
                        null,null,null,null);
                dismiss();
            }
        });
    }










}
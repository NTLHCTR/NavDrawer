package uteq.solutions.navdrawer.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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

public class fragment_filtrosproductos extends DialogFragment {

    Button btCancelar;
    Button btOK;
    MaterialButtonToggleGroup tgModoOrden, tgFuente;
    AutoCompleteTextView cbOrden;
    AutoCompleteTextView cbCat;
    AutoCompleteTextView cbSubCat;
    AutoCompleteTextView cbBodega;
    AutoCompleteTextView cbEstado;


    Integer  itemCat;
    Integer  itemSubCat;
    Integer  itemBodega;
    Integer  itemEstado;
    Integer  itemOrden;
    Integer  itemModoOrden;
    Integer itemFuente;


    public interface IfiltrosProductos {
        void onFinishFiltrosProductosDialog(String accion, Integer Fuente, Integer Cat, Integer SubCat,
                                            Integer Bodega, Integer Estado, Integer Orden,
                                            Integer ModoOrden);
    }

    public static fragment_filtrosproductos newInstance(Integer itemFuente, Integer itemCat,Integer itemSubCat,
                                                        Integer itemBodega,Integer itemEstado,
                                                        Integer  itemOrden, Integer  itemModoOrden)
    {
        fragment_filtrosproductos f = new fragment_filtrosproductos();
        Bundle args = new Bundle();
        args.putInt("itemFuente",itemFuente);
        args.putInt("itemCat",itemCat);
        args.putInt("itemSubCat",itemSubCat);
        args.putInt("itemBodega",itemBodega);
        args.putInt("itemEstado",itemEstado);
        args.putInt("itemOrden",itemOrden);
        args.putInt("itemModoOrden",itemModoOrden);

        f.setArguments(args);
        return f;
    }

    public fragment_filtrosproductos() {    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args=getArguments();
        itemFuente=args.getInt("itemFuente");
        itemCat=args.getInt("itemCat");
        itemSubCat=args.getInt("itemSubCat");
        itemBodega=args.getInt("itemBodega");
        itemEstado=args.getInt("itemEstado");
        itemOrden=args.getInt("itemOrden");
        itemModoOrden=args.getInt("itemModoOrden");

        return crearDialogoFiltrosProductos();
    }


    private Dialog crearDialogoFiltrosProductos(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_filtrosproductos,null);

        btCancelar = (Button) v.findViewById(R.id.btCancelarfiltroProductos);
        btOK = (Button) v.findViewById(R.id.btOkFiltroProductos);
        cbOrden = (AutoCompleteTextView) v.findViewById(R.id.actvCampoOrden);
        tgModoOrden = (MaterialButtonToggleGroup) v.findViewById(R.id.tgModoOrden);
        tgFuente = (MaterialButtonToggleGroup) v.findViewById(R.id.tgFuente);

        cbCat = (AutoCompleteTextView) v.findViewById(R.id.actvFiltroCat);
        cbSubCat = (AutoCompleteTextView) v.findViewById(R.id.actvFiltroSubCat);
        cbBodega = (AutoCompleteTextView) v.findViewById(R.id.actvFiltroBodega);
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
        listCampoOrden.add(new clsListItem(2,"Categorías", R.drawable.ic_baseline_fastfood_24));
        listCampoOrden.add(new clsListItem(3,"Sub Categorías",R.drawable.ic_baseline_coffee_24));
        listCampoOrden.add(new clsListItem(4,"Bodega", R.drawable.ic_baseline_apartment_24));
        listCampoOrden.add(new clsListItem(5,"Precio Unidad", R.drawable.ic_baseline_attach_money_24));
        UIHelper.fillCombo(cbOrden, listCampoOrden, itemOrden);

        UIHelper.fillCombo(cbCat, "cat", -1, itemCat,1);
        if(itemCat>0)
            UIHelper.fillCombo(cbSubCat, "subcat", itemCat, itemSubCat,1);
        UIHelper.fillCombo(cbBodega, "bod", -1, itemBodega,1);
        UIHelper.fillCombo(cbEstado, "estadoprod", -1, itemEstado,1);

        tgModoOrden.check(itemModoOrden==1?R.id.btUpOrderListaProductos:R.id.btDownOrderListaProductos);

        return inflater.inflate(R.layout.fragment_filtrosproductos, container, false);
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
                if(checkedId==R.id.btUpOrderListaProductos)     itemModoOrden = isChecked?1:2;
            }
        });

        cbCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clsListItem item = (clsListItem)adapterView.getItemAtPosition(i);
                itemCat=item.ID;
                cbSubCat.setText("");
                if(item.ID>0)
                     UIHelper.fillCombo(cbSubCat, "subcat", item.ID, -1,1);
            }
        });

        cbSubCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSubCat = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemEstado = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemEstado = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbBodega.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemBodega = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
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

                IfiltrosProductos listener = (IfiltrosProductos) getTargetFragment();
                listener.onFinishFiltrosProductosDialog("Filtrar", itemFuente, itemCat,itemSubCat,
                                                    itemBodega,itemEstado, itemOrden,itemModoOrden);
                dismiss();
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IfiltrosProductos listener = (IfiltrosProductos) getTargetFragment();
                listener.onFinishFiltrosProductosDialog("Cancelar", null,null,null,
                        null,null,null,null);
                dismiss();
            }
        });
    }



}
package uteq.solutions.navdrawer.ui.productos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.helper.DecimalDigitsInputFilter;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsListItem;


public class fragment_addproductopart2 extends Fragment {

    AutoCompleteTextView cbBodega, cbUnidad, cbEnvase, cbEstado, cbICE;
    TextInputLayout  cbEnvasely, CbUnidadLy, cbBodegaly, cbEstadoly, cbICELy;
    TextInputLayout  txtstockminly, txtstockmaxly, txtdescuentoly;
    TextInputEditText  txtstockmin, txtstockmax, txtdescuento;
    Switch swICE;

    private String Modo; //Add o Upd

    Integer  itemBodega=-1, itemUnidad=-1, itemEnvase=-1, itemEstado=-1, itemICE=-1;

    public fragment_addproductopart2() {
        // Required empty public constructor
    }

    public static fragment_addproductopart2 newInstance(String _Modo) {
        fragment_addproductopart2 fragment = new fragment_addproductopart2();
        Bundle args = new Bundle();;
        args.putString("Modo", _Modo);
        fragment.setArguments(args);
        return fragment;
    }

    public static fragment_addproductopart2 newInstance(String _Modo, String StockMin, String StockMax,
                                                        int Envase, int Unidad, int Ubicacion, int Estado, String Descuento,
                                                        int ICE) {
        fragment_addproductopart2 fragment = new fragment_addproductopart2();
        Bundle args = new Bundle();;
        args.putString("Modo", _Modo);
        if(StockMin!=null)    args.putDouble("StockMin",Double.parseDouble(StockMin));
        if(StockMax!=null)    args.putDouble("StockMax",Double.parseDouble(StockMax));
        if(Descuento!=null)   args.putDouble("Descuento",Double.parseDouble(Descuento));
        args.putInt("ICE",    ICE);
        args.putInt("Envase", Envase);
        args.putInt("Unidad", Unidad);
        args.putInt("Ubicacion", Ubicacion);
        args.putInt("Estado", Estado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Modo = getArguments().getString("Modo");
            if(Modo.equals("Upd")){
                itemICE=     getArguments().getInt("ICE");
                itemEnvase=  getArguments().getInt("Envase");
                itemUnidad=  getArguments().getInt("Unidad");
                itemBodega=  getArguments().getInt("Ubicacion");
                itemEstado=  getArguments().getInt("Estado");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_addproductopart2, container, false);

        cbEnvase = (AutoCompleteTextView) root.findViewById(R.id.actvAddEnvase);
        cbUnidad = (AutoCompleteTextView) root.findViewById(R.id.actvAddUnidad);
        cbBodega = (AutoCompleteTextView) root.findViewById(R.id.actvAddBodega);
        cbEstado = (AutoCompleteTextView) root.findViewById(R.id.actvEditEstado);
        cbICE =    (AutoCompleteTextView) root.findViewById(R.id.actvAddICE);
        swICE = (Switch) root.findViewById(R.id.swicthICE);

        txtstockmin = (TextInputEditText) root.findViewById(R.id.txtAddPStockMin);
        txtstockmax = (TextInputEditText) root.findViewById(R.id.txtAddPStockMax);
        txtdescuento = (TextInputEditText) root.findViewById(R.id.txtAddPDesc);

        txtstockminly = (TextInputLayout) root.findViewById(R.id.lblStockMin);
        txtstockmaxly = (TextInputLayout) root.findViewById(R.id.lblStockMax);
        txtdescuentoly = (TextInputLayout) root.findViewById(R.id.lblAddPDescLy);


        cbEstadoly  = (TextInputLayout) root.findViewById(R.id.cbEstado);
        if(Modo.equals("Add"))
            cbEstadoly.setVisibility(View.GONE);

        cbEnvasely  = (TextInputLayout) root.findViewById(R.id.cbAddEnvase);
        CbUnidadLy  = (TextInputLayout) root.findViewById(R.id.cbUnidad);
        cbBodegaly = (TextInputLayout) root.findViewById(R.id.cbAddBodega);
        cbICELy =      (TextInputLayout) root.findViewById(R.id.cbICELy);

        if(Modo.equals("Add")) {
            UIHelper.fillCombo(cbBodega, "bod", -1, -1, -1);
            UIHelper.fillCombo(cbEnvase, "env", -1, -1, -1);
            UIHelper.fillCombo(cbUnidad, "uni", -1, -1, -1);
            UIHelper.fillCombo(cbICE, "ice", -1, -1, -1);
        }else{

            if(getArguments().containsKey("Descuento"))
                txtdescuento.setText(String.format("%,.2f",getArguments().getDouble("Descuento")));
            if(getArguments().containsKey("StockMin"))
                 txtstockmin.setText(String.format("%,.2f",getArguments().getDouble("StockMin")));
            if(getArguments().containsKey("StockMax"))
                txtstockmax.setText(String.format("%,.2f",getArguments().getDouble("StockMax")));
            UIHelper.fillCombo(cbBodega, "bod", -1, itemBodega,-1);
            UIHelper.fillCombo(cbEnvase, "env", -1, itemEnvase,-1);
            UIHelper.fillCombo(cbUnidad, "uni", -1, itemUnidad,-1);
            UIHelper.fillCombo(cbICE, "ice", -1, itemICE,-1);
            swICE.setChecked(itemICE>0); cbICE.setEnabled(itemICE>0);
            UIHelper.fillCombo(cbEstado, "estadoprod", -1, itemEstado,-1);

        }


        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtstockmin.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtstockmax.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtdescuento.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});


        swICE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cbICE.setEnabled(b);
                cbICE.setText("",false);
                itemICE=-1;
            }
        } );


        cbICE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemICE = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemEstado = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbUnidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemUnidad = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbEnvase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemEnvase = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });


        cbBodega.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemBodega = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

    }

}
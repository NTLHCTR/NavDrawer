package uteq.solutions.navdrawer.ui.facturacion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.dialogs.fragment_busqclientes;
import uteq.solutions.navdrawer.dialogs.fragment_filtrosclientes;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsListItem;
import uteq.solutions.navdrawer.model.clsProducto;


public class fragment_encabezadoFactura extends Fragment
        implements fragment_busqclientes.IBusqClientes, View.OnClickListener {

    private viewModelFacturacion viewModel;
    private Button btSelectDate, btSearchCliente;
    private TextView lblFecha;

    AutoCompleteTextView cbTipoID;
    TextInputLayout cbTipoIDLy;
    TextInputLayout txtidly, txtnombrely, txtcorreoly;
    TextInputEditText txtid, txtnombre, txtcorreo;
    ProgressBar pb ;

    Integer  itemTipoID=-1;

    public fragment_encabezadoFactura() {
        // Required empty public constructor
    }


    public static fragment_encabezadoFactura newInstance(String param1, String param2) {
        fragment_encabezadoFactura fragment = new fragment_encabezadoFactura();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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

        View v = inflater.inflate(R.layout.fragment_encabezadofactura, container, false);

        btSelectDate = (Button) v.findViewById(R.id.btEncFacSelectDate);
        btSearchCliente = (Button) v.findViewById(R.id.btEncFacBuscarC);
        lblFecha  = (TextView) v.findViewById(R.id.lblEncFacFecha);

        cbTipoID = (AutoCompleteTextView) v.findViewById(R.id.actvEncFactTipoIDCliente);
        txtid = (TextInputEditText) v.findViewById(R.id.txtEncFactIdCliente);
        txtnombre = (TextInputEditText) v.findViewById(R.id.txtEncFacNombreC);
        txtcorreo = (TextInputEditText) v.findViewById(R.id.txtEncFacCorreoC);


        txtidly = (TextInputLayout) v.findViewById(R.id.txtEncFacClienteIDLy);
        txtnombrely = (TextInputLayout) v.findViewById(R.id.txtEncFacNombreCLy);
        txtcorreoly = (TextInputLayout) v.findViewById(R.id.txtFactEnCorreoLy);
        cbTipoIDLy = (TextInputLayout) v.findViewById(R.id.cbEncFacTipoIDClienteLy);


        pb = (ProgressBar) v.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);

        UIHelper.fillCombo(cbTipoID, "tipoid",-1 , -1,-1);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cbTipoID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clsListItem item = (clsListItem)adapterView.getItemAtPosition(i);
                itemTipoID=item.ID;
            }
        });

        btSearchCliente.setOnClickListener(this);


        txtid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Texto: " + txtid.getText(),Toast.LENGTH_LONG).show();
            }
        });

        btSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker datePicker =
                        MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select date")
                                .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        lblFecha.setText(DateFormat.format("dd/MM/yyyy", new Date(selection)).toString());
                    }
                });
                datePicker.show(getParentFragmentManager(), "tag");
            }
        });

        viewModel = new ViewModelProvider(requireActivity()).get(viewModelFacturacion.class);
        //viewModel.setProductoinList(new clsProducto("Producto ","Bebidas",5000.0,10));

    }

    @Override
    public void onFinishBusqClientesDialog(String accion) {
        if(accion.equals("OK")){

        }
    }

    @Override
    public void onClick(View view) {
        fragment_busqclientes dialogo = new  fragment_busqclientes();
        dialogo.setTargetFragment(this, 300);
        dialogo.show(getParentFragmentManager(), "DialogoFiltros");
    }
}
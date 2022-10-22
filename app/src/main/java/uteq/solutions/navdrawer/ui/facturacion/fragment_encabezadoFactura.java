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
import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsListItem;
import uteq.solutions.navdrawer.model.clsProducto;


public class fragment_encabezadoFactura extends Fragment
        implements fragment_busqclientes.IBusqClientes, View.OnClickListener {

    private viewModelFacturacion viewModel;
    private Button btSelectDate, btSearchCliente;
    private TextView lblFecha;

    TextInputLayout txtidly, txtnombrely, txtcorreoly;
    TextInputEditText txtid, txtnombre, txtcorreo;
    ProgressBar pb ;


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


        txtid = (TextInputEditText) v.findViewById(R.id.txtEncFactIdCliente);
        txtnombre = (TextInputEditText) v.findViewById(R.id.txtEncFacNombreC);
        txtcorreo = (TextInputEditText) v.findViewById(R.id.txtEncFacCorreoC);

        txtid.setKeyListener(null); txtnombre.setKeyListener(null);
        txtcorreo.setKeyListener(null);

        txtidly = (TextInputLayout) v.findViewById(R.id.txtEncFacClienteIDLy);
        txtnombrely = (TextInputLayout) v.findViewById(R.id.txtEncFacNombreCLy);
        txtcorreoly = (TextInputLayout) v.findViewById(R.id.txtFactEnCorreoLy);

        pb = (ProgressBar) v.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btSearchCliente.setOnClickListener(this);


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

    }

    @Override
    public void onFinishBusqClientesDialog(String accion, clsCliente cliente ) {
        if(accion.equals("OK")){
            txtcorreo.setText(cliente.correo);
            txtid.setText(cliente.tipoidentificacion + ": " + cliente.identificacion);
            txtnombre.setText(cliente.nombre);
        }
    }

    @Override
    public void onClick(View view) {
        fragment_busqclientes dialogo = new  fragment_busqclientes();
        dialogo.setTargetFragment(this, 300);
        dialogo.show(getParentFragmentManager(), "DialogoFiltros");
    }
}
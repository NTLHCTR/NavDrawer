package uteq.solutions.navdrawer.ui.facturacion;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.dialogs.fragment_busqclientes;
import uteq.solutions.navdrawer.dialogs.fragment_busqproducts;
import uteq.solutions.navdrawer.helper.DecimalDigitsInputFilter;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsItemFactura;
import uteq.solutions.navdrawer.model.clsListItem;
import uteq.solutions.navdrawer.model.clsProducto;
import uteq.solutions.navdrawer.ui.productos.fragment_addproductopart1;
import uteq.solutions.navdrawer.ui.productos.fragment_addproductopart2;


public class fragment_detallefactura extends Fragment
        implements fragment_busqproducts.IBusqProductos, View.OnClickListener {

    private viewModelFacturacion viewModel;

    Button btBuscar, btAgregar, btCancelar, btPMin, btPMax;
    TextInputLayout  txtcantly, txtivaly, txticely, txtpvply, txtdescuentoly, txtdescpercly, txtcodly, txtpfinally;
    TextInputEditText txtcod, txtdescuento, txtdescuentoporc, txtpvp, txtcant, txtiva, txtice, txtivaporc,
            txtdescripcion, txtpfinal;
    TextView lblSubTotal;
    MaterialButtonToggleGroup tgTipoPrecio;
    RelativeLayout rlyICE;

    ProgressBar pb ;
    clsItemFactura productoSeleccionado;



    public fragment_detallefactura() {
        // Required empty public constructor
    }


    public static fragment_detallefactura newInstance() {
        fragment_detallefactura fragment = new fragment_detallefactura();
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
        View root= inflater.inflate(R.layout.fragment_detallefactura, container, false);

        pb = (ProgressBar) root.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);

        rlyICE = (RelativeLayout) root.findViewById(R.id.rlyICE);
        txtcod = (TextInputEditText) root.findViewById(R.id.txtDetFactCodProd);
        txtdescripcion = (TextInputEditText) root.findViewById(R.id.txtDetFacdesccripcion);
        txtcant = (TextInputEditText) root.findViewById(R.id.txtDetFacCant);
        txtpvp = (TextInputEditText) root.findViewById(R.id.txtDetFacPrecio);
        txtiva = (TextInputEditText) root.findViewById(R.id.txtDetFacIva);
        txtice = (TextInputEditText) root.findViewById(R.id.txtDetFacICE);
        txtivaporc = (TextInputEditText) root.findViewById(R.id.txtDetFacIVAPerc);
        txtdescuento = (TextInputEditText) root.findViewById(R.id.txtDetFacDesc);
        txtdescuentoporc = (TextInputEditText) root.findViewById(R.id.txtDetFacDescPerc);
        txtpfinal = (TextInputEditText) root.findViewById(R.id.txtDetFacPFinal);
        lblSubTotal = (TextView) root.findViewById(R.id.lblDetFacSubTotal);

        txtcodly = (TextInputLayout) root.findViewById(R.id.txtDetFacCodProdLy);
        txtcantly = (TextInputLayout) root.findViewById(R.id.lblDetFacCantly);
        txtpvply = (TextInputLayout) root.findViewById(R.id.lblDetFacPVP);
        txtdescuentoly = (TextInputLayout) root.findViewById(R.id.lblDetFacDescLy);
        txtivaly = (TextInputLayout) root.findViewById(R.id.lblDetFacIvaLy);
        txticely = (TextInputLayout) root.findViewById(R.id.lblDetFacICELy);
        txtdescpercly = (TextInputLayout) root.findViewById(R.id.lblDetFacIvaLy);
        txtpfinally = (TextInputLayout) root.findViewById(R.id.lblDetFacPFinalLy);
        tgTipoPrecio = (MaterialButtonToggleGroup) root.findViewById(R.id.tgDetFacTipoPrecio);

        btBuscar = (Button) root.findViewById(R.id.btDetFacBuscarP);
        btAgregar = (Button) root.findViewById(R.id.btDetFacAddP);
        btCancelar = (Button) root.findViewById(R.id.btDetFacCancel);
        btPMin = (Button) root.findViewById(R.id.btPrecioMin);
        btPMax = (Button) root.findViewById(R.id.btPrecioMay);

        tgTipoPrecio.check(R.id.btPrecioMin);

        productoSeleccionado=null;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtcant.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtpvp.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtiva.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtdescuento.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtdescuentoporc.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

        txtdescripcion.setKeyListener(null);
        txtiva.setKeyListener(null);
        txtivaporc.setKeyListener(null);
        txtpfinal.setKeyListener(null);

        btBuscar.setOnClickListener(this);

        updateUIOnNew();

        btAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productoSeleccionado!=null) {

                    String resp=validaParametros();
                    if(!resp.equals("")) {
                        new MaterialAlertDialogBuilder(getContext())
                                .setTitle("Error en los Parámetros")
                                .setIcon(R.drawable.ic_baseline_error_24)
                                .setMessage(resp)
                                .setPositiveButton("Ok", null)
                                .show();
                    }else {

                        productoSeleccionado.cantidad = Double.parseDouble(txtcant.getText().toString());
                        productoSeleccionado.pvp = Double.parseDouble(txtpvp.getText().toString());
                        productoSeleccionado.descuento = Double.parseDouble(txtdescuento.getText().toString());

                        //Los vcódigos de IVA e ICE ya vienen asignados desde que se Buscó y Mostró el productos
                        productoSeleccionado.iva_baseimponible = productoSeleccionado.getBaseImponibleItem();

                        productoSeleccionado.iva_valor = Double.parseDouble(txtiva.getText().toString());
                        if (productoSeleccionado.ice_codigo > 0) {
                            productoSeleccionado.ice_baseimponible = productoSeleccionado.getBaseImponibleItem();
                            productoSeleccionado.ice_valor = Double.parseDouble(txtice.getText().toString());
                        }

                        viewModel = new ViewModelProvider(requireActivity()).get(viewModelFacturacion.class);
                        viewModel.setProductoinList(productoSeleccionado);



                        Snackbar snackbar = Snackbar.make(view, "Producto agregado..", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Deshacer", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(),"Elima",Toast.LENGTH_SHORT).show();
                            }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();

                    }
                }

            }
        });

        txtcod.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_DONE ||
                        (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&    keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    if(txtcod.getText().toString().length()>4)
                        getProductoByCode(txtcod.getText().toString());
                }
                return false;
            }
        });

        txtice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(txtice.getText().toString().equals("")){
                    txticely.setError("Error");
                }else {
                    try {
                        Double ice = Double.parseDouble(txtice.getText().toString());
                        if (ice < 0) {
                            txticely.setError("Error");
                        } else {
                            txticely.setError(null);
                        }
                    }  catch (Exception e){
                        txticely.setError("Error");
                    }
                }
                validaCalculaPFinal();
                reCalcularTotales();
                return false;
            }
        });


        txtcant.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(txtcant.getText().toString().equals("")){
                    txtcantly.setError("Error");
                }else {

                    try {
                        Double Cant = Double.parseDouble(txtcant.getText().toString());
                        if (Cant <= 0) {
                            txtcantly.setError("Error");
                        } else {
                            txtcantly.setError(null);
                        }
                    }catch (Exception e){
                        txtcantly.setError("Error");
                    }
                }
                validaCalculaIVA();
                validaCalculaPFinal();
                reCalcularTotales();
                return false;
            }
        });

        txtpvp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(txtpvp.getText().toString().equals("")) {
                    txtpvply.setError("Error");
                }else {
                    try {
                        Double PVP = Double.parseDouble(txtpvp.getText().toString());
                        if (PVP <= 0) {
                            txtpvply.setError("Error");
                        } else {
                            txtpvply.setError(null);
                        }
                    }catch (Exception e){
                        txtpvply.setError("Error");
                    }
                }
                calculaDescFromPVP();
                validaCalculaIVA();
                validaCalculaPFinal();
                reCalcularTotales();
                return false;
            }
        });

        txtdescuentoporc.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                txtdescuento.setText("");

                if(txtpvply.getError()==null){
                        Double PVP= Double.parseDouble(txtpvp.getText().toString());
                        Double desc, descd;
                        if(txtdescuentoporc.getText().toString().equals("")) {
                            txtdescpercly.setError("Error"); txtdescuentoly.setError("Error");
                         }else {
                            try{
                                desc = Double.parseDouble(txtdescuentoporc.getText().toString());
                                if (desc < 0 || desc >= 100) {
                                    txtdescpercly.setError("Error");
                                    txtdescuentoly.setError("Error");
                                } else {
                                    txtdescpercly.setError(null);
                                    descd = PVP * desc / 100;
                                    if (descd < 0 || descd >= PVP) {
                                        txtdescuentoly.setError("Error");
                                    } else {
                                        txtdescuento.setText(String.format("%.2f", descd));
                                        txtdescuentoly.setError(null);
                                    }
                                }
                            }catch (Exception e){
                                txtdescpercly.setError("Error"); txtdescuentoly.setError("Error");
                            }
                        }
                }else{
                    txtdescuentoly.setError("Error");  txtdescpercly.setError("Error");
                }

                validaCalculaIVA();
                validaCalculaPFinal();
                reCalcularTotales();
                return false;
            }
        });

        txtdescuento.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                txtdescuentoporc.setText("");

                if(txtpvply.getError()==null){
                    try {
                        Double PVP = Double.parseDouble(txtpvp.getText().toString());
                        Double desc, descd;
                        if (txtdescuento.getText().toString().equals("")) {
                            txtdescpercly.setError("Error");
                            txtdescuentoly.setError("Error");
                        } else {
                            descd = Double.parseDouble(txtdescuento.getText().toString());
                            if (descd < 0 || descd >= PVP) {
                                txtdescpercly.setError("Error");
                                txtdescuentoly.setError("Error");
                            } else {
                                txtdescuentoly.setError(null);
                                desc = descd / PVP * 100;
                                if (desc < 0 || desc >= 100) {
                                    txtdescpercly.setError("Error");
                                } else {
                                    txtdescuentoporc.setText(String.format("%.2f", desc));
                                    txtdescpercly.setError(null);
                                }
                            }
                        }

                    }catch (Exception e){
                        txtdescuentoly.setError("Error");  txtdescpercly.setError("Error");
                    }
                }else{
                    txtdescuentoly.setError("Error");  txtdescpercly.setError("Error");
                }
                validaCalculaIVA();
                validaCalculaPFinal();
                reCalcularTotales();
                return false;
            }
        });

        tgTipoPrecio.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked){
                    if(checkedId == R.id.btPrecioMin){
                        if(!btPMin.getTag().toString().equals(""))
                            txtpvp.setText(String.format("%.2f",Double.parseDouble(btPMin.getTag().toString())));
                        else
                            txtpvp.setText("0.00");
                    }else{
                        if(!btPMax.getTag().toString().equals(""))
                            txtpvp.setText(String.format("%.2f",Double.parseDouble(btPMax.getTag().toString())));
                        else
                            txtpvp.setText("0.00");
                    }
                    txtpvply.setError(null);
                    calculaDescFromPVP();
                    validaCalculaIVA();
                    validaCalculaPFinal();
                    reCalcularTotales();
                }
            }
        });


        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIOnNew();
            }
        });

    }

    private void updateUIOnNew(){
        txtdescripcion.setText("");
        txtiva.setText(""); txtivaporc.setText("");
        txtpfinal.setText("");
        lblSubTotal.setText("");
        txtice.setText("");   txtice.setEnabled(false); rlyICE.setVisibility(View.GONE);

        txtcod.setText(""); txtcod.setFocusable(true); txtcod.requestFocus();

        txtcant.setText(""); txtcant.setEnabled(false);
        txtpvp.setText(""); txtpvp.setEnabled(false);
        txtdescuento.setText(""); txtdescuento.setEnabled(false);
        txtdescuentoporc.setText(""); txtdescuentoporc.setEnabled(false);
        btPMin.setTag(""); btPMax.setTag("");
        tgTipoPrecio.setEnabled(false);
        btAgregar.setEnabled(false);

        productoSeleccionado=null;

    }

    private void showProduct(clsProducto jsonProducto){
        try {

            productoSeleccionado = new clsItemFactura(jsonProducto.ID, jsonProducto.codigo,jsonProducto.descripcion,
                    1,jsonProducto.preciocompra,jsonProducto.PVP, jsonProducto.DESC,
                    jsonProducto.codigoImpuestoIVA,0,0,
                    jsonProducto.codigoImpuestoICE,0,0,
                    jsonProducto.ImpuestoDescr);

            txtcant.setText("1");
            txtdescripcion.setText(jsonProducto.descripcion);
            Double pvp= jsonProducto.PVP;
            txtpvp.setText(String.format("%.2f",pvp>0?pvp:0));

            btPMin.setTag(pvp);
            btPMax.setTag(jsonProducto.PMayorista);

            Double descuento=jsonProducto.DESC;
            descuento= (descuento>=0 && descuento<100)?descuento:0.0;
            txtdescuentoporc.setText(String.format("%.2f",descuento));
            txtdescuento.setText(String.format("%.2f",pvp*descuento/100));
            Double SubTotal = pvp - (pvp*descuento/100);

            Double iva=jsonProducto.IVA;
            iva= iva>=0?iva:0.0;
            txtivaporc.setText(String.format("%.2f",iva));
            txtiva.setText(String.format("%.2f",SubTotal*iva/100));


            txtice.setText("0.00");
            if(jsonProducto.codigoImpuestoICE>0){
                rlyICE.setVisibility(View.VISIBLE);
                txticely.setHint(jsonProducto.ImpuestoICEDescr);
                txtice.setEnabled(true);
            }else{
                rlyICE.setVisibility(View.GONE);
                txtice.setEnabled(false);
            }

            txtpfinal.setText(String.format("%.2f",SubTotal+(SubTotal*iva/100)));
            lblSubTotal.setText("Subtotal: $" + String.format("%.2f",SubTotal+(SubTotal*iva/100)));

            txtcant.setEnabled(true); txtpvp.setEnabled(true);
            txtdescuento.setEnabled(true); txtdescuentoporc.setEnabled(true);
            tgTipoPrecio.setEnabled(true);


            txtcantly.setError(""); txtpvply.setError(""); txtdescuentoly.setError("");
            txtdescpercly.setError(""); txtivaly.setError(""); txtpfinally.setError("");
            txticely.setError("");

            btAgregar.setEnabled(true);

        } catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getProductoByCode (String Code){
        pb.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap();
        params.put("code", Code);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_GetProductoByCode, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pb.setVisibility(View.GONE);
                        try {
                            JSONObject jsonProducto=  response.getJSONArray("Producto").getJSONObject(0);
                            showProduct(new clsProducto(jsonProducto));

                        } catch (JSONException e) {
                            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }


                    }
                },new HTTPErrorResponseDialog(getContext(),pb)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UIHelper.getAuthHearders();
            }
        });

        pb.setVisibility(View.GONE);
        return true;
    }

    private String validaParametros(){
        if(txtcantly.getError()!=null || txtpvply.getError()!=null ||
                txtdescuentoly.getError()!=null  || txtdescpercly.getError()!=null
                || txtivaly.getError()!=null ) return "Error en Valores";

        Double Cant,PVP,IVA,Desc, ICE=0.0;
        if(rlyICE.getVisibility()==View.VISIBLE){
            if(txticely.getError()!=null)  return "Error en Valor ICE";
        }

        try {
            Cant = Double.parseDouble(txtcant.getText().toString());
            PVP = Double.parseDouble(txtpvp.getText().toString());
            Desc = Double.parseDouble(txtdescuento.getText().toString());
            IVA = Double.parseDouble(txtiva.getText().toString());
            if (PVP <= 0) return "PVP debe ser mayor a 0";
            if (Desc < 0) return "Descuento debe ser positivo";
            if (Desc >= PVP) return "Descuento debe ser menor que el PVP";
            if (Cant <= 0) return "Cantidad debe ser mayor a 0";
            if (IVA < 0) return "IVA debe ser positivo";
            return "";
        } catch (Exception e){
            return e.getMessage();
        }


    }
    private void reCalcularTotales(){

        Double Cant,PVP,IVA,Desc;
        lblSubTotal.setText("");

        if(txtcantly.getError()!=null || txtpvply.getError()!=null ||
                txtdescuentoly.getError()!=null  || txtdescpercly.getError()!=null
                || txtivaly.getError()!=null ) return;

        try{
            Cant = Double.parseDouble(txtcant.getText().toString());
            PVP = Double.parseDouble(txtpvp.getText().toString());
            Desc= Double.parseDouble(txtdescuento.getText().toString());
            IVA= Double.parseDouble(txtiva.getText().toString());

            Double ICE=0.0;
            if(rlyICE.getVisibility()==View.VISIBLE){
                if(txticely.getError()==null){
                    ICE = Double.parseDouble(txtice.getText().toString());
                    lblSubTotal.setText("SubTotal: $"  + String.format("%.2f",Cant * (PVP-Desc+IVA+ICE)));
                }
            }else{
                lblSubTotal.setText("SubTotal: $"  + String.format("%.2f",Cant * (PVP-Desc+IVA)));
            }
        } catch (Exception e){

        }
    }

    private void validaCalculaPFinal(){
        txtpfinal.setText("");

        if(txtpvply.getError()==null && txtdescuentoly.getError()==null && txtivaly.getError()==null ){
           try {
               Double PVP = Double.parseDouble(txtpvp.getText().toString());
               Double DESC = Double.parseDouble(txtdescuento.getText().toString());
               Double IVA = Double.parseDouble(txtiva.getText().toString());

               Double ICE = 0.0;
               if (rlyICE.getVisibility() == View.VISIBLE) {
                   if (txticely.getError() == null) {
                       ICE = Double.parseDouble(txtice.getText().toString());
                       txtpfinal.setText(String.format("%.2f", PVP - DESC + IVA + ICE));
                       txtpfinally.setError(null);
                   } else {
                       txtpfinally.setError("Error");
                   }
               } else {
                   txtpfinal.setText(String.format("%.2f", PVP - DESC + IVA));
                   txtpfinally.setError(null);
               }

           } catch (Exception e){
               txtpfinally.setError("Error");
           }
        }else{
            txtpfinally.setError("Error");
        }
    }

    private void calculaDescFromPVP(){
        txtdescuento.setText("");

        if(txtpvply.getError()==null && txtdescpercly.getError()==null ){
            try {
                Double PVP = Double.parseDouble(txtpvp.getText().toString());
                Double DESC = Double.parseDouble(txtdescuentoporc.getText().toString());

                txtdescuento.setText(String.format("%.2f", PVP * DESC / 100));
                txtdescuentoly.setError(null);

            }catch (Exception e){
                txtdescuentoly.setError("Error");
            }
        }else{
            txtdescuentoly.setError("Error");
        }
    }

    private void validaCalculaIVA(){
        txtiva.setText("");

        if(txtpvply.getError()==null && txtdescuentoly.getError()==null ){
            try {
                Double PVP = Double.parseDouble(txtpvp.getText().toString());
                Double DESC = Double.parseDouble(txtdescuento.getText().toString());
                Double iva, ivad;
                if (txtivaporc.getText().toString().equals("")) {
                    txtivaly.setError("Error");
                } else {
                    iva = Double.parseDouble(txtivaporc.getText().toString());
                    if (iva < 0 || iva >= 100) {
                        txtivaly.setError("Error");
                    } else {
                        ivad = (PVP - DESC) * iva / 100;
                        if (ivad < 0) {
                            txtivaly.setError("Error");
                        } else {
                            txtiva.setText(String.format("%.2f", ivad));
                            txtivaly.setError(null);
                        }
                    }
                }
            }catch (Exception e){
                txtivaly.setError("Error");
            }
        }else{
            txtivaly.setError("Error");
        }
    }

    @Override
    public void onClick(View view) {
        fragment_busqproducts dialogo = new  fragment_busqproducts();
        dialogo.setTargetFragment(this, 300);
        dialogo.show(getParentFragmentManager(), "DialogoFiltros");
    }

    @Override
    public void onFinishBusqProductsDialog(String accion, clsProducto producto) {
        if(accion.equals("OK")){
                showProduct(producto);
        }
    }
}
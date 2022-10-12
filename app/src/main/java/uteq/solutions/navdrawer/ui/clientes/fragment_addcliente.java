package uteq.solutions.navdrawer.ui.clientes;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.adapter.adaptador_ListItem;
import uteq.solutions.navdrawer.helper.DecimalDigitsInputFilter;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.helper.HTTPErrorResponseDialog;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsListItem;

public class fragment_addcliente extends Fragment {

    AutoCompleteTextView cbTipoID, cbOperadora, cbTipoCliente ;
    TextInputLayout cbTipoIDLy, cbOperadoraLy, cbTipoClienteLy;
    TextInputLayout txtidly, txtnombrely, txtcorreoly, txtnumeroly, txtdirly;
    TextInputEditText txtid, txtnombre, txtcorreo, txtnumero, txtdir;
    ProgressBar pb ;
    MaterialButtonToggleGroup tgGenero;


    Integer  itemTipoID=-1, itemOperadora=-1, itemGenero=-1, itemTipoCliente=-1;


    public static fragment_addcliente newInstance()
    {
        fragment_addcliente f = new fragment_addcliente();
        Bundle args = new Bundle();
        /*args.putInt("itemFuente",itemFuente);
        args.putInt("itemCat",itemCat);
        args.putInt("itemSubCat",itemSubCat);
        args.putInt("itemBodega",itemBodega);
        args.putInt("itemEstado",itemEstado);
        args.putInt("itemOrden",itemOrden);
        args.putInt("itemModoOrden",itemModoOrden);*/

        f.setArguments(args);
        return f;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_addcliente, container, false);


        cbTipoID = (AutoCompleteTextView) root.findViewById(R.id.actvAddCTipoID);
        cbTipoCliente = (AutoCompleteTextView) root.findViewById(R.id.actvAddCTipoCliente);
        cbOperadora = (AutoCompleteTextView) root.findViewById(R.id.actvAddCOperadora);
        tgGenero = (MaterialButtonToggleGroup) root.findViewById(R.id.tgAddCGenero);

        txtid = (TextInputEditText) root.findViewById(R.id.txtAddCID);
        txtnombre = (TextInputEditText) root.findViewById(R.id.txtAddCNombre);
        txtcorreo = (TextInputEditText) root.findViewById(R.id.txtAddCCorreo);
        txtnumero = (TextInputEditText) root.findViewById(R.id.txtAddCPhone);
        txtdir = (TextInputEditText) root.findViewById(R.id.txtAddCDireccion);

        txtidly = (TextInputLayout) root.findViewById(R.id.txtAddCIDly);
        txtnombrely = (TextInputLayout) root.findViewById(R.id.txtAddCNombreLy);
        txtcorreoly = (TextInputLayout) root.findViewById(R.id.txtAddCCorreoLy);
        txtnumeroly = (TextInputLayout) root.findViewById(R.id.txtAddCPhonely);
        txtdirly = (TextInputLayout) root.findViewById(R.id.txtAddCDireccionLy);


        cbTipoIDLy = (TextInputLayout) root.findViewById(R.id.cbAddCTipoClientely);
        cbOperadoraLy  = (TextInputLayout) root.findViewById(R.id.cbAddCOperadoraly);
        cbTipoClienteLy  = (TextInputLayout) root.findViewById(R.id.cbAddCTipoClientely);


        pb = (ProgressBar) root.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);

        UIHelper.fillCombo(cbTipoCliente, "tipocliente", -1, itemTipoCliente,-1);
        UIHelper.fillCombo(cbTipoID, "tipoid",-1 , itemTipoID,-1);
        UIHelper.fillCombo(cbOperadora, "oper", -1, itemOperadora,-1);

        return root;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.app_bar_search).setVisible(false);
        menu.findItem(R.id.mnu_bar_setting).setVisible(false);
        menu.findItem(R.id.mnu_bar_filtro).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_bar_save:
                addCliente();
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tgGenero.check(R.id.btAddCMasculino); itemGenero=1;
        tgGenero.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId==R.id.btAddCMasculino)     itemGenero = isChecked?1:2;
            }
        });

        cbTipoCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clsListItem item = (clsListItem)adapterView.getItemAtPosition(i);
                itemTipoCliente=item.ID;
            }
        });

        cbTipoID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemTipoID = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbOperadora.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemOperadora = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });


    }


    private String validateParams(){

        if(itemTipoID<1) {
            cbTipoIDLy.setError("Seleccione Tipo ID");
            return "Seleccione Tipo ID";
        }
        if(txtid.getText().toString().equals("")){
            txtidly.setError("Ingrese un Número ID"); return "Ingrese un Número de Identificación"; }
        if(txtnombre.getText().toString().equals("")) {
            txtnombrely.setError("Ingrese un Nombre del Cliente");  return "Ingrese Nombre del Cliente";
        }

        if(!txtcorreo.getText().toString().equals("")) {
            if(!Patterns.EMAIL_ADDRESS.matcher(txtcorreo.getText().toString().trim()).matches()){
                txtcorreoly.setError("Ingrese Correo Válido");  return "Ingrese Correo Válido";
            }
        }


        /*
        if(itemOperadora<1) {
            cbOperadoraLy.setError("Seleccione Operadora");
            return "Seleccione Operadora";
        }
        if(txtnumero.getText().toString().equals("")) {
            txtnumeroly.setError("Ingrese Número de Teléfono");  return "Ingrese Número de Teléfono";
        }
        if(txtdir.getText().toString().equals("")) {
            txtdirly.setError("Ingrese Dirección");  return "Ingrese Dirección";
        }
        if(itemTipoCliente<1) {
            cbTipoClienteLy.setError("Seleccione Tipo de Cliente");
            return "Seleccione Tipo de Cliente";
        }*/


        return  "";
    }

    private boolean addCliente (){
        pb.setVisibility(View.VISIBLE);
        txtdirly.setError(null);
        txtcorreoly.setError(null);
        txtnumeroly.setError(null);
        txtnombrely.setError(null);
        txtidly.setError(null);
        cbTipoIDLy.setError(null);
        cbOperadoraLy.setError(null);
        cbTipoClienteLy.setError(null);

        String resp=validateParams();
        if(!resp.equals("")){
            pb.setVisibility(View.GONE);
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Error en el Formulario")
                    .setIcon(R.drawable.ic_baseline_error_24)
                    .setMessage(resp)
                    .setPositiveButton("Ok", null)
                    .show();

            return false;
        }

        Map<String, String> params = new HashMap();
        params.put("nombre", txtnombre.getText().toString().trim().toUpperCase(Locale.ROOT));
        params.put("identificacion", txtid.getText().toString().trim());
        params.put("tipoidentificacion", itemTipoID.toString());

        params.put("direccion", txtdir.getText().toString().trim());
        if(!txtcorreo.getText().toString().equals(""))
            params.put("correo", txtcorreo.getText().toString().trim());

        if(itemGenero>0) params.put("genero", itemGenero.toString());

        if(!txtnumero.getText().toString().equals(""))
             params.put("telefonomovil", txtnumero.getText().toString().trim());

        if(itemOperadora>0) params.put("operadora", itemOperadora.toString());
        if(itemTipoCliente>0) params.put("tipo", itemTipoCliente.toString());


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(new JsonObjectRequest
                (Request.Method.POST, GlobalInfo.URL_AddCliente, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Snackbar snackbar = Snackbar.make(getView(), "Cliente creado Correctamente!!", Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();

                        pb.setVisibility(View.GONE);
                    }
                },new HTTPErrorResponseDialog(getContext(), pb)
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UIHelper.getAuthHearders();
            }
        });

        pb.setVisibility(View.GONE);
        return true;
    }





}
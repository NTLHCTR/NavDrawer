package uteq.solutions.navdrawer.ui.productos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.helper.DecimalDigitsInputFilter;
import uteq.solutions.navdrawer.helper.UIHelper;
import uteq.solutions.navdrawer.model.clsListItem;


public class fragment_addproductopart1 extends Fragment {

    AutoCompleteTextView cbCat, cbSubCat, cbImpuesto;
    TextInputLayout txtbbarcodely, txtdescly, cbCatly, cbSybCatly, cbImpLy;
    TextInputLayout txtcostoly, txtpvply, txtpvpmly;
    TextInputEditText txtbarcode, txtdesc, txtcosto, txtpvp, txtpvpm;

    String barcodeOriginal;

    MaterialButtonToggleGroup tgClase;

    Integer  itemCat=-1, itemSubCat=-1, itemImpuesto=-1, itemClase=1;

    private String Modo;

    public fragment_addproductopart1() {
            int a=0;
    }


    public static fragment_addproductopart1 newInstance(String _Modo) {
        fragment_addproductopart1 fragment = new fragment_addproductopart1();
        Bundle args = new Bundle();
        args.putString("Modo",_Modo);
        fragment.setArguments(args);
        return fragment;
    }


    public static fragment_addproductopart1 newInstance(String _Modo,String Desc, String Cod,
                                                        String Costo, String PVP,
                                                        String PVPM, int Cat, int SubCat, int Imp, int Clase) {
        fragment_addproductopart1 fragment = new fragment_addproductopart1();
        Bundle args = new Bundle();
        args.putString("Modo",_Modo);
        args.putString("Desc",Desc);
        args.putString("Cod",Cod);
        args.putInt("Cat",Cat);
        args.putInt("SubCat",SubCat);
        args.putInt("Imp",Imp);
        args.putInt("Clase",Clase);
        args.putDouble("Costo",Double.parseDouble(Costo));
        args.putDouble("PVP",Double.parseDouble(PVP));
        args.putDouble("PVPM",Double.parseDouble(PVPM));

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Modo = getArguments().getString("Modo");
            if(Modo.equals("Upd")) {
                barcodeOriginal=getArguments().getString("Cod");;
                itemCat = getArguments().getInt("Cat");
                itemSubCat = getArguments().getInt("SubCat");
                itemImpuesto = getArguments().getInt("Imp");
                itemClase = getArguments().getInt("Clase");
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_addproductopart1, container, false);

        cbCat = (AutoCompleteTextView) root.findViewById(R.id.actvAddCat);
        cbSubCat = (AutoCompleteTextView) root.findViewById(R.id.actvAddSubCat);
        cbImpuesto = (AutoCompleteTextView) root.findViewById(R.id.actvAddImpuesto);
        txtbarcode = (TextInputEditText) root.findViewById(R.id.txtbbarcode);
        txtdesc = (TextInputEditText) root.findViewById(R.id.txtdesc);
        txtcosto = (TextInputEditText) root.findViewById(R.id.txtAddPCosto);
        txtpvp = (TextInputEditText) root.findViewById(R.id.txtAddPPVP);
        txtpvpm = (TextInputEditText) root.findViewById(R.id.txtAddPPvpM);

        txtbbarcodely = (TextInputLayout) root.findViewById(R.id.txtbarcodely);
        txtdescly = (TextInputLayout) root.findViewById(R.id.txtdescly);
        txtcostoly = (TextInputLayout) root.findViewById(R.id.lblCosto);
        txtpvply = (TextInputLayout) root.findViewById(R.id.lblPVP);
        txtpvpmly = (TextInputLayout) root.findViewById(R.id.lblPvPMay);
        cbImpLy  = (TextInputLayout) root.findViewById(R.id.cbAddPImpuesto);

        tgClase = (MaterialButtonToggleGroup) root.findViewById(R.id.tgAddP1Clase);

        cbCatly = (TextInputLayout) root.findViewById(R.id.cbAddCat);
        cbSybCatly  = (TextInputLayout) root.findViewById(R.id.cbAddSubCat);


        if(Modo.equals("Add")) {
            UIHelper.fillCombo(cbCat, "cat", -1, -1, -1);
            UIHelper.fillCombo(cbImpuesto, "imp", -1, -1, -1);
        }else{
            UIHelper.fillCombo(cbCat, "cat", -1, itemCat,-1);
            UIHelper.fillCombo(cbSubCat, "subcat", itemCat, itemSubCat,-1);
            UIHelper.fillCombo(cbImpuesto, "imp", -1, itemImpuesto,-1);

            tgClase.check(itemClase==1?R.id.btAddP1Bien:R.id.btAddP1Servicio);
            txtbarcode.setText(getArguments().getString("Cod"));
            txtdesc.setText(getArguments().getString("Desc"));
            txtcosto.setText(String.format("%,.2f",getArguments().getDouble("Costo")));
            txtpvp.setText(String.format("%,.2f",getArguments().getDouble("PVP")));
            txtpvpm.setText(String.format("%,.2f",getArguments().getDouble("PVPM")));
        }




        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtcosto.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtpvp.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        txtpvpm.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

        //tgClase.check(R.id.btAddP1Bien);

        tgClase.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId==R.id.btAddP1Bien)     itemClase = isChecked?1:2;
            }
        });

        cbCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clsListItem item = (clsListItem)adapterView.getItemAtPosition(i);
                itemCat=item.ID;
                if(item.ID>0)
                    UIHelper.fillCombo(cbSubCat, "subcat", item.ID, -1,-1);
            }
        });

        cbSubCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSubCat = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });

        cbImpuesto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemImpuesto = ((clsListItem)adapterView.getItemAtPosition(i)).ID;
            }
        });




    }

}
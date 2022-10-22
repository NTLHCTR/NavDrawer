package uteq.solutions.navdrawer.ui.facturacion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.adapter.adaptador_TotalesImpFactura;
import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.model.clsFacturaTotalImpuestos;
import uteq.solutions.navdrawer.model.clsItemFactura;
import uteq.solutions.navdrawer.model.clsListItemTotalImpFactura;
import uteq.solutions.navdrawer.model.clsProducto;


public class fragment_Factura extends Fragment {

    private TabLayout tabLayout;
    //FragmentManager fragmentManager;
    //FragmentTransaction fragmentTransaction;
    FrameLayout frameLayout;

    ImageView arrow;
    ConstraintLayout hiddenView;
    CardView cardView;

    private fragment_encabezadoFactura fragmentEncabezado;
    private fragment_detallefactura fragmentDetalle;
    private fragment_FormasPago fragmentFormasPago;
    private fragment_detfaclistaproductos fragmentDetalleListaProds;

    NavController navController;

    ProgressBar pb ;

    private viewModelFacturacion viewModel;

    private RecyclerView lvImp, lvImpValores;
    private TextView lblTotalHead, lblTitSubSinImp, txtSubSinImpuiesto, lblTitDescuento, txtDescuento;
    private TextView lblTituloICE, txtICE, lblTitTotalPagar, txtTotalAPagar;

    public fragment_Factura() {
        // Required empty public constructor
    }


    public static fragment_Factura newInstance(String param1, String param2) {
        fragment_Factura fragment = new fragment_Factura();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View r=inflater.inflate(R.layout.fragment_factura, container, false);

        //viewPager = r.findViewById(R.id.viewPager);
        tabLayout = r.findViewById(R.id.tab_layoutFactura);
        frameLayout = (FrameLayout) r.findViewById(R.id.frameLayout);

        fragmentEncabezado = new fragment_encabezadoFactura();
        fragmentDetalle = new fragment_detallefactura();
        fragmentFormasPago = new fragment_FormasPago();
        fragmentDetalleListaProds = new fragment_detfaclistaproductos();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragmentEncabezado)
                .commit();

        pb = (ProgressBar) r.findViewById(R.id.idLoadingPB);
        pb.setVisibility(View.GONE);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, fragmentEncabezado)
                                .commit();

                        break;
                    case 1:
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, fragmentDetalle)
                                .commit();
                        break;
                    case 2:
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, fragmentDetalleListaProds)
                                .commit();
                        break;
                    case 3:
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, fragmentFormasPago)
                                .commit();
                        break;
               }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       // cardView = r.findViewById(R.id.lyTotales);
        arrow = r.findViewById(R.id.imgarrow);
        hiddenView = r.findViewById(R.id.lytotaleshidden);
        hiddenView.setVisibility(View.GONE);

        lvImp = (RecyclerView)r.findViewById(R.id.lvImp);
        lvImpValores = (RecyclerView)r.findViewById(R.id.lvImpValores);

        lblTotalHead = (TextView)r.findViewById(R.id.lblTotalHead);
        lblTitSubSinImp = (TextView)r.findViewById(R.id.lblTitSubSinImp);
        txtSubSinImpuiesto = (TextView)r.findViewById(R.id.txtSubSinImpuiesto);
        lblTitDescuento = (TextView)r.findViewById(R.id.lblTitDescuento);
        txtDescuento = (TextView)r.findViewById(R.id.txtDescuento);
        lblTituloICE = (TextView)r.findViewById(R.id.lblTituloICE);
        txtICE = (TextView)r.findViewById(R.id.txtICE);
        lblTitTotalPagar = (TextView)r.findViewById(R.id.lblTitTotalPagar);
        txtTotalAPagar = (TextView)r.findViewById(R.id.txtTotalAPagar);


        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(hiddenView, new AutoTransition());
                    hiddenView.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                } else {

                    txtSubSinImpuiesto.setText( String.format("%.2f", viewModel.totalsinimpuestos.getValue()));
                    if (viewModel.totaldescuento.getValue()>0) {
                        txtDescuento.setVisibility(View.VISIBLE); lblTitDescuento.setVisibility(View.VISIBLE);
                        txtDescuento.setText(String.format("%.2f", viewModel.totaldescuento.getValue()));
                    }else{
                        txtDescuento.setVisibility(View.GONE); lblTitDescuento.setVisibility(View.GONE);
                    }
                    if (viewModel.totalice.getValue()>0) {
                        txtICE.setVisibility(View.VISIBLE); lblTituloICE.setVisibility(View.VISIBLE);
                        txtICE.setText(String.format("%.2f", viewModel.totalice.getValue()));
                    }else{
                        txtICE.setVisibility(View.GONE); lblTituloICE.setVisibility(View.GONE);
                    }

                    List<clsListItemTotalImpFactura> lstItemTotalesImp = new ArrayList<clsListItemTotalImpFactura>();
                    List<clsListItemTotalImpFactura> lstItemSUbTotalesImp = new ArrayList<clsListItemTotalImpFactura>();

                    ArrayList<clsFacturaTotalImpuestos> lstTotalesImp = viewModel.getlstTotalesImpuestos().getValue();

                    for (clsFacturaTotalImpuestos itemTotalImp:lstTotalesImp ) {
                        if(itemTotalImp.tipoimpuesto== GlobalInfo.CONST_tipoImpuestoIVA){
                            lstItemTotalesImp.add(new clsListItemTotalImpFactura(itemTotalImp.descripcionimp,
                                    itemTotalImp.valor));
                            lstItemSUbTotalesImp.add(new clsListItemTotalImpFactura("SubTotal " + itemTotalImp.descripcionimp,
                                    itemTotalImp.baseimponible));
                        }
                    }


                    adaptador_TotalesImpFactura adapter = new adaptador_TotalesImpFactura(lstItemSUbTotalesImp);
                    lvImp.setHasFixedSize(true);
                    lvImp.setLayoutManager(new LinearLayoutManager(getContext()));
                    lvImp.setAdapter(adapter);


                    adaptador_TotalesImpFactura adapter2 = new adaptador_TotalesImpFactura(lstItemTotalesImp);
                    lvImpValores.setHasFixedSize(true);
                    lvImpValores.setLayoutManager(new LinearLayoutManager(getContext()));
                    lvImpValores.setAdapter(adapter2);


                    txtTotalAPagar.setText( String.format("%.2f", viewModel.importetotal.getValue()));

                    TransitionManager.beginDelayedTransition(hiddenView, new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);
                    arrow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                }
            }
        });

        return r;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.app_bar_search).setVisible(false);
        menu.findItem(R.id.mnu_bar_setting).setVisible(false);
        menu.findItem(R.id.mnu_bar_filtro).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_bar_save:
                save();
                return true;
            default:
                break;
        }

        return false;
    }

    public void save(){
       /* Toast.makeText(getContext(),
                "Text: " + viewModel.getText().getValue().toString() + "\n" +
                "Valor: " +  viewModel.getValor().getValue() + "\n" +
                "Costo: " + viewModel.getCosto().getValue()  +"\n" +
                "Texto;: "+  viewModel.getTexto().getValue(), Toast.LENGTH_LONG).show();*/
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(viewModelFacturacion.class);
        viewModel.getlstProductos().observe(getViewLifecycleOwner(), lstproducts -> {
            Double total=0.0;
            for (clsItemFactura producto : lstproducts) {
                    total+=producto.getSubTotalItem();
            }
            lblTotalHead.setText(String.format("%.2f",total));

        });
    }


}
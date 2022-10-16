package uteq.solutions.navdrawer.ui.facturacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import uteq.solutions.navdrawer.R;


public class fragment_detfaclistaproductos extends Fragment {

    RecyclerView rvListaProd;
    public fragment_detfaclistaproductos() {
        // Required empty public constructor
    }


    public static fragment_detfaclistaproductos newInstance(String param1, String param2) {
        fragment_detfaclistaproductos fragment = new fragment_detfaclistaproductos();
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

        View root = inflater.inflate(R.layout.fragment_detfaclistaproductos, container, false);
        rvListaProd = (RecyclerView) root.findViewById(R.id.rvListaProductos);
        return root;
    }
}
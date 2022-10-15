package uteq.solutions.navdrawer.ui.facturacion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import uteq.solutions.navdrawer.R;


public class fragment_detallefactura extends Fragment {

    private SharedViewModel viewModel;
    private EditText editText;

    public fragment_detallefactura() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_detallefactura.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_detallefactura newInstance(String param1, String param2) {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detallefactura, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        editText = view.findViewById(R.id.edit_text);
        Button button = view.findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCosto(Double.parseDouble(editText.getText().toString()));
                viewModel.setTexto("Campos Texto ID: " + editText.getText().toString());
            }
        });
    }
}
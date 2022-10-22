package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uteq.solutions.navdrawer.R;

public class clsListItemTotalImpFactura {
    public String descripcion;
    public Double Total;

    public clsListItemTotalImpFactura( String descripcion, Double Total){
        this.Total = Total;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return  descripcion;
    }

}

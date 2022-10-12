package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uteq.solutions.navdrawer.R;

public class clsListItem {
    public int ID;
    public String descripcion;
    public int icon;

    public clsListItem(int ID, String descripcion, Integer icon){
        this.ID = ID;
        this.descripcion = descripcion;
        this.icon = icon==null?R.drawable.ic_baseline_arrow_right_24:icon;
    }

    public clsListItem(clsListItem copy){
        this.ID = copy.ID;
        this.descripcion = copy.descripcion;
        this.icon = copy.icon;
    }

    public clsListItem(JSONObject a) throws JSONException {
        descripcion =  a.getString("descripcion") ;
        ID=a.getInt("id");
        this.icon = R.drawable.ic_baseline_arrow_right_24;
    }

    @Override
    public String toString() {
        return  descripcion;
    }

    public static ArrayList<clsListItem> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsListItem> lista = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            lista.add(new clsListItem(datos.getJSONObject(i)));
        }
        return lista;
    }
}

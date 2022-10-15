package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class clsProducto {

    public String descripcion;
    public String categoria;
    public double valor;
    public int ID;

    public clsProducto(JSONObject a) throws JSONException {
        descripcion =  a.getString("descripcion") ;
        categoria=a.getString("p_categoria");
        valor = a.getDouble("costo");
        ID = a.getInt("id");


    }

    public clsProducto(String _desc, String _categoria, Double _valor, Integer _ID) {
        descripcion =  _desc;
        categoria= _categoria;
        valor = _valor;
        ID = _ID;


    }

    public static ArrayList<clsProducto> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsProducto> productos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            productos.add(new clsProducto(datos.getJSONObject(i)));
        }
        return productos;
    }
}

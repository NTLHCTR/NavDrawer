package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class clsProducto {

    public String descripcion;
    public String categoria;
    public double PVP;
    public double IVA;
    public double DESC;
    public String ImpuestoDescr;
    public String UnidadMedida;
    public int ID;

    public clsProducto(JSONObject a) throws JSONException {
        ID = a.getInt("id");
        descripcion =  a.getString("descripcion") ;
        categoria=a.getString("p_categoria");
        PVP = a.getDouble("precio_unidad");
        IVA =  a.getDouble("p_impuestovalor");
        ImpuestoDescr = a.getString("p_impuesto") ;
        DESC = a.isNull("descuento")?0:a.getDouble("descuento");
        UnidadMedida = a.isNull("p_unidad")?"":a.getString("p_unidad");
    }

    /*public clsProducto(String _desc, String _categoria, Double _PVP, Integer _ID) {
        descripcion =  _desc;
        categoria= _categoria;
        PVP = _PVP;
        ID = _ID;
    }*/

    public static ArrayList<clsProducto> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsProducto> productos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            productos.add(new clsProducto(datos.getJSONObject(i)));
        }
        return productos;
    }
}

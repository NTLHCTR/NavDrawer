package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class clsCliente {

    public String nombre;
    public String identificacion;
    public String correo;
    public String tipocliente;
    public int ID;
    public String tipoidentificacion;

    public clsCliente(JSONObject a) throws JSONException {
        nombre =  a.getString("nombre") ;
        identificacion=a.getString("identificacion");
        tipoidentificacion = a.getString("c_tipoidentificacion");

        correo = a.getString("correo");
        if(!a.isNull("c_tipocliente"))
             tipocliente = a.getString("c_tipocliente");

        ID = a.getInt("id");


    }

    public static ArrayList<clsCliente> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsCliente> clientes = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            clientes.add(new clsCliente(datos.getJSONObject(i)));
        }
        return clientes;
    }
}

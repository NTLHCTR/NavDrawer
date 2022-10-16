package uteq.solutions.navdrawer.helper;

import android.widget.AutoCompleteTextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uteq.solutions.navdrawer.adapter.adaptador_ListItem;
import uteq.solutions.navdrawer.model.clsListItem;

public class UIHelper {

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void fillCombo(AutoCompleteTextView cb, String nombreMenu, Integer idParent, Integer idItemASeleccionar, Integer addAllItem){

            Map<String, String> params = new HashMap();
            if(nombreMenu=="subcat")
                if(idParent>0)  params.put("idcat", idParent.toString());

            if(addAllItem==1) params.put("addAllItem", "1");

            RequestQueue requestQueue = Volley.newRequestQueue(cb.getContext());
            requestQueue.add(new JsonObjectRequest
                    (Request.Method.POST, GlobalInfo.URL_ListaMenu+"/"+nombreMenu, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ArrayList<clsListItem> lista = new ArrayList<clsListItem> ();
                            try{
                                JSONArray JSONlista=  response.getJSONArray("lista");
                                lista = clsListItem.JsonObjectsBuild(JSONlista);

                                adaptador_ListItem arrayAdapter = new adaptador_ListItem(cb.getContext(), lista);
                                cb.setAdapter(arrayAdapter);
                                if(idItemASeleccionar>=0){
                                    clsListItem item= getItemListFromList (lista,idItemASeleccionar);
                                    if(item!=null)    cb.setText(item.descripcion,false);
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    },new HTTPErrorResponseDialog(cb.getContext())
                    ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getAuthHearders();
                }
            });
        }


    public static Map<String, String> getAuthHearders(){
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", "Bearer " + GlobalInfo.Token);
        return headerMap;
    }

    public static clsListItem getItemListFromList(ArrayList<clsListItem> lista , Integer itemIDToSearch){
        for (clsListItem item:lista ) {
            if(itemIDToSearch==item.ID) return item;
        }
        return null;
    }


    public static  void fillCombo(AutoCompleteTextView cb, ArrayList<clsListItem> lista, Integer idItemASeleccionar ){
        adaptador_ListItem arrayAdapter = new adaptador_ListItem(cb.getContext(), lista);
        cb.setAdapter(arrayAdapter);
        clsListItem item= getItemListFromList (lista,idItemASeleccionar);
        if(item!=null){
            cb.setText(item.descripcion,false);
        }
    }
}

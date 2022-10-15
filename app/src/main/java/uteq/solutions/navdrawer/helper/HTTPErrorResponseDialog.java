package uteq.solutions.navdrawer.helper;

import android.content.Context;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.ItemTouchHelper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.model.clsProducto;

public class HTTPErrorResponseDialog implements  Response.ErrorListener{

    private Context cntx;
    private ProgressBar pb=null;

    public HTTPErrorResponseDialog(Context context) {    cntx=context;   }
    public HTTPErrorResponseDialog(Context context, ProgressBar pb) { cntx=context; this.pb = pb; }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse!=null) {
            try {
                JSONObject msg = new JSONObject(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                if(msg.has("error"))
                  new MaterialAlertDialogBuilder(cntx)
                        .setTitle("Error  " + error.networkResponse.statusCode)
                        .setIcon(R.drawable.ic_baseline_error_24)
                        .setMessage(msg.getString("error").toString())
                        .setPositiveButton("Ok", null)
                        .show();
                else {
                    JSONArray vals=  msg.getJSONArray("validaciones");
                    CharSequence[] items = new String[vals.length()];
                    for (int i = 0; i < vals.length(); i++) {
                        items[i]  = vals.getJSONObject(i).getString("mensaje");
                    }
                    new MaterialAlertDialogBuilder(cntx)
                            .setTitle("Error: Lista de Validaciones")
                            .setIcon(R.drawable.ic_baseline_error_24)
                            .setItems(items, null)
                            .setPositiveButton("Ok", null)
                            .show();
                }

            } catch(JSONException e){
                e.printStackTrace();
            }
        }else{
            new MaterialAlertDialogBuilder(cntx)
                    .setTitle("Error")
                    .setIcon(R.drawable.ic_baseline_error_24)
                    .setMessage(error.getMessage().toString())
                    .setPositiveButton("Ok", null)
                    .show();
        }

        if(pb!=null)        pb.setVisibility(View.GONE);
    }
}

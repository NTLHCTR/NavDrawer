package uteq.solutions.navdrawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.model.clsListItem;

public class adaptador_ListItem extends ArrayAdapter<clsListItem> {

    public adaptador_ListItem(Context context, List<clsListItem> listItem ) {
        super(context, R.layout.layout_itemlist, R.id.textitemlist, listItem);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rootview= LayoutInflater.from(getContext()).inflate(R.layout.layout_itemlist,viewGroup,false);
        TextView txtnombre=rootview.findViewById(R.id.textitemlist);
        ImageView iconitem=rootview.findViewById(R.id.iconitemlist);
        txtnombre.setText( getItem(position).descripcion);
        iconitem.setImageResource(getItem(position).icon);
        return rootview;
    }


}
package gulak.util;

/**
 * Created by naresh on 5/11/15.
 */

import android.widget.ArrayAdapter;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gulak.chittyalpha.R;

public class ChittyRowAdapter extends ArrayAdapter<ChittyRow> {

    private final Context context;
    private final ArrayList<ChittyRow> data;


    public ChittyRowAdapter(Context context, ArrayList<ChittyRow> data) {
        super(context, R.layout.chittyrow , data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.chittyrow , parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.txtChittyDetails);
        TextView textVal = (TextView) rowView.findViewById(R.id.txtChittyVal);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.chittyImageIcon);
        textView.setText(data.get(position).getFromPhone());
        textVal.setText(data.get(position).getChittyVal());
        System.out.println(" Values of Chitty Id :" +data.get(position).getChittyId() );
        /*Set Chitty Images
        String s = values[position];
        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        textView.setTag(data.get(position).getAtIndex());

        return rowView;
    }
}

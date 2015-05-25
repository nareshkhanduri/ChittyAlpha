package gulak.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gulak.chittyalpha.R;

/**
 * Created by naresh on 5/12/15.
 */
public class IssuedParchiRowAdapter extends ArrayAdapter<ChittyRow> {

    private final Context context;
    private final ArrayList<ChittyRow> data;


    public IssuedParchiRowAdapter(Context context, ArrayList<ChittyRow> data) {
        super(context, R.layout.parchi_issued_row , data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.parchi_issued_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.txtParchiDetails);
        TextView textVal = (TextView) rowView.findViewById(R.id.txtParchiVal);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.parchiImageIcon);
        textView.setText(data.get(position).getToPhone());
        textVal.setText(data.get(position).getChittyVal());
        //System.out.println(" Values of Chitty Id :" +data.get(position).getChittyId() );
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


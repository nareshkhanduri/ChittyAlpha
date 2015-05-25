package gulak.util;

/**
 * Created by naresh on 5/11/15.
 */

import android.widget.ArrayAdapter;
import android.content.Context;
import android.text.style.RelativeSizeSpan;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableString;
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
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.chittyImageIcon);
        StringBuffer sb = new StringBuffer();
        sb.append(data.get(position).getBusinessname());
        sb.append(System.getProperty("line.separator"));
        sb.append(data.get(position).getTranstime());
        SpannableString ss1=  new SpannableString(sb.toString());

        ss1.setSpan(new RelativeSizeSpan(1f), 0,data.get(position).getBusinessname().length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, data.get(position).getBusinessname().length(), 0);// set color
        textView.setText(ss1);
        textVal.setText(data.get(position).getChittyVal());
        ImageView imgView =(ImageView) rowView.findViewById(R.id.parchiImageIcon);
        imgView.setImageResource(R.drawable.payone);
        if(data.get(position).getChittyVal().equalsIgnoreCase("5"))
            imgView.setImageResource(R.drawable.payfive);
        if(data.get(position).getChittyVal().equalsIgnoreCase("9"))
            imgView.setImageResource(R.drawable.paynine);
        if(data.get(position).getChittyVal().equalsIgnoreCase("7"))
            imgView.setImageResource(R.drawable.payseven);
        if(data.get(position).getChittyVal().equalsIgnoreCase("2"))
            imgView.setImageResource(R.drawable.paytwo);
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

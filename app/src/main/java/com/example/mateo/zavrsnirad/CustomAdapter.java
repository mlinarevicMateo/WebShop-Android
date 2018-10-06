package com.example.mateo.zavrsnirad;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<Article> result = new ArrayList<>();
    Context context;

    public CustomAdapter(Context c, ArrayList<Article> artiklList) {
        // TODO Auto-generated constructor stub
        result = artiklList;
        context = c;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.article, null);
        holder.os_text = rowView.findViewById(R.id.os_texts);
        holder.os_img = rowView.findViewById(R.id.cart_item_imageview);
        holder.os_price = rowView.findViewById(R.id.os_prices);

        if (result.size() != 0) {
            holder.os_text.setText(result.get(position).getName());
            holder.os_price.setText(result.get(position).getPrice() + " KM");

            Picasso.with(context).load(context.getString(R.string.IMAGES_URL) + result.get(position).getImage()).into(holder.os_img);
            holder.os_img.requestLayout();

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            holder.os_img.getLayoutParams().height = display.getWidth() / 2;
        }
        return rowView;
    }

    public class Holder {
        TextView os_text;
        ImageView os_img;
        TextView os_price;
    }

}
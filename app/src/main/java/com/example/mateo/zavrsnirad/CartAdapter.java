package com.example.mateo.zavrsnirad;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CartAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<ArticleInCart> result = new ArrayList<>();
    Context context;
    private CartAdapter adapter;

    public CartAdapter(Context c, ArrayList<ArticleInCart> artiklList) {
        // TODO Auto-generated constructor stub
        result = artiklList;
        context = c;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.adapter = this;
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
        final Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.cart_item, null);
        holder.cart_heading = rowView.findViewById(R.id.cart_item_heading);
        holder.cart_image = rowView.findViewById(R.id.cart_item_imageview);
        holder.cart_quantity = rowView.findViewById(R.id.cart_item_quantity);
        holder.btn_minus = rowView.findViewById(R.id.cart_item_quantity_minus);
        holder.btn_plus = rowView.findViewById(R.id.cart_item_quantity_plus);
        holder.btn_remove = rowView.findViewById(R.id.cart_item_remove_button);

        holder.cart_heading.setText(result.get(position).getArticle().getName());
        holder.cart_quantity.setText(String.valueOf(result.get(position).getQuantity()));

        Picasso.with(context).load(context.getString(R.string.IMAGES_URL) + result.get(position).getArticle().getImage()).into(holder.cart_image);
        holder.cart_image.requestLayout();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        holder.cart_image.getLayoutParams().height = display.getWidth() / 2;

        final Cart cart = Cart.getInstance();
        final ArrayList<ArticleInCart> articles = cart.getArticlesInCart();

        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.valueOf(String.valueOf(holder.cart_quantity.getText()));
                if (quantity > 1) {
                    cart.CartQuantityMinus(position);
                    quantity--;
                    holder.cart_quantity.setText(String.valueOf(quantity));
                }
            }
        });

        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.valueOf(String.valueOf(holder.cart_quantity.getText()));
                if (quantity < Integer.valueOf(articles.get(position).getArticle().getQuantity())) {
                    cart.CartQuantityPlus(position);
                    quantity++;
                    holder.cart_quantity.setText(String.valueOf(quantity));
                }
            }
        });

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articles.remove(position);
                adapter.notifyDataSetChanged();
                if (articles.isEmpty()) {
                    Toast.makeText(context, R.string.cart_is_empty, Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                }
            }
        });

        return rowView;
    }

    public class Holder {
        TextView cart_heading;
        ImageView cart_image;
        TextView cart_quantity;
        Button btn_plus;
        Button btn_minus;
        TextView btn_remove;
    }

}
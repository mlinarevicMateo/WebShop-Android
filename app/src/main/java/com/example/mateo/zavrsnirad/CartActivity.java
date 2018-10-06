package com.example.mateo.zavrsnirad;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends Activity {

    GridView gridview;
    ArrayList<ArticleInCart> articlesInCart;
    Button btn_clear;
    Button btn_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btn_buy = findViewById(R.id.cart_buy);
        btn_clear = findViewById(R.id.cart_clear);

        final Cart cart = Cart.getInstance();

        if (cart.getArticlesInCart().isEmpty()) {
            Toast.makeText(getBaseContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            finish();
        }

        articlesInCart = cart.getArticlesInCart();

        gridview = findViewById(R.id.cartgridview);
        gridview.setAdapter(new CartAdapter(this, articlesInCart));


        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(CartActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(CartActivity.this);
                }
                builder.setTitle(R.string.buy_articles)
                        .setMessage(R.string.buy_articles_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), R.string.articles_buyed, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();


            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(CartActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(CartActivity.this);
                }
                builder.setTitle(R.string.clear_cart)
                        .setMessage(R.string.clr_cart_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                cart.getArticlesInCart().clear();
                                Toast.makeText(getBaseContext(), R.string.cart_cleared, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();

            }
        });
    }


}

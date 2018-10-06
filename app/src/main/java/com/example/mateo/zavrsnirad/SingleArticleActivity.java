package com.example.mateo.zavrsnirad;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class SingleArticleActivity extends Activity {

    ImageView image;
    TextView heading, category, price, description;
    Button add_to_cart;

    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_article);

        Intent i = getIntent();
        article = (Article) i.getSerializableExtra("Article");

        image = findViewById(R.id.single_article_imageview);

        heading = findViewById(R.id.single_article_heading);
        category = findViewById(R.id.single_article_category);
        price = findViewById(R.id.single_article_price);
        description = findViewById(R.id.single_article_description);

        add_to_cart = findViewById(R.id.add_to_cart_button);

        Picasso.with(SingleArticleActivity.this).load(SingleArticleActivity.this.getString(R.string.IMAGES_URL) + article.getImage()).into(image);

        heading.setText(article.getName());
        price.setText(article.getPrice() + getString(R.string.currency));
        category.setText(article.getCategory());
        description.setText(article.getDescription());

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(SingleArticleActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(SingleArticleActivity.this);
                }
                builder.setTitle(R.string.add_to_cart)
                        .setMessage(R.string.add_to_cart_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Cart cart = Cart.getInstance();

                                if (cart.addToCart(new ArticleInCart(1, article))) {
                                    Toast.makeText(getBaseContext(), R.string.article_added, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getBaseContext(), R.string.article_already_added, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

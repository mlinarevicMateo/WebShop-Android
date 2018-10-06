package com.example.mateo.zavrsnirad;

import java.util.ArrayList;

public class Cart {
    private static final Cart instance = new Cart();
    private ArrayList<ArticleInCart> articlesInCart;

    private Cart() {
        articlesInCart = new ArrayList<>();
    }

    public static Cart getInstance() {
        return instance;
    }

    public boolean addToCart(ArticleInCart articleInCart) {

        if (checkIfInCart(articleInCart)) {
            return false;
        } else {
            this.articlesInCart.add(articleInCart);
            return true;
        }
    }

    private boolean checkIfInCart(ArticleInCart articleInCart) {
        for (ArticleInCart art : articlesInCart) {
            Article a = art.getArticle();
            Article article = articleInCart.getArticle();
            if (a.getName().equals(article.getName())
                    && a.getCategory().equals(article.getCategory())
                    && a.getDescription().equals(article.getDescription())
                    && a.getImage().equals(article.getImage())
                    && a.getPrice().equals(article.getPrice())
                    && a.getQuantity().equals(article.getQuantity())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ArticleInCart> getArticlesInCart() {
        return articlesInCart;
    }

    public void CartQuantityMinus(int position) {
        int q = Integer.valueOf(articlesInCart.get(position).getQuantity()) - 1;
        articlesInCart.get(position).setQuantity(q);
    }

    public void CartQuantityPlus(int position) {
        int q = Integer.valueOf(articlesInCart.get(position).getQuantity()) + 1;
        articlesInCart.get(position).setQuantity(q);
    }
}

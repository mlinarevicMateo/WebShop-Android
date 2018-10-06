package com.example.mateo.zavrsnirad;

public class ArticleInCart {

    private int quantity;
    private Article article;

    public ArticleInCart(int quantity, Article article) {
        this.quantity = quantity;
        this.article = article;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}

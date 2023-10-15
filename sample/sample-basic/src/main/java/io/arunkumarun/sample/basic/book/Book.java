package io.arunkumarun.sample.basic.book;

import io.arunkumarun.sample.basic.store.Store;

import java.util.List;
import java.util.Set;

public class Book {
    private Integer id;
    private String title;
    private Set<Author> authors;
    private List<Store> sellingStores;

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public List<Store> getSellingStores() {
        return sellingStores;
    }

    public void setSellingStores(List<Store> sellingStores) {
        this.sellingStores = sellingStores;
    }
}

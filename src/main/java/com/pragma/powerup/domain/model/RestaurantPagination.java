package com.pragma.powerup.domain.model;

import java.util.List;

public class RestaurantPagination<T> {

    private List<T> restaurants;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public RestaurantPagination(List<T> restaurants, int currentPage, int totalPages, long totalItems) {
        this.restaurants = restaurants;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<T> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<T> restaurants) {
        this.restaurants = restaurants;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
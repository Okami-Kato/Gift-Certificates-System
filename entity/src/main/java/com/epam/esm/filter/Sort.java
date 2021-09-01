package com.epam.esm.filter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Sort {
    private List<Order> orders = new LinkedList<>();

    public static Sort by(Order... orders) {
        Sort sort = new Sort();
        sort.orders.addAll(Arrays.asList(orders));
        return sort;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Sort and(Sort sort) {
        Sort newSort = new Sort();
        newSort.orders.addAll(this.orders);
        newSort.orders.addAll(sort.orders);
        return newSort;
    }

    public static class Order {
        private String property;
        private SortDirection direction;

        public Order() {
        }

        private Order(String property, SortDirection sortDirection) {
            this.property = property;
            this.direction = sortDirection;
        }

        public static Order asc(String property) {
            return new Order(property, SortDirection.ASCENDING);
        }

        public static Order desc(String property) {
            return new Order(property, SortDirection.DESCENDING);
        }

        public SortDirection getDirection() {
            return direction;
        }

        public void setDirection(SortDirection direction) {
            this.direction = direction;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }
}

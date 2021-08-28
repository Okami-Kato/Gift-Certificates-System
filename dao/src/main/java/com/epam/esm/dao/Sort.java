package com.epam.esm.dao;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Sort {
    private final String ORDER_QUERY = "ORDER BY";
    private final List<Order> orders = new LinkedList<>();

    public static Sort by(Order... orders) {
        Sort sort = new Sort();
        sort.orders.addAll(Arrays.asList(orders));
        return sort;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Sort and(Sort sort) {
        Sort newSort = new Sort();
        newSort.orders.addAll(this.orders);
        newSort.orders.addAll(sort.orders);
        return newSort;
    }

    public String getQuery() {
        StringBuilder finalQuery = new StringBuilder(ORDER_QUERY);
        for (Order order : orders) {
            finalQuery.append(" ").append(order.property).append(" ").append(order.direction.getKeyWord()).append(",");
        }
        finalQuery.deleteCharAt(finalQuery.length() - 1); //removes last comma
        return finalQuery.toString();
    }

    public static class Order {
        private final String property;
        private final SortDirection direction;

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

        public String getProperty() {
            return property;
        }
    }
}

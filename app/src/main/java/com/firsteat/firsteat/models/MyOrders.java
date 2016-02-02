package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/15/2015.
 */
public class MyOrders {


    /**
     * status : 0
     * data : [{"id":"113","user_id":"28","sub_total":"270","discount":"0","vat":"0","surcharge":"0","total_amount":"270","comments":null,"ordered_at":"2015-12-21 05:48:00","bring_change":"0","orderDetails":[{"id":"85","order_id":"113","menu_id":"1","qty":"2","amount":"130"}],"orderAddresses":[{"id":"91","order_id":"113","address_id":"8"}],"orderStatuses":[{"id":"70","order_id":"113","order_status":"Pending","status_date":"2015-12-21 05:48:00"}]}]
     * addresses : [{"id":8,"user_id":28,"address_line_one":"abc","address_line_two":"efg","address_line_three":"hij","category":"HOME","cluster":null}]
     */

    private String status;
    /**
     * id : 113
     * user_id : 28
     * sub_total : 270
     * discount : 0
     * vat : 0
     * surcharge : 0
     * total_amount : 270
     * comments : null
     * ordered_at : 2015-12-21 05:48:00
     * bring_change : 0
     * orderDetails : [{"id":"85","order_id":"113","menu_id":"1","qty":"2","amount":"130"}]
     * orderAddresses : [{"id":"91","order_id":"113","address_id":"8"}]
     * orderStatuses : [{"id":"70","order_id":"113","order_status":"Pending","status_date":"2015-12-21 05:48:00"}]
     */

    private List<DataEntity> data;
    /**
     * id : 8
     * user_id : 28
     * address_line_one : abc
     * address_line_two : efg
     * address_line_three : hij
     * category : HOME
     * cluster : null
     */

    private List<AddressesEntity> addresses;

    public static MyOrders objectFromData(String str) {

        return new Gson().fromJson(str, MyOrders.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setAddresses(List<AddressesEntity> addresses) {
        this.addresses = addresses;
    }

    public String getStatus() {
        return status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public List<AddressesEntity> getAddresses() {
        return addresses;
    }

    public static class DataEntity {
        private String id;
        private String user_id;
        private String sub_total;
        private String discount;
        private String vat;
        private String surcharge;
        private String total_amount;
        private Object comments;
        private String ordered_at;
        private String bring_change;
        /**
         * id : 85
         * order_id : 113
         * menu_id : 1
         * qty : 2
         * amount : 130
         */

        private List<OrderDetailsEntity> orderDetails;
        /**
         * id : 91
         * order_id : 113
         * address_id : 8
         */

        private List<OrderAddressesEntity> orderAddresses;
        /**
         * id : 70
         * order_id : 113
         * order_status : Pending
         * status_date : 2015-12-21 05:48:00
         */

        private List<OrderStatusesEntity> orderStatuses;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setSub_total(String sub_total) {
            this.sub_total = sub_total;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public void setVat(String vat) {
            this.vat = vat;
        }

        public void setSurcharge(String surcharge) {
            this.surcharge = surcharge;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }

        public void setOrdered_at(String ordered_at) {
            this.ordered_at = ordered_at;
        }

        public void setBring_change(String bring_change) {
            this.bring_change = bring_change;
        }

        public void setOrderDetails(List<OrderDetailsEntity> orderDetails) {
            this.orderDetails = orderDetails;
        }

        public void setOrderAddresses(List<OrderAddressesEntity> orderAddresses) {
            this.orderAddresses = orderAddresses;
        }

        public void setOrderStatuses(List<OrderStatusesEntity> orderStatuses) {
            this.orderStatuses = orderStatuses;
        }

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getSub_total() {
            return sub_total;
        }

        public String getDiscount() {
            return discount;
        }

        public String getVat() {
            return vat;
        }

        public String getSurcharge() {
            return surcharge;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public Object getComments() {
            return comments;
        }

        public String getOrdered_at() {
            return ordered_at;
        }

        public String getBring_change() {
            return bring_change;
        }

        public List<OrderDetailsEntity> getOrderDetails() {
            return orderDetails;
        }

        public List<OrderAddressesEntity> getOrderAddresses() {
            return orderAddresses;
        }

        public List<OrderStatusesEntity> getOrderStatuses() {
            return orderStatuses;
        }

        public static class OrderDetailsEntity {
            private String id;
            private String order_id;
            private String menu_id;
            private String qty;
            private String amount;

            public static OrderDetailsEntity objectFromData(String str) {

                return new Gson().fromJson(str, OrderDetailsEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public void setMenu_id(String menu_id) {
                this.menu_id = menu_id;
            }

            public void setQty(String qty) {
                this.qty = qty;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getId() {
                return id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public String getMenu_id() {
                return menu_id;
            }

            public String getQty() {
                return qty;
            }

            public String getAmount() {
                return amount;
            }
        }

        public static class OrderAddressesEntity {
            private String id;
            private String order_id;
            private String address_id;

            public static OrderAddressesEntity objectFromData(String str) {

                return new Gson().fromJson(str, OrderAddressesEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public void setAddress_id(String address_id) {
                this.address_id = address_id;
            }

            public String getId() {
                return id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public String getAddress_id() {
                return address_id;
            }
        }

        public static class OrderStatusesEntity {
            private String id;
            private String order_id;
            private String order_status;
            private String status_date;

            public static OrderStatusesEntity objectFromData(String str) {

                return new Gson().fromJson(str, OrderStatusesEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public void setStatus_date(String status_date) {
                this.status_date = status_date;
            }

            public String getId() {
                return id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public String getOrder_status() {
                return order_status;
            }

            public String getStatus_date() {
                return status_date;
            }
        }
    }

    public static class AddressesEntity {
        private int id;
        private int user_id;
        private String address_line_one;
        private String address_line_two;
        private String address_line_three;
        private String category;
        private Object cluster;

        public static AddressesEntity objectFromData(String str) {

            return new Gson().fromJson(str, AddressesEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setAddress_line_one(String address_line_one) {
            this.address_line_one = address_line_one;
        }

        public void setAddress_line_two(String address_line_two) {
            this.address_line_two = address_line_two;
        }

        public void setAddress_line_three(String address_line_three) {
            this.address_line_three = address_line_three;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setCluster(Object cluster) {
            this.cluster = cluster;
        }

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getAddress_line_one() {
            return address_line_one;
        }

        public String getAddress_line_two() {
            return address_line_two;
        }

        public String getAddress_line_three() {
            return address_line_three;
        }

        public String getCategory() {
            return category;
        }

        public Object getCluster() {
            return cluster;
        }
    }
}

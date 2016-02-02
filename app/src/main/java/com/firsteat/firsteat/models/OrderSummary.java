package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/16/2015.
 */
public class OrderSummary {


    /**
     * status : 0
     * data : {"order":{"id":"283","user_id":"54","sub_total":"300","discount":"0","vat":"37.5","surcharge":"1.875","total_amount":"300","comments":null,"ordered_at":"2016-01-03 22:52:49","bring_change":"0","orderAddresses":[{"id":"303","order_id":"283","address_id":"54"}],"orderCoupons":[],"orderStatuses":[{"id":"248","order_id":"283","order_status":"Pending","status_date":"2016-01-03 22:52:49"}],"orderTimeSlots":[{"id":"267","order_id":"283","slot_id":"1"}]},"menu_details":[{"id":"261","order_id":"283","menu_id":"8","qty":"2","amount":"150","menu":{"id":"8","kitchen":"4","item_name":"Zero oil egg sandwich","description":"Zero oil egg sandwich","category":"1","price":"75","max_order_limit":"6","img_url":"web/menu/1451470390Zero oil egg sandwich.png","status":"0","in_stock":"0","best_suited":"Best suited for gym "}},{"id":"262","order_id":"283","menu_id":"9","qty":"2","amount":"100","menu":{"id":"9","kitchen":"4","item_name":"Juice","description":"juice is good ","category":"0","price":"50","max_order_limit":"5","img_url":"web/menu/1451470423Juice.png","status":"0","in_stock":"0","best_suited":"for everyone"}}],"user":{"id":54,"user_email":"atit@atit","auth_password":"827ccb0eea8a706c4c34a16891f84e7b","full_name":"atit","mobile":"123456789","referal_code":"NULL","user_role":2,"status":0},"address":{"id":54,"user_id":54,"address_line_one":"address line 0","address_line_two":"003 orchid","address_line_three":"Sector 49","category":"HOME","cluster":null},"slot":{"id":1,"kitchen_id":4,"start_time":"17:00:00","end_time":"18:00:00","status":0,"max_order_limit":6,"present_orders":9,"slot_date":"2015-12-30"},"offers":[{"id":"245","offer_id":"7","order_id":"283","offer":{"id":"7","offer_name":"testing","description":"f;ljlkdsvnfljlsfdkvbsflkdgbslfkdgbdflkgbsfkj","qty":"2","price":"30","menu_id":"8","status":"0","start_date":"2015-12-30","offer_of_the_day":"1"}},{"id":"246","offer_id":"10","order_id":"283","offer":{"id":"10","offer_name":"Juice","description":"get a glass of juice with your breakfast","qty":"1","price":"20","menu_id":"9","status":"0","start_date":"2016-01-03","offer_of_the_day":"0"}}]}
     */

    private String status;
    /**
     * order : {"id":"283","user_id":"54","sub_total":"300","discount":"0","vat":"37.5","surcharge":"1.875","total_amount":"300","comments":null,"ordered_at":"2016-01-03 22:52:49","bring_change":"0","orderAddresses":[{"id":"303","order_id":"283","address_id":"54"}],"orderCoupons":[],"orderStatuses":[{"id":"248","order_id":"283","order_status":"Pending","status_date":"2016-01-03 22:52:49"}],"orderTimeSlots":[{"id":"267","order_id":"283","slot_id":"1"}]}
     * menu_details : [{"id":"261","order_id":"283","menu_id":"8","qty":"2","amount":"150","menu":{"id":"8","kitchen":"4","item_name":"Zero oil egg sandwich","description":"Zero oil egg sandwich","category":"1","price":"75","max_order_limit":"6","img_url":"web/menu/1451470390Zero oil egg sandwich.png","status":"0","in_stock":"0","best_suited":"Best suited for gym "}},{"id":"262","order_id":"283","menu_id":"9","qty":"2","amount":"100","menu":{"id":"9","kitchen":"4","item_name":"Juice","description":"juice is good ","category":"0","price":"50","max_order_limit":"5","img_url":"web/menu/1451470423Juice.png","status":"0","in_stock":"0","best_suited":"for everyone"}}]
     * user : {"id":54,"user_email":"atit@atit","auth_password":"827ccb0eea8a706c4c34a16891f84e7b","full_name":"atit","mobile":"123456789","referal_code":"NULL","user_role":2,"status":0}
     * address : {"id":54,"user_id":54,"address_line_one":"address line 0","address_line_two":"003 orchid","address_line_three":"Sector 49","category":"HOME","cluster":null}
     * slot : {"id":1,"kitchen_id":4,"start_time":"17:00:00","end_time":"18:00:00","status":0,"max_order_limit":6,"present_orders":9,"slot_date":"2015-12-30"}
     * offers : [{"id":"245","offer_id":"7","order_id":"283","offer":{"id":"7","offer_name":"testing","description":"f;ljlkdsvnfljlsfdkvbsflkdgbslfkdgbdflkgbsfkj","qty":"2","price":"30","menu_id":"8","status":"0","start_date":"2015-12-30","offer_of_the_day":"1"}},{"id":"246","offer_id":"10","order_id":"283","offer":{"id":"10","offer_name":"Juice","description":"get a glass of juice with your breakfast","qty":"1","price":"20","menu_id":"9","status":"0","start_date":"2016-01-03","offer_of_the_day":"0"}}]
     */

    private DataEntity data;

    public static OrderSummary objectFromData(String str) {

        return new Gson().fromJson(str, OrderSummary.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * id : 283
         * user_id : 54
         * sub_total : 300
         * discount : 0
         * vat : 37.5
         * surcharge : 1.875
         * total_amount : 300
         * comments : null
         * ordered_at : 2016-01-03 22:52:49
         * bring_change : 0
         * orderAddresses : [{"id":"303","order_id":"283","address_id":"54"}]
         * orderCoupons : []
         * orderStatuses : [{"id":"248","order_id":"283","order_status":"Pending","status_date":"2016-01-03 22:52:49"}]
         * orderTimeSlots : [{"id":"267","order_id":"283","slot_id":"1"}]
         */

        private OrderEntity order;
        /**
         * id : 54
         * user_email : atit@atit
         * auth_password : 827ccb0eea8a706c4c34a16891f84e7b
         * full_name : atit
         * mobile : 123456789
         * referal_code : NULL
         * user_role : 2
         * status : 0
         */

        private UserEntity user;
        /**
         * id : 54
         * user_id : 54
         * address_line_one : address line 0
         * address_line_two : 003 orchid
         * address_line_three : Sector 49
         * category : HOME
         * cluster : null
         */

        private AddressEntity address;
        /**
         * id : 1
         * kitchen_id : 4
         * start_time : 17:00:00
         * end_time : 18:00:00
         * status : 0
         * max_order_limit : 6
         * present_orders : 9
         * slot_date : 2015-12-30
         */

        private SlotEntity slot;
        /**
         * id : 261
         * order_id : 283
         * menu_id : 8
         * qty : 2
         * amount : 150
         * menu : {"id":"8","kitchen":"4","item_name":"Zero oil egg sandwich","description":"Zero oil egg sandwich","category":"1","price":"75","max_order_limit":"6","img_url":"web/menu/1451470390Zero oil egg sandwich.png","status":"0","in_stock":"0","best_suited":"Best suited for gym "}
         */

        private List<MenuDetailsEntity> menu_details;
        /**
         * id : 245
         * offer_id : 7
         * order_id : 283
         * offer : {"id":"7","offer_name":"testing","description":"f;ljlkdsvnfljlsfdkvbsflkdgbslfkdgbdflkgbsfkj","qty":"2","price":"30","menu_id":"8","status":"0","start_date":"2015-12-30","offer_of_the_day":"1"}
         */

        private List<OffersEntity> offers;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setOrder(OrderEntity order) {
            this.order = order;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setAddress(AddressEntity address) {
            this.address = address;
        }

        public void setSlot(SlotEntity slot) {
            this.slot = slot;
        }

        public void setMenu_details(List<MenuDetailsEntity> menu_details) {
            this.menu_details = menu_details;
        }

        public void setOffers(List<OffersEntity> offers) {
            this.offers = offers;
        }

        public OrderEntity getOrder() {
            return order;
        }

        public UserEntity getUser() {
            return user;
        }

        public AddressEntity getAddress() {
            return address;
        }

        public SlotEntity getSlot() {
            return slot;
        }

        public List<MenuDetailsEntity> getMenu_details() {
            return menu_details;
        }

        public List<OffersEntity> getOffers() {
            return offers;
        }

        public static class OrderEntity {
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
             * id : 303
             * order_id : 283
             * address_id : 54
             */

            private List<OrderAddressesEntity> orderAddresses;
            private List<?> orderCoupons;
            /**
             * id : 248
             * order_id : 283
             * order_status : Pending
             * status_date : 2016-01-03 22:52:49
             */

            private List<OrderStatusesEntity> orderStatuses;
            /**
             * id : 267
             * order_id : 283
             * slot_id : 1
             */

            private List<OrderTimeSlotsEntity> orderTimeSlots;

            public static OrderEntity objectFromData(String str) {

                return new Gson().fromJson(str, OrderEntity.class);
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

            public void setOrderAddresses(List<OrderAddressesEntity> orderAddresses) {
                this.orderAddresses = orderAddresses;
            }

            public void setOrderCoupons(List<?> orderCoupons) {
                this.orderCoupons = orderCoupons;
            }

            public void setOrderStatuses(List<OrderStatusesEntity> orderStatuses) {
                this.orderStatuses = orderStatuses;
            }

            public void setOrderTimeSlots(List<OrderTimeSlotsEntity> orderTimeSlots) {
                this.orderTimeSlots = orderTimeSlots;
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

            public List<OrderAddressesEntity> getOrderAddresses() {
                return orderAddresses;
            }

            public List<?> getOrderCoupons() {
                return orderCoupons;
            }

            public List<OrderStatusesEntity> getOrderStatuses() {
                return orderStatuses;
            }

            public List<OrderTimeSlotsEntity> getOrderTimeSlots() {
                return orderTimeSlots;
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

            public static class OrderTimeSlotsEntity {
                private String id;
                private String order_id;
                private String slot_id;

                public static OrderTimeSlotsEntity objectFromData(String str) {

                    return new Gson().fromJson(str, OrderTimeSlotsEntity.class);
                }

                public void setId(String id) {
                    this.id = id;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public void setSlot_id(String slot_id) {
                    this.slot_id = slot_id;
                }

                public String getId() {
                    return id;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public String getSlot_id() {
                    return slot_id;
                }
            }
        }

        public static class UserEntity {
            private int id;
            private String user_email;
            private String auth_password;
            private String full_name;
            private String mobile;
            private String referal_code;
            private int user_role;
            private int status;

            public static UserEntity objectFromData(String str) {

                return new Gson().fromJson(str, UserEntity.class);
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setUser_email(String user_email) {
                this.user_email = user_email;
            }

            public void setAuth_password(String auth_password) {
                this.auth_password = auth_password;
            }

            public void setFull_name(String full_name) {
                this.full_name = full_name;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public void setReferal_code(String referal_code) {
                this.referal_code = referal_code;
            }

            public void setUser_role(int user_role) {
                this.user_role = user_role;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public String getUser_email() {
                return user_email;
            }

            public String getAuth_password() {
                return auth_password;
            }

            public String getFull_name() {
                return full_name;
            }

            public String getMobile() {
                return mobile;
            }

            public String getReferal_code() {
                return referal_code;
            }

            public int getUser_role() {
                return user_role;
            }

            public int getStatus() {
                return status;
            }
        }

        public static class AddressEntity {
            private int id;
            private int user_id;
            private String address_line_one;
            private String address_line_two;
            private String address_line_three;
            private String category;
            private Object cluster;

            public static AddressEntity objectFromData(String str) {

                return new Gson().fromJson(str, AddressEntity.class);
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

        public static class SlotEntity {
            private int id;
            private int kitchen_id;
            private String start_time;
            private String end_time;
            private int status;
            private int max_order_limit;
            private int present_orders;
            private String slot_date;

            public static SlotEntity objectFromData(String str) {

                return new Gson().fromJson(str, SlotEntity.class);
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setKitchen_id(int kitchen_id) {
                this.kitchen_id = kitchen_id;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setMax_order_limit(int max_order_limit) {
                this.max_order_limit = max_order_limit;
            }

            public void setPresent_orders(int present_orders) {
                this.present_orders = present_orders;
            }

            public void setSlot_date(String slot_date) {
                this.slot_date = slot_date;
            }

            public int getId() {
                return id;
            }

            public int getKitchen_id() {
                return kitchen_id;
            }

            public String getStart_time() {
                return start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public int getStatus() {
                return status;
            }

            public int getMax_order_limit() {
                return max_order_limit;
            }

            public int getPresent_orders() {
                return present_orders;
            }

            public String getSlot_date() {
                return slot_date;
            }
        }

        public static class MenuDetailsEntity {
            private String id;
            private String order_id;
            private String menu_id;
            private String qty;
            private String amount;
            /**
             * id : 8
             * kitchen : 4
             * item_name : Zero oil egg sandwich
             * description : Zero oil egg sandwich
             * category : 1
             * price : 75
             * max_order_limit : 6
             * img_url : web/menu/1451470390Zero oil egg sandwich.png
             * status : 0
             * in_stock : 0
             * best_suited : Best suited for gym
             */

            private MenuEntity menu;

            public static MenuDetailsEntity objectFromData(String str) {

                return new Gson().fromJson(str, MenuDetailsEntity.class);
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

            public void setMenu(MenuEntity menu) {
                this.menu = menu;
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

            public MenuEntity getMenu() {
                return menu;
            }

            public static class MenuEntity {
                private String id;
                private String kitchen;
                private String item_name;
                private String description;
                private String category;
                private String price;
                private String max_order_limit;
                private String img_url;
                private String status;
                private String in_stock;
                private String best_suited;

                public static MenuEntity objectFromData(String str) {

                    return new Gson().fromJson(str, MenuEntity.class);
                }

                public void setId(String id) {
                    this.id = id;
                }

                public void setKitchen(String kitchen) {
                    this.kitchen = kitchen;
                }

                public void setItem_name(String item_name) {
                    this.item_name = item_name;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public void setMax_order_limit(String max_order_limit) {
                    this.max_order_limit = max_order_limit;
                }

                public void setImg_url(String img_url) {
                    this.img_url = img_url;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public void setIn_stock(String in_stock) {
                    this.in_stock = in_stock;
                }

                public void setBest_suited(String best_suited) {
                    this.best_suited = best_suited;
                }

                public String getId() {
                    return id;
                }

                public String getKitchen() {
                    return kitchen;
                }

                public String getItem_name() {
                    return item_name;
                }

                public String getDescription() {
                    return description;
                }

                public String getCategory() {
                    return category;
                }

                public String getPrice() {
                    return price;
                }

                public String getMax_order_limit() {
                    return max_order_limit;
                }

                public String getImg_url() {
                    return img_url;
                }

                public String getStatus() {
                    return status;
                }

                public String getIn_stock() {
                    return in_stock;
                }

                public String getBest_suited() {
                    return best_suited;
                }
            }
        }

        public static class OffersEntity {
            private String id;
            private String offer_id;
            private String order_id;
            /**
             * id : 7
             * offer_name : testing
             * description : f;ljlkdsvnfljlsfdkvbsflkdgbslfkdgbdflkgbsfkj
             * qty : 2
             * price : 30
             * menu_id : 8
             * status : 0
             * start_date : 2015-12-30
             * offer_of_the_day : 1
             */

            private OfferEntity offer;

            public static OffersEntity objectFromData(String str) {

                return new Gson().fromJson(str, OffersEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setOffer_id(String offer_id) {
                this.offer_id = offer_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public void setOffer(OfferEntity offer) {
                this.offer = offer;
            }

            public String getId() {
                return id;
            }

            public String getOffer_id() {
                return offer_id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public OfferEntity getOffer() {
                return offer;
            }

            public static class OfferEntity {
                private String id;
                private String offer_name;
                private String description;
                private String qty;
                private String price;
                private String menu_id;
                private String status;
                private String start_date;
                private String offer_of_the_day;

                public static OfferEntity objectFromData(String str) {

                    return new Gson().fromJson(str, OfferEntity.class);
                }

                public void setId(String id) {
                    this.id = id;
                }

                public void setOffer_name(String offer_name) {
                    this.offer_name = offer_name;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public void setQty(String qty) {
                    this.qty = qty;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public void setMenu_id(String menu_id) {
                    this.menu_id = menu_id;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public void setStart_date(String start_date) {
                    this.start_date = start_date;
                }

                public void setOffer_of_the_day(String offer_of_the_day) {
                    this.offer_of_the_day = offer_of_the_day;
                }

                public String getId() {
                    return id;
                }

                public String getOffer_name() {
                    return offer_name;
                }

                public String getDescription() {
                    return description;
                }

                public String getQty() {
                    return qty;
                }

                public String getPrice() {
                    return price;
                }

                public String getMenu_id() {
                    return menu_id;
                }

                public String getStatus() {
                    return status;
                }

                public String getStart_date() {
                    return start_date;
                }

                public String getOffer_of_the_day() {
                    return offer_of_the_day;
                }
            }
        }
    }
}

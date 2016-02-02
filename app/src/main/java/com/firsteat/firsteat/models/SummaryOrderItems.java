package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/24/2015.
 */
public class SummaryOrderItems {


    /**
     * status : 0
     * data : {"order":"successfull","details":[{"id":"152","order_id":"181","menu_id":"1","qty":"3","amount":"195","menu":{"id":"1","kitchen":"1","item_name":"Eggs","description":"4 Boiled Eggs","category":"1","price":"65","max_order_limit":"1","img_url":"web/menu/1450377251Eggs.jpg","status":"0","in_stock":"0","best_suited":"People who need good Protein"}}],"offers":[],"del_address":[{"id":"183","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"184","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"185","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"186","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"187","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"188","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"190","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"194","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"195","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"196","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}}]}
     */

    private String status;
    /**
     * order : successfull
     * details : [{"id":"152","order_id":"181","menu_id":"1","qty":"3","amount":"195","menu":{"id":"1","kitchen":"1","item_name":"Eggs","description":"4 Boiled Eggs","category":"1","price":"65","max_order_limit":"1","img_url":"web/menu/1450377251Eggs.jpg","status":"0","in_stock":"0","best_suited":"People who need good Protein"}}]
     * offers : []
     * del_address : [{"id":"183","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"184","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"185","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"186","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"187","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"188","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"190","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"194","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"195","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}},{"id":"196","order_id":"181","address_id":"35","address":{"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}}]
     */

    private DataEntity data;

    public static SummaryOrderItems objectFromData(String str) {

        return new Gson().fromJson(str, SummaryOrderItems.class);
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
        private String order;
        /**
         * id : 152
         * order_id : 181
         * menu_id : 1
         * qty : 3
         * amount : 195
         * menu : {"id":"1","kitchen":"1","item_name":"Eggs","description":"4 Boiled Eggs","category":"1","price":"65","max_order_limit":"1","img_url":"web/menu/1450377251Eggs.jpg","status":"0","in_stock":"0","best_suited":"People who need good Protein"}
         */

        private List<DetailsEntity> details;
        private List<?> offers;
        /**
         * id : 183
         * order_id : 181
         * address_id : 35
         * address : {"id":"35","user_id":"28","address_line_one":"kuch bhi123","address_line_two":"16","address_line_three":"Sushant Lok I","category":"HOME","cluster":null}
         */

        private List<DelAddressEntity> del_address;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public void setDetails(List<DetailsEntity> details) {
            this.details = details;
        }

        public void setOffers(List<?> offers) {
            this.offers = offers;
        }

        public void setDel_address(List<DelAddressEntity> del_address) {
            this.del_address = del_address;
        }

        public String getOrder() {
            return order;
        }

        public List<DetailsEntity> getDetails() {
            return details;
        }

        public List<?> getOffers() {
            return offers;
        }

        public List<DelAddressEntity> getDel_address() {
            return del_address;
        }

        public static class DetailsEntity {
            private String id;
            private String order_id;
            private String menu_id;
            private String qty;
            private String amount;
            /**
             * id : 1
             * kitchen : 1
             * item_name : Eggs
             * description : 4 Boiled Eggs
             * category : 1
             * price : 65
             * max_order_limit : 1
             * img_url : web/menu/1450377251Eggs.jpg
             * status : 0
             * in_stock : 0
             * best_suited : People who need good Protein
             */

            private MenuEntity menu;

            public static DetailsEntity objectFromData(String str) {

                return new Gson().fromJson(str, DetailsEntity.class);
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

        public static class DelAddressEntity {
            private String id;
            private String order_id;
            private String address_id;
            /**
             * id : 35
             * user_id : 28
             * address_line_one : kuch bhi123
             * address_line_two : 16
             * address_line_three : Sushant Lok I
             * category : HOME
             * cluster : null
             */

            private AddressEntity address;

            public static DelAddressEntity objectFromData(String str) {

                return new Gson().fromJson(str, DelAddressEntity.class);
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

            public void setAddress(AddressEntity address) {
                this.address = address;
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

            public AddressEntity getAddress() {
                return address;
            }

            public static class AddressEntity {
                private String id;
                private String user_id;
                private String address_line_one;
                private String address_line_two;
                private String address_line_three;
                private String category;
                private Object cluster;

                public static AddressEntity objectFromData(String str) {

                    return new Gson().fromJson(str, AddressEntity.class);
                }

                public void setId(String id) {
                    this.id = id;
                }

                public void setUser_id(String user_id) {
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

                public String getId() {
                    return id;
                }

                public String getUser_id() {
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
    }
}

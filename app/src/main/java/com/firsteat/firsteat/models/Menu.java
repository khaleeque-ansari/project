package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class Menu {


    /**
     * status : 0
     * token : 9f61408e3afb633e50cdf1b20de6f466
     * data : [{"id":8,"name":"Zero oil egg sandwich","location_id":492,"description":"Zero oil egg sandwich","category":"1","price":75,"max_order_limit":6,"img_url":"http://eatfirst.touchmagics.com/web/menu/1451470390Zero%20oil%20egg%20sandwich.png","in_stock":"Yes","active":"Yes","best_suited":"Best suited for gym ","food_details":{"proteins":150,"carbohydrates":300,"fats":125,"fibre":200,"calories":150,"offers":[{"id":15,"offer_name":"Offer with Sandwich","description":"desc","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":1},{"id":16,"offer_name":"Add Juice for 50","description":"test","qty":1,"price":50,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":0},{"id":17,"offer_name":"Offer 2 with sand","description":"test","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":1},{"id":20,"offer_name":"new","description":"new","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-09","offer_of_the_day":1},{"id":21,"offer_name":"Offer of the day ","description":"check","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-09","offer_of_the_day":0}],"stock":{"id":68,"menu_id":8,"max_order_limit":10,"present_orders":10,"menu_date":"2016-01-09"}}},{"id":9,"name":"Juice","location_id":492,"description":"juice is good juice is good juice is good juice is good juice is good juice is good juice is good juice is good juice is good juice is good juice is good ","category":"0","price":50,"max_order_limit":5,"img_url":"http://eatfirst.touchmagics.com/web/menu/1451470423Juice.png","in_stock":"Yes","active":"Yes","best_suited":"Best suited for everyone Best suited for everyone Best suited for everyone Best suited for everyone Best suited for everyone Best suited for everyoneBest suited for everyone","food_details":{"proteins":127,"carbohydrates":158,"fats":184,"fibre":652,"calories":132,"offers":[],"stock":{"id":66,"menu_id":9,"max_order_limit":90,"present_orders":0,"menu_date":"2016-01-09"}}},{"id":12,"name":"Omlete","location_id":492,"description":"omelette  ","category":"1","price":200,"max_order_limit":5,"img_url":"http://eatfirst.touchmagics.com/web/menu/1452193632Omlete.jpg","in_stock":"Yes","active":"Yes","best_suited":"This is the best dish ","food_details":{"proteins":10,"carbohydrates":10,"fats":10,"fibre":10,"calories":10,"offers":[{"id":18,"offer_name":"Morning Soother","description":"test","qty":1,"price":29,"menu_id":12,"status":0,"start_date":"2016-01-08","offer_of_the_day":0}],"stock":{"id":67,"menu_id":12,"max_order_limit":10,"present_orders":0,"menu_date":"2016-01-09"}}}]
     * offer_of_the_day : {"id":21,"offer_name":"Offer of the day ","description":"check","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-09","offer_of_the_day":0}
     * num_points : 1
     * point_value : 1
     */

    private String status;
    private String token;
    /**
     * id : 21
     * offer_name : Offer of the day
     * description : check
     * qty : 1
     * price : 10
     * menu_id : 8
     * status : 0
     * start_date : 2016-01-09
     * offer_of_the_day : 0
     */

    private OfferOfTheDayEntity offer_of_the_day;
    private int num_points;
    private int point_value;
    /**
     * id : 8
     * name : Zero oil egg sandwich
     * location_id : 492
     * description : Zero oil egg sandwich
     * category : 1
     * price : 75
     * max_order_limit : 6
     * img_url : http://eatfirst.touchmagics.com/web/menu/1451470390Zero%20oil%20egg%20sandwich.png
     * in_stock : Yes
     * active : Yes
     * best_suited : Best suited for gym
     * food_details : {"proteins":150,"carbohydrates":300,"fats":125,"fibre":200,"calories":150,"offers":[{"id":15,"offer_name":"Offer with Sandwich","description":"desc","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":1},{"id":16,"offer_name":"Add Juice for 50","description":"test","qty":1,"price":50,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":0},{"id":17,"offer_name":"Offer 2 with sand","description":"test","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":1},{"id":20,"offer_name":"new","description":"new","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-09","offer_of_the_day":1},{"id":21,"offer_name":"Offer of the day ","description":"check","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-09","offer_of_the_day":0}],"stock":{"id":68,"menu_id":8,"max_order_limit":10,"present_orders":10,"menu_date":"2016-01-09"}}
     */

    private List<DataEntity> data;

    public static Menu objectFromData(String str) {

        return new Gson().fromJson(str, Menu.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOffer_of_the_day(OfferOfTheDayEntity offer_of_the_day) {
        this.offer_of_the_day = offer_of_the_day;
    }

    public void setNum_points(int num_points) {
        this.num_points = num_points;
    }

    public void setPoint_value(int point_value) {
        this.point_value = point_value;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public OfferOfTheDayEntity getOffer_of_the_day() {
        return offer_of_the_day;
    }

    public int getNum_points() {
        return num_points;
    }

    public int getPoint_value() {
        return point_value;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class OfferOfTheDayEntity {
        private int id;
        private String offer_name;
        private String description;
        private int qty;
        private int price;
        private int menu_id;
        private int status;
        private String start_date;
        private int offer_of_the_day;

        public static OfferOfTheDayEntity objectFromData(String str) {

            return new Gson().fromJson(str, OfferOfTheDayEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setOffer_name(String offer_name) {
            this.offer_name = offer_name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setMenu_id(int menu_id) {
            this.menu_id = menu_id;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public void setOffer_of_the_day(int offer_of_the_day) {
            this.offer_of_the_day = offer_of_the_day;
        }

        public int getId() {
            return id;
        }

        public String getOffer_name() {
            return offer_name;
        }

        public String getDescription() {
            return description;
        }

        public int getQty() {
            return qty;
        }

        public int getPrice() {
            return price;
        }

        public int getMenu_id() {
            return menu_id;
        }

        public int getStatus() {
            return status;
        }

        public String getStart_date() {
            return start_date;
        }

        public int getOffer_of_the_day() {
            return offer_of_the_day;
        }
    }

    public static class DataEntity {
        private int id;
        private String name;
        private int location_id;
        private String description;
        private String category;
        private int price;
        private int max_order_limit;
        private String img_url;
        private String in_stock;
        private String active;
        private String best_suited;
        /**
         * proteins : 150
         * carbohydrates : 300
         * fats : 125
         * fibre : 200
         * calories : 150
         * offers : [{"id":15,"offer_name":"Offer with Sandwich","description":"desc","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":1},{"id":16,"offer_name":"Add Juice for 50","description":"test","qty":1,"price":50,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":0},{"id":17,"offer_name":"Offer 2 with sand","description":"test","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-06","offer_of_the_day":1},{"id":20,"offer_name":"new","description":"new","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-09","offer_of_the_day":1},{"id":21,"offer_name":"Offer of the day ","description":"check","qty":1,"price":10,"menu_id":8,"status":0,"start_date":"2016-01-09","offer_of_the_day":0}]
         * stock : {"id":68,"menu_id":8,"max_order_limit":10,"present_orders":10,"menu_date":"2016-01-09"}
         */

        private FoodDetailsEntity food_details;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLocation_id(int location_id) {
            this.location_id = location_id;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setMax_order_limit(int max_order_limit) {
            this.max_order_limit = max_order_limit;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public void setIn_stock(String in_stock) {
            this.in_stock = in_stock;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public void setBest_suited(String best_suited) {
            this.best_suited = best_suited;
        }

        public void setFood_details(FoodDetailsEntity food_details) {
            this.food_details = food_details;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getLocation_id() {
            return location_id;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        public int getPrice() {
            return price;
        }

        public int getMax_order_limit() {
            return max_order_limit;
        }

        public String getImg_url() {
            return img_url;
        }

        public String getIn_stock() {
            return in_stock;
        }

        public String getActive() {
            return active;
        }

        public String getBest_suited() {
            return best_suited;
        }

        public FoodDetailsEntity getFood_details() {
            return food_details;
        }

        public static class FoodDetailsEntity {
            private int proteins;
            private int carbohydrates;
            private int fats;
            private int fibre;
            private int calories;
            /**
             * id : 68
             * menu_id : 8
             * max_order_limit : 10
             * present_orders : 10
             * menu_date : 2016-01-09
             */

            private StockEntity stock;
            /**
             * id : 15
             * offer_name : Offer with Sandwich
             * description : desc
             * qty : 1
             * price : 10
             * menu_id : 8
             * status : 0
             * start_date : 2016-01-06
             * offer_of_the_day : 1
             */

            private List<OffersEntity> offers;

            public static FoodDetailsEntity objectFromData(String str) {

                return new Gson().fromJson(str, FoodDetailsEntity.class);
            }

            public void setProteins(int proteins) {
                this.proteins = proteins;
            }

            public void setCarbohydrates(int carbohydrates) {
                this.carbohydrates = carbohydrates;
            }

            public void setFats(int fats) {
                this.fats = fats;
            }

            public void setFibre(int fibre) {
                this.fibre = fibre;
            }

            public void setCalories(int calories) {
                this.calories = calories;
            }

            public void setStock(StockEntity stock) {
                this.stock = stock;
            }

            public void setOffers(List<OffersEntity> offers) {
                this.offers = offers;
            }

            public int getProteins() {
                return proteins;
            }

            public int getCarbohydrates() {
                return carbohydrates;
            }

            public int getFats() {
                return fats;
            }

            public int getFibre() {
                return fibre;
            }

            public int getCalories() {
                return calories;
            }

            public StockEntity getStock() {
                return stock;
            }

            public List<OffersEntity> getOffers() {
                return offers;
            }

            public static class StockEntity {
                private int id;
                private int menu_id;
                private int max_order_limit;
                private int present_orders;
                private String menu_date;

                public static StockEntity objectFromData(String str) {

                    return new Gson().fromJson(str, StockEntity.class);
                }

                public void setId(int id) {
                    this.id = id;
                }

                public void setMenu_id(int menu_id) {
                    this.menu_id = menu_id;
                }

                public void setMax_order_limit(int max_order_limit) {
                    this.max_order_limit = max_order_limit;
                }

                public void setPresent_orders(int present_orders) {
                    this.present_orders = present_orders;
                }

                public void setMenu_date(String menu_date) {
                    this.menu_date = menu_date;
                }

                public int getId() {
                    return id;
                }

                public int getMenu_id() {
                    return menu_id;
                }

                public int getMax_order_limit() {
                    return max_order_limit;
                }

                public int getPresent_orders() {
                    return present_orders;
                }

                public String getMenu_date() {
                    return menu_date;
                }
            }

            public static class OffersEntity {
                private int id;
                private String offer_name;
                private String description;
                private int qty;
                private int price;
                private int menu_id;
                private int status;
                private String start_date;
                private int offer_of_the_day;

                public static OffersEntity objectFromData(String str) {

                    return new Gson().fromJson(str, OffersEntity.class);
                }

                public void setId(int id) {
                    this.id = id;
                }

                public void setOffer_name(String offer_name) {
                    this.offer_name = offer_name;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public void setQty(int qty) {
                    this.qty = qty;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public void setMenu_id(int menu_id) {
                    this.menu_id = menu_id;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public void setStart_date(String start_date) {
                    this.start_date = start_date;
                }

                public void setOffer_of_the_day(int offer_of_the_day) {
                    this.offer_of_the_day = offer_of_the_day;
                }

                public int getId() {
                    return id;
                }

                public String getOffer_name() {
                    return offer_name;
                }

                public String getDescription() {
                    return description;
                }

                public int getQty() {
                    return qty;
                }

                public int getPrice() {
                    return price;
                }

                public int getMenu_id() {
                    return menu_id;
                }

                public int getStatus() {
                    return status;
                }

                public String getStart_date() {
                    return start_date;
                }

                public int getOffer_of_the_day() {
                    return offer_of_the_day;
                }
            }
        }
    }
}

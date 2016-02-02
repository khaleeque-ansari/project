package com.firsteat.firsteat.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by touchmagics on 12/19/2015.
 */
public class CouponJson {


    /**
     * status : 0
     * data : {"id":10,"category":"1","coupon_name":"TEAM250","discount_type":"AMOUNT","discount_value":"50.00","max_discount_amount":250,"start_date":"2016-01-03","end_date":"2016-02-29","max_redeem":500}
     * final : 1
     */

    private String status;
    /**
     * id : 10
     * category : 1
     * coupon_name : TEAM250
     * discount_type : AMOUNT
     * discount_value : 50.00
     * max_discount_amount : 250
     * start_date : 2016-01-03
     * end_date : 2016-02-29
     * max_redeem : 500
     */

    private DataEntity data;
    @SerializedName("final")
    private int finalX;

    public static CouponJson objectFromData(String str) {

        return new Gson().fromJson(str, CouponJson.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setFinalX(int finalX) {
        this.finalX = finalX;
    }

    public String getStatus() {
        return status;
    }

    public DataEntity getData() {
        return data;
    }

    public int getFinalX() {
        return finalX;
    }

    public static class DataEntity {
        private int id;
        private String category;
        private String coupon_name;
        private String discount_type;
        private String discount_value;
        private int max_discount_amount;
        private String start_date;
        private String end_date;
        private int max_redeem;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public void setDiscount_type(String discount_type) {
            this.discount_type = discount_type;
        }

        public void setDiscount_value(String discount_value) {
            this.discount_value = discount_value;
        }

        public void setMax_discount_amount(int max_discount_amount) {
            this.max_discount_amount = max_discount_amount;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public void setMax_redeem(int max_redeem) {
            this.max_redeem = max_redeem;
        }

        public int getId() {
            return id;
        }

        public String getCategory() {
            return category;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public String getDiscount_type() {
            return discount_type;
        }

        public String getDiscount_value() {
            return discount_value;
        }

        public int getMax_discount_amount() {
            return max_discount_amount;
        }

        public String getStart_date() {
            return start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public int getMax_redeem() {
            return max_redeem;
        }
    }
}

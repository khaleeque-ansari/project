package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/28/2015.
 */
public class Kitchens {

    /**
     * status : 0
     * data : [{"id":1,"name":"Chef Kitchen","description":"Desc addint","address":"abc e","mobile":"9464360600","landline":"","active":"Yes","radius":6,"latitude":"28.491681","longitude":"77.094897","location":"DLF Phase 3, Gurgaon","cluster":"A"},{"id":2,"name":"Huda","description":"Huda Kitchen","address":"abcd","mobile":"9999999999","landline":"","active":"Yes","radius":9,"latitude":"28.7","longitude":"70.8","location":"Huda","cluster":"A"}]
     */

    private String status;
    /**
     * id : 1
     * name : Chef Kitchen
     * description : Desc addint
     * address : abc e
     * mobile : 9464360600
     * landline :
     * active : Yes
     * radius : 6
     * latitude : 28.491681
     * longitude : 77.094897
     * location : DLF Phase 3, Gurgaon
     * cluster : A
     */

    private List<DataEntity> data;

    public static Kitchens objectFromData(String str) {

        return new Gson().fromJson(str, Kitchens.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private int id;
        private String name;
        private String description;
        private String address;
        private String mobile;
        private String landline;
        private String active;
        private int radius;
        private String latitude;
        private String longitude;
        private String location;
        private String cluster;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setLandline(String landline) {
            this.landline = landline;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setCluster(String cluster) {
            this.cluster = cluster;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getAddress() {
            return address;
        }

        public String getMobile() {
            return mobile;
        }

        public String getLandline() {
            return landline;
        }

        public String getActive() {
            return active;
        }

        public int getRadius() {
            return radius;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLocation() {
            return location;
        }

        public String getCluster() {
            return cluster;
        }
    }
}

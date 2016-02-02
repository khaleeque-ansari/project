package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/18/2015.
 */
public class DeliveryLocations {


    /**
     * status : 0
     * data : [{"id":1,"location_name":"DLF Phase 3, Gurgaon","latitude":"28.491681","longitude":"77.094897","status":0},{"id":2,"location_name":"DLF Phase 2, Gurgaon","latitude":"28.487667","longitude":"77.088103","status":0},{"id":3,"location_name":"Cyber City, Gurgaon","latitude":"28.493602","longitude":"77.088325","status":0},{"id":4,"location_name":"Sushant Lok I","latitude":"28.463441","longitude":"77.076756","status":0},{"id":5,"location_name":"test","latitude":"65.967653345","longitude":"65.865343456","status":0},{"id":6,"location_name":"Test Location","latitude":"12.45","longitude":"88.8","status":0}]
     */

    private String status;
    /**
     * id : 1
     * location_name : DLF Phase 3, Gurgaon
     * latitude : 28.491681
     * longitude : 77.094897
     * status : 0
     */

    private List<DataEntity> data;

    public static DeliveryLocations objectFromData(String str) {

        return new Gson().fromJson(str, DeliveryLocations.class);
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
        private String location_name;
        private String latitude;
        private String longitude;
        private int status;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setLocation_name(String location_name) {
            this.location_name = location_name;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getLocation_name() {
            return location_name;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public int getStatus() {
            return status;
        }
    }
}

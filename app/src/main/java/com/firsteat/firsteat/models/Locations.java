package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/3/2015.
 */
public class Locations {


    /**
     * status : 0
     * token : 182be0c5cdcd5072bb1864cdee4d3d6e
     * data : [{"id":1,"location":"DLF Phase 3, Gurgaon","latitude":"28.491681","longitude":"77.094897","active":"Yes"},{"id":2,"location":"DLF Phase 2, Gurgaon","latitude":"28.487667","longitude":"77.088103","active":"Yes"},{"id":489,"location":"test","latitude":"98","longitude":"677","active":"Yes"}]
     */

    private String status;
    private String token;
    /**
     * id : 1
     * location : DLF Phase 3, Gurgaon
     * latitude : 28.491681
     * longitude : 77.094897
     * active : Yes
     */

    private List<DataEntity> data;

    public static Locations objectFromData(String str) {

        return new Gson().fromJson(str, Locations.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
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

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private int id;
        private String location;
        private String latitude;
        private String longitude;
        private String active;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public int getId() {
            return id;
        }

        public String getLocation() {
            return location;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getActive() {
            return active;
        }
    }
}

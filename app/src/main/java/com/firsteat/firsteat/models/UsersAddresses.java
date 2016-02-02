package com.firsteat.firsteat.models;

/**
 * Created by touchmagics on 12/10/2015.
 */
public class UsersAddresses {

    int adress_id;

    String add_0;
    String add_1;
    String add_2;
    String category;

    public int getAdress_id() {
        return adress_id;
    }

    public void setAdress_id(int adress_id) {
        this.adress_id = adress_id;
    }

    public String getAdd_0() {
        return add_0;
    }

    public void setAdd_0(String add_0) {
        this.add_0 = add_0;
    }

    public String getAdd_1() {
        return add_1;
    }

    public void setAdd_1(String add_1) {
        this.add_1 = add_1;
    }

    public String getAdd_2() {
        return add_2;
    }

    public void setAdd_2(String add_2) {
        this.add_2 = add_2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    String cluster;
}

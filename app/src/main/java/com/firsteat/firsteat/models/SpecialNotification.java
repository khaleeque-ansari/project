package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/19/2015.
 */
public class SpecialNotification {

    /**
     * status : 0
     * data : [{"id":6,"title_message":"First Eat","content_text":"mannayugasdhgs asdlkjasldja laksdjlkjas l;ajdkjlasd lkajldkjalsd lkjadkljasd kjsdfljlasf lkjad lkjadjlasd ljladj","sub_text":"lljasdjlasj","summary":"lkjlkajsdj","img_url":null,"notify_date":"2015-12-18 18:55:05","type":1},{"id":5,"title_message":"test","content_text":"a","sub_text":"b","summary":"c","img_url":null,"notify_date":"2015-12-18 18:53:36","type":1}]
     * count : 2
     */

    private String status;
    private String count;
    /**
     * id : 6
     * title_message : First Eat
     * content_text : mannayugasdhgs asdlkjasldja laksdjlkjas l;ajdkjlasd lkajldkjalsd lkjadkljasd kjsdfljlasf lkjad lkjadjlasd ljladj
     * sub_text : lljasdjlasj
     * summary : lkjlkajsdj
     * img_url : null
     * notify_date : 2015-12-18 18:55:05
     * type : 1
     */

    private List<DataEntity> data;

    public static SpecialNotification objectFromData(String str) {

        return new Gson().fromJson(str, SpecialNotification.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getCount() {
        return count;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private int id;
        private String title_message;
        private String content_text;
        private String sub_text;
        private String summary;
        private Object img_url;
        private String notify_date;
        private int type;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle_message(String title_message) {
            this.title_message = title_message;
        }

        public void setContent_text(String content_text) {
            this.content_text = content_text;
        }

        public void setSub_text(String sub_text) {
            this.sub_text = sub_text;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setImg_url(Object img_url) {
            this.img_url = img_url;
        }

        public void setNotify_date(String notify_date) {
            this.notify_date = notify_date;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public String getTitle_message() {
            return title_message;
        }

        public String getContent_text() {
            return content_text;
        }

        public String getSub_text() {
            return sub_text;
        }

        public String getSummary() {
            return summary;
        }

        public Object getImg_url() {
            return img_url;
        }

        public String getNotify_date() {
            return notify_date;
        }

        public int getType() {
            return type;
        }
    }
}

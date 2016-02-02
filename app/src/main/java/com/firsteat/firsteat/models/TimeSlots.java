package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/18/2015.
 */
public class TimeSlots {

    /**
     * status : 0
     * data : [{"id":136,"kitchen_id":7,"slot":"08:30-09:30","start_time":"08:30:00","end_time":"09:30:00","order_limit":10,"present_orders":5,"status":"Active"},{"id":137,"kitchen_id":7,"slot":"09:30-10:30","start_time":"09:30:00","end_time":"10:30:00","order_limit":10,"present_orders":0,"status":"Active"},{"id":138,"kitchen_id":7,"slot":"10:30-11:30","start_time":"10:30:00","end_time":"11:30:00","order_limit":10,"present_orders":1,"status":"Active"},{"id":139,"kitchen_id":7,"slot":"11:30-12:30","start_time":"11:30:00","end_time":"12:30:00","order_limit":10,"present_orders":0,"status":"Active"},{"id":140,"kitchen_id":7,"slot":"14:30-15:30","start_time":"14:30:00","end_time":"15:30:00","order_limit":10,"present_orders":0,"status":"Active"},{"id":141,"kitchen_id":7,"slot":"15:30-16:30","start_time":"15:30:00","end_time":"16:30:00","order_limit":10,"present_orders":0,"status":"Active"},{"id":142,"kitchen_id":7,"slot":"16:30-17:30","start_time":"16:30:00","end_time":"17:30:00","order_limit":10,"present_orders":0,"status":"Active"},{"id":143,"kitchen_id":7,"slot":"17:30-18:30","start_time":"17:30:00","end_time":"18:30:00","order_limit":10,"present_orders":2,"status":"Active"},{"id":146,"kitchen_id":7,"slot":"10:00-11:00","start_time":"10:00:00","end_time":"11:00:00","order_limit":9,"present_orders":0,"status":"Active"},{"id":147,"kitchen_id":7,"slot":"11:00-00:00","start_time":"11:00:00","end_time":"00:00:00","order_limit":9,"present_orders":0,"status":"Active"}]
     * now : 12:57:46
     * active_slot : {"id":140,"kitchen_id":7,"slot":"14:30-15:30","start_time":"14:30:00","end_time":"15:30:00","order_limit":10,"present_orders":0,"status":"Active"}
     */

    private String status;
    private String now;
    /**
     * id : 140
     * kitchen_id : 7
     * slot : 14:30-15:30
     * start_time : 14:30:00
     * end_time : 15:30:00
     * order_limit : 10
     * present_orders : 0
     * status : Active
     */

    private ActiveSlotEntity active_slot;
    /**
     * id : 136
     * kitchen_id : 7
     * slot : 08:30-09:30
     * start_time : 08:30:00
     * end_time : 09:30:00
     * order_limit : 10
     * present_orders : 5
     * status : Active
     */

    private List<DataEntity> data;

    public static TimeSlots objectFromData(String str) {

        return new Gson().fromJson(str, TimeSlots.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public void setActive_slot(ActiveSlotEntity active_slot) {
        this.active_slot = active_slot;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getNow() {
        return now;
    }

    public ActiveSlotEntity getActive_slot() {
        return active_slot;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class ActiveSlotEntity {
        private int id;
        private int kitchen_id;
        private String slot;
        private String start_time;
        private String end_time;
        private int order_limit;
        private int present_orders;
        private String status;

        public static ActiveSlotEntity objectFromData(String str) {

            return new Gson().fromJson(str, ActiveSlotEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setKitchen_id(int kitchen_id) {
            this.kitchen_id = kitchen_id;
        }

        public void setSlot(String slot) {
            this.slot = slot;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setOrder_limit(int order_limit) {
            this.order_limit = order_limit;
        }

        public void setPresent_orders(int present_orders) {
            this.present_orders = present_orders;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public int getKitchen_id() {
            return kitchen_id;
        }

        public String getSlot() {
            return slot;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public int getOrder_limit() {
            return order_limit;
        }

        public int getPresent_orders() {
            return present_orders;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class DataEntity {
        private int id;
        private int kitchen_id;
        private String slot;
        private String start_time;
        private String end_time;
        private int order_limit;
        private int present_orders;
        private String status;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setKitchen_id(int kitchen_id) {
            this.kitchen_id = kitchen_id;
        }

        public void setSlot(String slot) {
            this.slot = slot;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setOrder_limit(int order_limit) {
            this.order_limit = order_limit;
        }

        public void setPresent_orders(int present_orders) {
            this.present_orders = present_orders;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public int getKitchen_id() {
            return kitchen_id;
        }

        public String getSlot() {
            return slot;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public int getOrder_limit() {
            return order_limit;
        }

        public int getPresent_orders() {
            return present_orders;
        }

        public String getStatus() {
            return status;
        }
    }
}

package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/15/2015.
 */
public class DistenceMatrix  {

    /**
     * destination_addresses : ["U-46/43, Galli No 46, U Block, DLF Phase 3, Sector 24, Gurgaon, Haryana 122002, India","P1, DLF Phase 2, Sector 25, Gurgaon, Haryana 122002, India"]
     * origin_addresses : ["C-2267, Block C, Sushant Lok Phase I, Sector 43, Gurgaon, Haryana 122003, India"]
     * rows : [{"elements":[{"distance":{"text":"9.7 km","value":9719},"duration":{"text":"27 mins","value":1630},"status":"OK"},{"distance":{"text":"6.9 km","value":6874},"duration":{"text":"20 mins","value":1179},"status":"OK"}]}]
     * status : OK
     */

    private String status;
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<RowsEntity> rows;

    public static DistenceMatrix objectFromData(String str) {

        return new Gson().fromJson(str, DistenceMatrix.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity {
        /**
         * distance : {"text":"9.7 km","value":9719}
         * duration : {"text":"27 mins","value":1630}
         * status : OK
         */

        private List<ElementsEntity> elements;

        public static RowsEntity objectFromData(String str) {

            return new Gson().fromJson(str, RowsEntity.class);
        }

        public void setElements(List<ElementsEntity> elements) {
            this.elements = elements;
        }

        public List<ElementsEntity> getElements() {
            return elements;
        }

        public static class ElementsEntity {
            /**
             * text : 9.7 km
             * value : 9719
             */

            private DistanceEntity distance;
            /**
             * text : 27 mins
             * value : 1630
             */

            private DurationEntity duration;
            private String status;

            public static ElementsEntity objectFromData(String str) {

                return new Gson().fromJson(str, ElementsEntity.class);
            }

            public void setDistance(DistanceEntity distance) {
                this.distance = distance;
            }

            public void setDuration(DurationEntity duration) {
                this.duration = duration;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public DistanceEntity getDistance() {
                return distance;
            }

            public DurationEntity getDuration() {
                return duration;
            }

            public String getStatus() {
                return status;
            }

            public static class DistanceEntity {
                private String text;
                private int value;

                public static DistanceEntity objectFromData(String str) {

                    return new Gson().fromJson(str, DistanceEntity.class);
                }

                public void setText(String text) {
                    this.text = text;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }

            public static class DurationEntity {
                private String text;
                private int value;

                public static DurationEntity objectFromData(String str) {

                    return new Gson().fromJson(str, DurationEntity.class);
                }

                public void setText(String text) {
                    this.text = text;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public int getValue() {
                    return value;
                }
            }
        }
    }
}

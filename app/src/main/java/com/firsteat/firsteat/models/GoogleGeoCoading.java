package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/19/2015.
 */
public class GoogleGeoCoading {

    /**
     * results : [{"address_components":[{"long_name":"C-2267","short_name":"C-2267","types":["premise"]},{"long_name":"Block C","short_name":"Block C","types":["sublocality_level_3","sublocality","political"]},{"long_name":"Sushant Lok Phase I","short_name":"Sushant Lok Phase I","types":["sublocality_level_2","sublocality","political"]},{"long_name":"Sector 43","short_name":"Sector 43","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Gurgaon","short_name":"Gurgaon","types":["locality","political"]},{"long_name":"Gurgaon","short_name":"Gurgaon","types":["administrative_area_level_2","political"]},{"long_name":"Haryana","short_name":"HR","types":["administrative_area_level_1","political"]},{"long_name":"India","short_name":"IN","types":["country","political"]},{"long_name":"122003","short_name":"122003","types":["postal_code"]}],"formatted_address":"C-2267, Block C, Sushant Lok Phase I, Sector 43, Gurgaon, Haryana 122003, India","geometry":{"location":{"lat":28.4497225,"lng":77.0811627},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":28.45107148029151,"lng":77.08251168029149},"southwest":{"lat":28.4483735197085,"lng":77.07981371970848}}},"place_id":"ChIJGZyMdcAYDTkRSV_xNxolLxE","types":["street_address"]}]
     * status : OK
     */

    private String status;
    /**
     * address_components : [{"long_name":"C-2267","short_name":"C-2267","types":["premise"]},{"long_name":"Block C","short_name":"Block C","types":["sublocality_level_3","sublocality","political"]},{"long_name":"Sushant Lok Phase I","short_name":"Sushant Lok Phase I","types":["sublocality_level_2","sublocality","political"]},{"long_name":"Sector 43","short_name":"Sector 43","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Gurgaon","short_name":"Gurgaon","types":["locality","political"]},{"long_name":"Gurgaon","short_name":"Gurgaon","types":["administrative_area_level_2","political"]},{"long_name":"Haryana","short_name":"HR","types":["administrative_area_level_1","political"]},{"long_name":"India","short_name":"IN","types":["country","political"]},{"long_name":"122003","short_name":"122003","types":["postal_code"]}]
     * formatted_address : C-2267, Block C, Sushant Lok Phase I, Sector 43, Gurgaon, Haryana 122003, India
     * geometry : {"location":{"lat":28.4497225,"lng":77.0811627},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":28.45107148029151,"lng":77.08251168029149},"southwest":{"lat":28.4483735197085,"lng":77.07981371970848}}}
     * place_id : ChIJGZyMdcAYDTkRSV_xNxolLxE
     * types : ["street_address"]
     */

    private List<ResultsEntity> results;

    public static GoogleGeoCoading objectFromData(String str) {

        return new Gson().fromJson(str, GoogleGeoCoading.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class ResultsEntity {
        private String formatted_address;
        /**
         * location : {"lat":28.4497225,"lng":77.0811627}
         * location_type : ROOFTOP
         * viewport : {"northeast":{"lat":28.45107148029151,"lng":77.08251168029149},"southwest":{"lat":28.4483735197085,"lng":77.07981371970848}}
         */

        private GeometryEntity geometry;
        private String place_id;
        /**
         * long_name : C-2267
         * short_name : C-2267
         * types : ["premise"]
         */

        private List<AddressComponentsEntity> address_components;
        private List<String> types;

        public static ResultsEntity objectFromData(String str) {

            return new Gson().fromJson(str, ResultsEntity.class);
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public void setGeometry(GeometryEntity geometry) {
            this.geometry = geometry;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public void setAddress_components(List<AddressComponentsEntity> address_components) {
            this.address_components = address_components;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public GeometryEntity getGeometry() {
            return geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public List<AddressComponentsEntity> getAddress_components() {
            return address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public static class GeometryEntity {
            /**
             * lat : 28.4497225
             * lng : 77.0811627
             */

            private LocationEntity location;
            private String location_type;
            /**
             * northeast : {"lat":28.45107148029151,"lng":77.08251168029149}
             * southwest : {"lat":28.4483735197085,"lng":77.07981371970848}
             */

            private ViewportEntity viewport;

            public static GeometryEntity objectFromData(String str) {

                return new Gson().fromJson(str, GeometryEntity.class);
            }

            public void setLocation(LocationEntity location) {
                this.location = location;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public void setViewport(ViewportEntity viewport) {
                this.viewport = viewport;
            }

            public LocationEntity getLocation() {
                return location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public ViewportEntity getViewport() {
                return viewport;
            }

            public static class LocationEntity {
                private double lat;
                private double lng;

                public static LocationEntity objectFromData(String str) {

                    return new Gson().fromJson(str, LocationEntity.class);
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }

            public static class ViewportEntity {
                /**
                 * lat : 28.45107148029151
                 * lng : 77.08251168029149
                 */

                private NortheastEntity northeast;
                /**
                 * lat : 28.4483735197085
                 * lng : 77.07981371970848
                 */

                private SouthwestEntity southwest;

                public static ViewportEntity objectFromData(String str) {

                    return new Gson().fromJson(str, ViewportEntity.class);
                }

                public void setNortheast(NortheastEntity northeast) {
                    this.northeast = northeast;
                }

                public void setSouthwest(SouthwestEntity southwest) {
                    this.southwest = southwest;
                }

                public NortheastEntity getNortheast() {
                    return northeast;
                }

                public SouthwestEntity getSouthwest() {
                    return southwest;
                }

                public static class NortheastEntity {
                    private double lat;
                    private double lng;

                    public static NortheastEntity objectFromData(String str) {

                        return new Gson().fromJson(str, NortheastEntity.class);
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }

                    public double getLat() {
                        return lat;
                    }

                    public double getLng() {
                        return lng;
                    }
                }

                public static class SouthwestEntity {
                    private double lat;
                    private double lng;

                    public static SouthwestEntity objectFromData(String str) {

                        return new Gson().fromJson(str, SouthwestEntity.class);
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }

                    public double getLat() {
                        return lat;
                    }

                    public double getLng() {
                        return lng;
                    }
                }
            }
        }

        public static class AddressComponentsEntity {
            private String long_name;
            private String short_name;
            private List<String> types;

            public static AddressComponentsEntity objectFromData(String str) {

                return new Gson().fromJson(str, AddressComponentsEntity.class);
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }

            public String getLong_name() {
                return long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public List<String> getTypes() {
                return types;
            }
        }
    }
}

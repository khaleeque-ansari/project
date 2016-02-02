package com.firsteat.firsteat.models;

import com.google.gson.Gson;

/**
 * Created by touchmagics on 12/16/2015.
 */
public class RegisterUser {

    /**
     * status : 2
     * data : {"register":"user already exists","user_id":15,"device_id":"352795061317854","gcm_id":"APA91bGe8vl_iNBbCrEIf_-WEHmkn4VsWUYnSP6RIzdE-blmaGru1BjVsstu9Kyj_i10xu0fyiddKWRDaaoBrJy2RCXnU2TLMX0C3OOzD0j7tP69HNAdHxgjU2lhHKg-diAn-YEeQJOw","keys":{"id":"4","user_id":"15","access_token":"RYpIx6OaXYrf3Yc32yiY5aZkWfAanRl7jmYGzdjJYWcCff4LsrVWbEtAjJoEwI8Gd92e4d86986f1e4d1af378b21933e7a5","secret_key":"JEaUhr2iMZbqdxySNp3jdR4ytkkgEzfRaBxtUVAWFpneD672ZCV8jBLs20TgFAuPd92e4d86986f1e4d1af378b21933e7a5","referal_code":"0sQ0vSP"}}
     */

    private String status;
    /**
     * register : user already exists
     * user_id : 15
     * device_id : 352795061317854
     * gcm_id : APA91bGe8vl_iNBbCrEIf_-WEHmkn4VsWUYnSP6RIzdE-blmaGru1BjVsstu9Kyj_i10xu0fyiddKWRDaaoBrJy2RCXnU2TLMX0C3OOzD0j7tP69HNAdHxgjU2lhHKg-diAn-YEeQJOw
     * keys : {"id":"4","user_id":"15","access_token":"RYpIx6OaXYrf3Yc32yiY5aZkWfAanRl7jmYGzdjJYWcCff4LsrVWbEtAjJoEwI8Gd92e4d86986f1e4d1af378b21933e7a5","secret_key":"JEaUhr2iMZbqdxySNp3jdR4ytkkgEzfRaBxtUVAWFpneD672ZCV8jBLs20TgFAuPd92e4d86986f1e4d1af378b21933e7a5","referal_code":"0sQ0vSP"}
     */

    private DataEntity data;

    public static RegisterUser objectFromData(String str) {

        return new Gson().fromJson(str, RegisterUser.class);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String register;
        private int user_id;
        private String device_id;
        private String gcm_id;
        /**
         * id : 4
         * user_id : 15
         * access_token : RYpIx6OaXYrf3Yc32yiY5aZkWfAanRl7jmYGzdjJYWcCff4LsrVWbEtAjJoEwI8Gd92e4d86986f1e4d1af378b21933e7a5
         * secret_key : JEaUhr2iMZbqdxySNp3jdR4ytkkgEzfRaBxtUVAWFpneD672ZCV8jBLs20TgFAuPd92e4d86986f1e4d1af378b21933e7a5
         * referal_code : 0sQ0vSP
         */

        private KeysEntity keys;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setRegister(String register) {
            this.register = register;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public void setGcm_id(String gcm_id) {
            this.gcm_id = gcm_id;
        }

        public void setKeys(KeysEntity keys) {
            this.keys = keys;
        }

        public String getRegister() {
            return register;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public String getGcm_id() {
            return gcm_id;
        }

        public KeysEntity getKeys() {
            return keys;
        }

        public static class KeysEntity {
            private String id;
            private String user_id;
            private String access_token;
            private String secret_key;
            private String referal_code;

            public static KeysEntity objectFromData(String str) {

                return new Gson().fromJson(str, KeysEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setAccess_token(String access_token) {
                this.access_token = access_token;
            }

            public void setSecret_key(String secret_key) {
                this.secret_key = secret_key;
            }

            public void setReferal_code(String referal_code) {
                this.referal_code = referal_code;
            }

            public String getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getAccess_token() {
                return access_token;
            }

            public String getSecret_key() {
                return secret_key;
            }

            public String getReferal_code() {
                return referal_code;
            }
        }
    }
}

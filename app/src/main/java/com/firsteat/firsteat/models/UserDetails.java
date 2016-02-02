package com.firsteat.firsteat.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by touchmagics on 12/18/2015.
 */
public class UserDetails {

    /**
     * status : 0
     * data : [{"id":"26","user_email":"dil@dil","auth_password":"827ccb0eea8a706c4c34a16891f84e7b","full_name":"dilsher","mobile":"7696487712","referal_code":"1362yeA","user_role":"2","status":"0","gcmUsers":[{"id":"25","user_id":"26","device_id":"352795061317854","device_type":"Android","gcm_id":"APA91bFXAHKgpaK7xSSNPPcTXGhCExwFkXU9mr3DEtiT0qvNNQCApvxMYuhppFkr_fPPl-SE51WlJjZqBZIbsIHoFQlRJv9wpuDQGd1Jh7ScmXDOvhSIMLdcjqeV_znYgmUcMWsW1Qi_"}],"userKeys":[{"id":"15","user_id":"26","access_token":"pFuPgxS0THHIC0xMPl8U6E5yvz4OwDnqAILhzM80MbzYL0w7lOLkN2QkR61HFeTq9a6d316bb8773e4737335c76a7103aa9","secret_key":"APg3AWnXoKGwqLOY5lkgx3tZzUIZwCO7Gy7vLdlJn5FxzrT8IerK3Nipw0xGt1ci9a6d316bb8773e4737335c76a7103aa9","referal_code":"G8fcpko"}],"userPoints":{"id":"2","user_id":"26","total_points":"30"},"userPointsLogs":[],"orders":[],"contactFeedbacks":[],"userFavoriteMenus":[],"userRole":{"id":"2","role_name":"customer"},"usersAddresses":[]}]
     */

    private String status;
    /**
     * id : 26
     * user_email : dil@dil
     * auth_password : 827ccb0eea8a706c4c34a16891f84e7b
     * full_name : dilsher
     * mobile : 7696487712
     * referal_code : 1362yeA
     * user_role : 2
     * status : 0
     * gcmUsers : [{"id":"25","user_id":"26","device_id":"352795061317854","device_type":"Android","gcm_id":"APA91bFXAHKgpaK7xSSNPPcTXGhCExwFkXU9mr3DEtiT0qvNNQCApvxMYuhppFkr_fPPl-SE51WlJjZqBZIbsIHoFQlRJv9wpuDQGd1Jh7ScmXDOvhSIMLdcjqeV_znYgmUcMWsW1Qi_"}]
     * userKeys : [{"id":"15","user_id":"26","access_token":"pFuPgxS0THHIC0xMPl8U6E5yvz4OwDnqAILhzM80MbzYL0w7lOLkN2QkR61HFeTq9a6d316bb8773e4737335c76a7103aa9","secret_key":"APg3AWnXoKGwqLOY5lkgx3tZzUIZwCO7Gy7vLdlJn5FxzrT8IerK3Nipw0xGt1ci9a6d316bb8773e4737335c76a7103aa9","referal_code":"G8fcpko"}]
     * userPoints : {"id":"2","user_id":"26","total_points":"30"}
     * userPointsLogs : []
     * orders : []
     * contactFeedbacks : []
     * userFavoriteMenus : []
     * userRole : {"id":"2","role_name":"customer"}
     * usersAddresses : []
     */

    private List<DataEntity> data;

    public static UserDetails objectFromData(String str) {

        return new Gson().fromJson(str, UserDetails.class);
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
        private String id;
        private String user_email;
        private String auth_password;
        private String full_name;
        private String mobile;
        private String referal_code;
        private String user_role;
        private String status;
        /**
         * id : 2
         * user_id : 26
         * total_points : 30
         */

        private UserPointsEntity userPoints;
        /**
         * id : 2
         * role_name : customer
         */

        private UserRoleEntity userRole;
        /**
         * id : 25
         * user_id : 26
         * device_id : 352795061317854
         * device_type : Android
         * gcm_id : APA91bFXAHKgpaK7xSSNPPcTXGhCExwFkXU9mr3DEtiT0qvNNQCApvxMYuhppFkr_fPPl-SE51WlJjZqBZIbsIHoFQlRJv9wpuDQGd1Jh7ScmXDOvhSIMLdcjqeV_znYgmUcMWsW1Qi_
         */

        private List<GcmUsersEntity> gcmUsers;
        /**
         * id : 15
         * user_id : 26
         * access_token : pFuPgxS0THHIC0xMPl8U6E5yvz4OwDnqAILhzM80MbzYL0w7lOLkN2QkR61HFeTq9a6d316bb8773e4737335c76a7103aa9
         * secret_key : APg3AWnXoKGwqLOY5lkgx3tZzUIZwCO7Gy7vLdlJn5FxzrT8IerK3Nipw0xGt1ci9a6d316bb8773e4737335c76a7103aa9
         * referal_code : G8fcpko
         */

        private List<UserKeysEntity> userKeys;
        private List<?> userPointsLogs;
        private List<?> orders;
        private List<?> contactFeedbacks;
        private List<?> userFavoriteMenus;
        private List<?> usersAddresses;

        public static DataEntity objectFromData(String str) {

            return new Gson().fromJson(str, DataEntity.class);
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public void setAuth_password(String auth_password) {
            this.auth_password = auth_password;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setReferal_code(String referal_code) {
            this.referal_code = referal_code;
        }

        public void setUser_role(String user_role) {
            this.user_role = user_role;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setUserPoints(UserPointsEntity userPoints) {
            this.userPoints = userPoints;
        }

        public void setUserRole(UserRoleEntity userRole) {
            this.userRole = userRole;
        }

        public void setGcmUsers(List<GcmUsersEntity> gcmUsers) {
            this.gcmUsers = gcmUsers;
        }

        public void setUserKeys(List<UserKeysEntity> userKeys) {
            this.userKeys = userKeys;
        }

        public void setUserPointsLogs(List<?> userPointsLogs) {
            this.userPointsLogs = userPointsLogs;
        }

        public void setOrders(List<?> orders) {
            this.orders = orders;
        }

        public void setContactFeedbacks(List<?> contactFeedbacks) {
            this.contactFeedbacks = contactFeedbacks;
        }

        public void setUserFavoriteMenus(List<?> userFavoriteMenus) {
            this.userFavoriteMenus = userFavoriteMenus;
        }

        public void setUsersAddresses(List<?> usersAddresses) {
            this.usersAddresses = usersAddresses;
        }

        public String getId() {
            return id;
        }

        public String getUser_email() {
            return user_email;
        }

        public String getAuth_password() {
            return auth_password;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getReferal_code() {
            return referal_code;
        }

        public String getUser_role() {
            return user_role;
        }

        public String getStatus() {
            return status;
        }

        public UserPointsEntity getUserPoints() {
            return userPoints;
        }

        public UserRoleEntity getUserRole() {
            return userRole;
        }

        public List<GcmUsersEntity> getGcmUsers() {
            return gcmUsers;
        }

        public List<UserKeysEntity> getUserKeys() {
            return userKeys;
        }

        public List<?> getUserPointsLogs() {
            return userPointsLogs;
        }

        public List<?> getOrders() {
            return orders;
        }

        public List<?> getContactFeedbacks() {
            return contactFeedbacks;
        }

        public List<?> getUserFavoriteMenus() {
            return userFavoriteMenus;
        }

        public List<?> getUsersAddresses() {
            return usersAddresses;
        }

        public static class UserPointsEntity {
            private String id;
            private String user_id;
            private String total_points;

            public static UserPointsEntity objectFromData(String str) {

                return new Gson().fromJson(str, UserPointsEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setTotal_points(String total_points) {
                this.total_points = total_points;
            }

            public String getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getTotal_points() {
                return total_points;
            }
        }

        public static class UserRoleEntity {
            private String id;
            private String role_name;

            public static UserRoleEntity objectFromData(String str) {

                return new Gson().fromJson(str, UserRoleEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setRole_name(String role_name) {
                this.role_name = role_name;
            }

            public String getId() {
                return id;
            }

            public String getRole_name() {
                return role_name;
            }
        }

        public static class GcmUsersEntity {
            private String id;
            private String user_id;
            private String device_id;
            private String device_type;
            private String gcm_id;

            public static GcmUsersEntity objectFromData(String str) {

                return new Gson().fromJson(str, GcmUsersEntity.class);
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setDevice_id(String device_id) {
                this.device_id = device_id;
            }

            public void setDevice_type(String device_type) {
                this.device_type = device_type;
            }

            public void setGcm_id(String gcm_id) {
                this.gcm_id = gcm_id;
            }

            public String getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getDevice_id() {
                return device_id;
            }

            public String getDevice_type() {
                return device_type;
            }

            public String getGcm_id() {
                return gcm_id;
            }
        }

        public static class UserKeysEntity {
            private String id;
            private String user_id;
            private String access_token;
            private String secret_key;
            private String referal_code;

            public static UserKeysEntity objectFromData(String str) {

                return new Gson().fromJson(str, UserKeysEntity.class);
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

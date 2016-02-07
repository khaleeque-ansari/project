package com.firsteat.firsteat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firsteat.firsteat.models.Kitchens;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by touchmagics on 12/3/2015.
 */
public class Constants {
    //Development server http://itfeathers.com/firsteat.in/app/
    //Live server http://www.firsteat.in/app/
    private static final String BASE_URL="http://itfeathers.com/firsteat.in/app/";

    private static final String TAG = Constants.class.getSimpleName();
    private static final String GOOGLE_BROWSER_KEY = "AIzaSyCQeMJ_iTUeitPnr71dHMFgK_T6ZXj1BMo";
    public static final String FE_EMAIL = "contact@firsteat.in";
    public static final String FE_FACEBOOK = "https://www.facebook.com/FirstEat/";
    public static final String FE_TWITTER = "https://twitter.com/firsteat_in";
    public static final String FE_INSTA = "https://www.instagram.com/firsteat_in/";
    public static final String FE_PHONE = "+919821706223";
    public static String DEVICE_ID ;
    public static String ADVERT_ID;
    public static boolean test=true;
    public static String SENDER_ID="564217570465";
    public static final String URL_TEST="http://www.itfeathers.com/firsteat/testjson.php";
    public static final String URL_LOCATION=BASE_URL+"index.php/ws/get/all/locations";
    public static final String URL_KITCHENS=BASE_URL+"index.php/ws/kitchens/all";
    public static final String URL_MENU_ALL_ALACARTE=BASE_URL+"index.php/ws/menu/all?d=";
    public static final String URL_REGISTER_USER=BASE_URL+"index.php/ws/register/user?";
    public static final String URL_LOGIN=BASE_URL+"index.php/ws/verify/user?";
    public static final String URL_USERS_FAV_MENU=BASE_URL+"index.php/ws/user/favorite/menu/";
    public static final String URL_USERS_FAV_MENU_UPLOAD=BASE_URL+"index.php/ws/menu/user/favorite?user=";
    public static final String URL_USERS_ADDRESSES=BASE_URL+"index.php/ws/users/address/list/";
    public static final String URL_UPLOAD_ADDRESS=BASE_URL+"index.php/ws/users/address/add?user=";
    public static final String URL_ADD_NEW_ORDER=BASE_URL+"index.php/ws/orders/add/new/";
    public static final String URL_ORDER_ITEM =BASE_URL+"index.php/ws/orders/add/new/item/";
    public static final String URL_ORDER_OFFER =BASE_URL+"index.php/ws/orders/add/menu/offers/";
    public static final String URL_UPDATE_ORDER_ITEM =BASE_URL+"index.php/ws/orders/update/";
    public static final String URL_ORDER_HISTORY=BASE_URL+"index.php/ws/users/orders/all/";
    public static final String URL_FEEDBACK=BASE_URL+"index.php/ws/user/feedback/";
    public static final String URL_COUPOUN_VALIDATION=BASE_URL+"index.php/ws/orders/coupon/apply?coupon=";
    public static final String URL_GET_REEDEAM_POINT=BASE_URL+"index.php/ws/users/points/all/";
    public static final String URL_USERS_DETAIL=BASE_URL+"index.php/ws/users/details/all/";
    public static final String URL_DELIVERY_LOCATIONS=BASE_URL+"index.php/ws/locations/delivery/all";
    public static final String URL_TIME_SLOTS=BASE_URL+"index.php/ws/kitchen/slots?d=";
    public static final String URL_SPECIAL_NOTIFICATION=BASE_URL+"index.php/ws/notifications/special";
    public static final String URL_GOOGLE_GEOCODE="https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static final String URL_REDEEM_POINT=BASE_URL+"index.php/ws/points/redeem/";
    public static final String URL_FIRST_SERVICE=BASE_URL+"index.php/ws/first/app?devid=";
    private static final String URL_ORDER_DETAILS = BASE_URL+"index.php/ws/user/order/details/";
    private static final String URL_UPDATE_USER_PROFILE = BASE_URL+"index.php/ws/user/profile/update?name=";
    private static final String URL_CHECK_MOBILE = BASE_URL+"index.php/ws/mobile/check?mobile=";
    private static final String URL_CHECK_EMAIL = BASE_URL+"index.php/ws/email/check?email=";
    private static final String URL_ROLLBACK_ORDER = "http://www.dev.firsteat.com/index.php/ws/orders/rollback/";
    public static Location GPS_LOCATION;


    /*Google maps URL*/
    public static final String URL_GOOGLE_DISTENCE_MATRIX="https://maps.googleapis.com/maps/api/distancematrix/json?origins=";

    private static URL convertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    * Alacarte  menu all
    * */
    public static String getUrlMenuAllAlacarte(String date){
        String UrlMenuAllAlacarte=""+URL_MENU_ALL_ALACARTE+date;
        UrlMenuAllAlacarte.trim();
        URL url;
        url=convertToUrl(UrlMenuAllAlacarte);
        Log.d(TAG, "UrlMenuAllAlacarte url: " + url.toString());
        return url.toString();
    }
    /*
    * Users favorite menu
    * */
    public static String getUrlUsersFavMenu(String userId){
        String urlUserFavMenu=""+URL_USERS_FAV_MENU+userId;
        urlUserFavMenu.trim();
        URL url;
        url=convertToUrl(urlUserFavMenu);
        Log.d(TAG, "getUrlUsersFavMenu url: " + url.toString());
        return url.toString();
    }

    /*
    * Users favorite menu UPLOAD
    * */
    public static String getUrlUsersFavMenuUpload(String userId,int menuId){

        String urlUserFavMenuUpload=""+URL_USERS_FAV_MENU_UPLOAD+userId+
                "&menu="+menuId;
        urlUserFavMenuUpload.trim();
        URL url;
        url=convertToUrl(urlUserFavMenuUpload);
        Log.d(TAG, "urlUserFavMenuUpload url: " + url.toString());
        return url.toString();
    }

    /*
    * Registration
    * */
    public static String getUrlRegsitration(String email,String pwd,String name,String mobile,String devId,String gcmId,String referalId){
        String ref;
        if (referalId==null)
            ref="NULL";
        else
            ref=referalId;

        String urlRegistration=""+URL_REGISTER_USER+"email="+email+
                "&pwd="+pwd+
                "&name="+name+
                "&mobile="+mobile+
                "&address="+
                "&devid="+devId+"&devtype=Android"+
                "&gcm="+gcmId+
                "&ref="+ref;
        urlRegistration.trim();
        URL url;
        url=convertToUrl(urlRegistration);
        Log.d(TAG, "registration url: " + url.toString());

        return url.toString();
    }

    /*
    * Login
    * */
    public static String getUrlLogin(String email,String pwd,String devId,String gcmId){
        String urlLogin=""+URL_LOGIN+"email="+email+
                "&pwd="+pwd+
                "&devid="+devId+"&devtype=Android"+
                "&gcm="+gcmId;
        urlLogin.trim();
        URL url;
        url=convertToUrl(urlLogin);
        Log.d(TAG, "login url: " + url.toString());
       return url.toString();
    }
/*
* get all address asociated with a particular user
* */
    public static String getUrlUsersAddList(String userID) {
        String addURL=URL_USERS_ADDRESSES+userID;
        Log.d(TAG,"URL_USERS_ADDRESSES: "+addURL);
        addURL.trim();
        URL url;
        url=convertToUrl(addURL);
        Log.d(TAG, "getUrlUsersAddList url: " + url.toString());
        return url.toString();
    }

    /*
    * upload address to a specific user
    * */
    public static String getUrlUploadAdd(String userid,String line1,String line2,String line3,String category) {
        String urlgetUrlUploadAdd=URL_UPLOAD_ADDRESS+userid+
                "&line1="+line1+
                "&line2="+line2+
                "&line3="+line3+
                "&category="+category;
        urlgetUrlUploadAdd.trim();
        URL url;
        url=convertToUrl(urlgetUrlUploadAdd);
        Log.d(TAG, "urlgetUrlUploadAdd url: " + url.toString());
        return url.toString();
    }

    /*
    * Place new order
    * */
    public static String getUrlAddNewOrder(String userId,String changeFor,String couponId,String kitchenId){
        String urlAddNewOrder=URL_ADD_NEW_ORDER+userId+
                "?change="+changeFor+
                "&coupon="+couponId+
                "&kitchen="+kitchenId;
        urlAddNewOrder.trim();
        URL url;
        url=convertToUrl(urlAddNewOrder);
        Log.d(TAG, "urlAddNewOrder url: " + url.toString());
        return url.toString();
    }

    /*
   * Place order item
   * */
    public static String getUrlAddOrderItem(String orderId,String ids,String qty){

        String urlAddOrderItem= URL_ORDER_ITEM +orderId+
                "?menu="+ids+
                "&qty="+qty;
        urlAddOrderItem.trim();
        URL url;
        url=convertToUrl(urlAddOrderItem);
        Log.d(TAG, "urlAddOrderItem url: " + url.toString());
        return url.toString();

    }

    /*
   * Place order offers
   * */
    public static String getUrlAddOrderOffers(String orderId,ArrayList<String> offerIDs){
        String ids=null;
        for(int i=0;i<offerIDs.size();i++){
            if(i==0)
                ids=offerIDs.get(i).toString();
            else
                ids=ids+","+offerIDs.get(i).toString();
        }

        String urlAddOrderOffers= URL_ORDER_OFFER +orderId+
                "?offers="+ids;
        urlAddOrderOffers.trim();
        URL url;
        url=convertToUrl(urlAddOrderOffers);
        Log.d(TAG, "urlAddOrderOffers url: " + url.toString());
        return url.toString();

    }


    /*
  * Update order
  * this is final service to hit for order
  * */
    public static String getUrlUpdateOrder(String orderId,String subTotal, String discount,
                                           String vat, String surcharge, String totalamount,
                                           String addressId, String timeSlot, String orderDate){


        String urlUpdateOrder= URL_UPDATE_ORDER_ITEM +orderId+
                "?subtotal="+subTotal+//total amount before discount cal.
                "&discount="+discount+//discount by coupounds or any other mean
                "&vat="+vat+//vat tax
                "&surcharge="+surcharge+//surcharge tax
                "&total="+totalamount+//total after discount
                "&address="+addressId+//address id
                "&slot="+timeSlot+//time slot
                "&d="+orderDate;//order date
        urlUpdateOrder.trim();
        URL url;
        url=convertToUrl(urlUpdateOrder);
        Log.d(TAG, "urlUpdateOrder url: " + url.toString());
        return url.toString();

    }

    /*
    * getUrlGoogleDistenceMatrix for finding distence between two points
    * */
    public static String getUrlGoogleDistenceMatrix(String originLat,String originLong,Kitchens kitchens){

        String destinations="";
        for(int i=0;i<kitchens.getData().size();i++){
            if(i==0)
                destinations=""+kitchens.getData().get(i).getLatitude()+","+kitchens.getData().get(i).getLongitude();
            else
                destinations=destinations+"|"+kitchens.getData().get(i).getLatitude()+","+kitchens.getData().get(i).getLongitude();
        }

        String urlGoogleDistenceMatrix= URL_GOOGLE_DISTENCE_MATRIX +originLat+","+originLong+
                "&destinations="+destinations+
                "&language=en-EN&key="+Constants.GOOGLE_BROWSER_KEY;
        urlGoogleDistenceMatrix.trim();
        URL url;
        url=convertToUrl(urlGoogleDistenceMatrix);
        Log.d(TAG, "urlGoogleDistenceMatrix url: " + url.toString());
        return url.toString();
    }

    /*
    * getUrlGoogleGeocode (detailed address)
    * */
    public static String getUrlGoogleGeocode(String lat,String lon){
       String urlGoogleGeocode= URL_GOOGLE_GEOCODE +lat+","+lon+
                "&key="+Constants.GOOGLE_BROWSER_KEY;
        urlGoogleGeocode.trim();
        URL url;
        url=convertToUrl(urlGoogleGeocode);
        Log.d(TAG, "urlGoogleGeocode url: " + url.toString());
        return url.toString();
    }

    /*
   * getUrlOrderHistory for get user's order history
   * */
    public static String getUrlOrderHistory(String userID){

        String urlOrderHistory= URL_ORDER_HISTORY +userID;
        Log.d(TAG,"urlOrderHistory "+urlOrderHistory );
        urlOrderHistory.trim();
        URL url;
        url=convertToUrl(urlOrderHistory);
        Log.d(TAG, "urlUpdateOrder url: " + url.toString());
        return url.toString();
    }

    /*
  * getUrlOrderHistory for get user's order history
  * */
    public static String getUrlFeedback(String userID,String msg,String rating){
        String urlFeedback= URL_FEEDBACK +userID+
                "?msg="+msg+
                "&rating="+rating;
        Log.d(TAG,"urlOrderHistory "+urlFeedback );
        urlFeedback.trim();
        URL url;
        url=convertToUrl(urlFeedback);
        Log.d(TAG, "urlFeedback url: " + url.toString());
        return url.toString();
    }



    /*
 * getUrlCoupounValidation for validating coupouns
 * */
    public static String getUrlCoupounValidation(String coupounCode,String userID,String totalAmount,String menuIds,String itemQtys){
        String urlCoupounValidation= URL_COUPOUN_VALIDATION +coupounCode+
                "&user="+userID+
                "&amt="+totalAmount+
                "&menu="+menuIds+
                "&qty="+itemQtys;
        Log.d(TAG,"urlCoupounValidation "+urlCoupounValidation );
        urlCoupounValidation.trim();
        URL url;
        url=convertToUrl(urlCoupounValidation);
        Log.d(TAG, "urlCoupounValidation url: " + url.toString());
        return url.toString();
    }

    /*
    * getUrlGetReedeamPoint for validating coupouns (to get total available redeam points for the users)
    * */
    public static String getUrlGetReedeamPoint(String userId){
        String urlGetReedeamPoint= URL_GET_REEDEAM_POINT +userId;
        urlGetReedeamPoint.trim();
        URL url;
        url=convertToUrl(urlGetReedeamPoint);
        Log.d(TAG, "urlGetReedeamPoint url: " + url.toString());
        return url.toString();
    }

    /*
   * getUrlReedeamPoint for validating coupouns
   * */
    public static String getUrlReedeamPoint(String userId,String points){
        String urlReedeamPoint= URL_REDEEM_POINT +userId+
                "?points="+points;
        urlReedeamPoint.trim();
        URL url;
        url=convertToUrl(urlReedeamPoint);
        Log.d(TAG, "urlReedeamPoint url: " + url.toString());
        return url.toString();
    }

    /*
    * URL Users detail
    * */
    public static String getUrlUsersDetail(String userId){
        String urlUsersDetail= URL_USERS_DETAIL +userId;
        urlUsersDetail.trim();
        URL url;
        url=convertToUrl(urlUsersDetail);
        Log.d(TAG, "urlUsersDetail url: " + url.toString());
        return url.toString();
    }

    /*
    * Get Url time Slots
    * */
    public static String getUrlTimeSlots(String time){
        String urlTimeSlots= URL_TIME_SLOTS +time;
        Log.d(TAG,"urlTimeSlots "+urlTimeSlots );
        urlTimeSlots.trim();
        URL url;
        url=convertToUrl(urlTimeSlots);
        Log.d(TAG, "urlTimeSlots url: " + url.toString());
        return url.toString();
    }

    /*
    * Get Url first service
    * */
    public static String getUrlFirstService(String deviceId, String gcmId, String primaryEmail){
        String urlFirstService= URL_FIRST_SERVICE +deviceId+
                "&dev=ANDROID&gcm="+gcmId+
                "&email="+primaryEmail;
        urlFirstService.trim();
        URL url;
        url=convertToUrl(urlFirstService);
        Log.d(TAG, "urlFirstService url: " + url.toString());
        return url.toString();
    }

    /*
    * Get Url oder detail of a specific order
    * */
    public static String getUrlOrderDetails(String orderid) {
        String urlOrderDetail= URL_ORDER_DETAILS +orderid;
        Log.d(TAG,"urlOrderDetail "+urlOrderDetail );
        urlOrderDetail.trim();
        URL url;
        url=convertToUrl(urlOrderDetail);
        Log.d(TAG, "urlOrderDetail url: " + url.toString());
        return url.toString();
    }

    /*
    * get time slot from start time and end time stamp
    * */
    public static String getTimeSlots(String starttime,String endTime){
        String startHr=starttime.substring(0, starttime.indexOf(":"));
        String startMin=starttime.substring(starttime.indexOf(":")+1,5);
        String endHr=endTime.substring(0, endTime.indexOf(":"));
        String endMin=endTime.substring(endTime.indexOf(":") + 1,5);
        String shift= (Integer.parseInt(startHr) > 12) ? "PM" : "AM";
        String time="";
        //converting time in 12 hours format
        if(Integer.parseInt(startHr)>12){
            startHr=String.valueOf((Integer.parseInt(startHr)-12));
            endHr=String.valueOf((Integer.parseInt(endHr)-12));
        }
        //eliminate minute if minute value is 00
        if(Integer.parseInt(startMin)==0){
            time= startHr+shift+"-"+endHr+shift;
        }else{
            time= startHr+":"+startMin+shift+"-"+endHr+":"+endMin+shift;
        }

        return time;
    }

    /*
    * get (EEE MMM dd,yyyy) from default date stamp
    * */
    public static String getDate(String dateStamp){
        String dateString="";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat desiredformat = new SimpleDateFormat("EEE MMM dd, yyyy");
        try {
            Date date = format.parse(dateStamp);
            System.out.println(date);
            dateString=desiredformat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dateString;
    }

    /*
    * Update user's profile
    * */
    public static String getUrlUpdateUserProfile(String name,String email,String mobile){
        String urlUpdateUserProfile= URL_UPDATE_USER_PROFILE +name+
                "&email="+email+
                "&mobile="+mobile;
        Log.d(TAG,"urlOrderDetail "+urlUpdateUserProfile );
        urlUpdateUserProfile.trim();
        URL url;
        url=convertToUrl(urlUpdateUserProfile);
        Log.d(TAG, "urlUpdateUserProfile url: " + url.toString());
        return url.toString();
    }

    /*
    * Check whether Mobile number exist or not
    * */
    public static String getUrlCheckMobile(String mobile){
        String urlCheckMobile= URL_CHECK_MOBILE +mobile;
        Log.d(TAG,"urlCheckMobile "+urlCheckMobile );
        urlCheckMobile.trim();
        URL url;
        url=convertToUrl(urlCheckMobile);
        Log.d(TAG, "urlCheckMobile url: " + url.toString());
        return url.toString();
    }

    /*
   * Check whether Email number exist or not
   * */
    public static String getUrlCheckEmail(String email){
        String urlCheckEmail= URL_CHECK_EMAIL +email;
        Log.d(TAG,"urlCheckEmail "+urlCheckEmail );
        urlCheckEmail.trim();
        URL url;
        url=convertToUrl(urlCheckEmail);
        Log.d(TAG, "urlCheckEmail url: " + url.toString());
        return url.toString();
    }

    /*
   * Rollback order
   * */
    public static String getUrlRollbackOrder(String orderid){
        String UrlRollbackOrder= URL_ROLLBACK_ORDER +orderid;
        Log.d(TAG,"UrlRollbackOrder "+UrlRollbackOrder );
        UrlRollbackOrder.trim();
        URL url;
        url=convertToUrl(UrlRollbackOrder);
        Log.d(TAG, "UrlRollbackOrder url: " + url.toString());
        return url.toString();
    }

    public static int getAppPrefInt(Context context, String prefName){
        int result = -1;
        if(context != null){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if(sharedPreferences!=null){
                result = sharedPreferences.getInt(
                        prefName, -1);
            }
        }
        return result;
    }

    public static void putAppPrefInt(Context context, String prefName, int value) {
        if(context!=null){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt(prefName, value);
            if(Build.VERSION.SDK_INT>=9){
                edit.apply();
            }else{
                edit.commit();
            }
        }
    }

    public static int getAppVersionCode(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }

        return version;
    }

    /*
    * get time in militry format
    * */
    public static int getMilitryTimming(String timestamp){
        int militryTime=0000;
        //timestamp 08:00
        String hr=timestamp.substring(0,timestamp.indexOf(":"));
        String min=timestamp.substring(timestamp.indexOf(":")+1,timestamp.lastIndexOf(":"));
        militryTime=Integer.parseInt(hr+min);
        Log.d(TAG,"militry timming for "+timestamp+"= "+militryTime);
        return militryTime;
    }
}

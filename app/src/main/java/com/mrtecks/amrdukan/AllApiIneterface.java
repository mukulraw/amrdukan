package com.mrtecks.amrdukan;


import com.mrtecks.amrdukan.addressPOJO.addressBean;
import com.mrtecks.amrdukan.cartPOJO.cartBean;
import com.mrtecks.amrdukan.checkPromoPOJO.checkPromoBean;
import com.mrtecks.amrdukan.checkoutPOJO.checkoutBean;
import com.mrtecks.amrdukan.homePOJO.homeBean;
import com.mrtecks.amrdukan.orderDetailsPOJO.orderDetailsBean;
import com.mrtecks.amrdukan.ordersPOJO.ordersBean;
import com.mrtecks.amrdukan.productsPOJO.productsBean;
import com.mrtecks.amrdukan.searchPOJO.searchBean;
import com.mrtecks.amrdukan.seingleProductPOJO.singleProductBean;
import com.mrtecks.amrdukan.subCat1POJO.subCat1Bean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {


    @Multipart
    @POST("amrdukan/api/getHome.php")
    Call<homeBean> getHome(@Part("user_id") String user_id);

    @Multipart
    @POST("amrdukan/api/getSubCat1.php")
    Call<subCat1Bean> getSubCat1(
            @Part("cat") String cat
    );

    @Multipart
    @POST("amrdukan/api/getSubCat2.php")
    Call<subCat1Bean> getSubCat2(
            @Part("subcat1") String cat
    );

    @Multipart
    @POST("amrdukan/api/getProducts.php")
    Call<productsBean> getProducts(
            @Part("subcat1") String subcat1
    );

    @Multipart
    @POST("amrdukan/api/getProductById.php")
    Call<singleProductBean> getProductById(
            @Part("id") String cat
    );

    @Multipart
    @POST("amrdukan/api/search.php")
    Call<searchBean> search(
            @Part("query") String query
    );

    @Multipart
    @POST("amrdukan/api/login.php")
    Call<loginBean> login(
            @Part("phone") String phone,
            @Part("token") String token
    );

    @Multipart
    @POST("amrdukan/api/verify.php")
    Call<loginBean> verify(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("amrdukan/api/addCart.php")
    Call<singleProductBean> addCart(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price,
            @Part("version") String version
    );

    @Multipart
    @POST("amrdukan/api/addFav.php")
    Call<singleProductBean> addFav(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id
    );

    @Multipart
    @POST("amrdukan/api/addRating.php")
    Call<singleProductBean> addRating(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id,
            @Part("rating") String rating
    );

    @Multipart
    @POST("amrdukan/api/rateOrder.php")
    Call<singleProductBean> rateOrder(
            @Part("id") String id,
            @Part("rating") String rating
    );

    @Multipart
    @POST("amrdukan/api/updateCart.php")
    Call<singleProductBean> updateCart(
            @Part("id") String id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price
    );

    @Multipart
    @POST("amrdukan/api/deleteCart.php")
    Call<singleProductBean> deleteCart(
            @Part("id") String id
    );

    @Multipart
    @POST("amrdukan/api/getRew.php")
    Call<String> getRew(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/clearCart.php")
    Call<singleProductBean> clearCart(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/getOrderDetails.php")
    Call<orderDetailsBean> getOrderDetails(
            @Part("order_id") String order_id
    );

    @Multipart
    @POST("amrdukan/api/getFav.php")
    Call<orderDetailsBean> getFav(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/getCart.php")
    Call<cartBean> getCart(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/getOrders.php")
    Call<ordersBean> getOrders(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/getPres.php")
    Call<ordersBean> getPres(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/checkPromo.php")
    Call<checkPromoBean> checkPromo(
            @Part("promo") String promo,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/buyVouchers.php")
    Call<checkoutBean> buyVouchers(
            @Part("user_id") String user_id,
            @Part("amount") String amount,
            @Part("txn") String txn,
            @Part("name") String name,
            @Part("address") String address,
            @Part("pay_mode") String pay_mode,
            @Part("slot") String slot,
            @Part("date") String date,
            @Part("promo") String promo,
            @Part("house") String house,
            @Part("area") String area,
            @Part("city") String city,
            @Part("pin") String pin,
            @Part("isnew") String isnew
    );

    @Multipart
    @POST("amrdukan/api/getAddress.php")
    Call<addressBean> getAddress(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("amrdukan/api/deleteAddress.php")
    Call<addressBean> deleteAddress(
            @Part("id") String id
    );

    @Multipart
    @POST("amrdukan/api/uploadPres.php")
    Call<checkoutBean> uploadPres(
            @Part("user_id") String user_id,
            @Part MultipartBody.Part file1,
            @Part("txn") String txn,
            @Part("name") String name,
            @Part("address") String address,
            @Part("pay_mode") String pay_mode,
            @Part("slot") String slot,
            @Part("date") String date,
            @Part("promo") String promo,
            @Part("house") String house,
            @Part("area") String area,
            @Part("city") String city,
            @Part("pin") String pin,
            @Part("isnew") String isnew
    );

}

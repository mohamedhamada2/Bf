package com.mz.bf.api;




import com.mz.bf.add_masrouf.Masrouf;
import com.mz.bf.addbill.ClientDiscount;
import com.mz.bf.addclient.City;
import com.mz.bf.addclient.Govern;
import com.mz.bf.addclient.SuccessModel;
import com.mz.bf.addpayment.SandModel;
import com.mz.bf.addsalary.RKM;
import com.mz.bf.addvisit.VisitModel;
import com.mz.bf.allbills.AllBillsModel;
import com.mz.bf.allbills.BillDetailsModel;
import com.mz.bf.authentication.LoginModel;
import com.mz.bf.addbill.Bill;
import com.mz.bf.addbill.ClientModel;
import com.mz.bf.addbill.LastBill;
import com.mz.bf.addbill.ProductModel;
import com.mz.bf.addbill.SpinnerModel;
import com.mz.bf.client_safe.SafeModel;
import com.mz.bf.clients.ClientLocation;
import com.mz.bf.code.Code;
import com.mz.bf.masroufat.AllMasroufat;
import com.mz.bf.uis.activity_print_bill.EsalModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetDataService{
/*1*/
    @FormUrlEncoded
    @POST("Api/login")
    Call<LoginModel>login_user(@Field("user_name") String user_name,
                               @Field("password")String password);

    @FormUrlEncoded
    @POST("Api/get_data_by_id")
    Call<LoginModel> get_user_data(@Field("row_id")String row_id);
    @FormUrlEncoded
    @POST("Api/update_data")
    Call<LoginModel> update_data_without_img(@Field("row_id")String row_id,
                                 @Field("user_name")String user_name,
                                 @Field("password")String password);
    @Multipart
    @POST("Api/update_data")
    Call<LoginModel> update_data_with_img(@Part("row_id")RequestBody row_id,
                                          @Part("user_name")RequestBody user_name,
                                          @Part("password")RequestBody password,
                                          @Part MultipartBody.Part img);

    @GET("Api/get_governs")
    Call<List<Govern>> get_govern();

    @FormUrlEncoded
    @POST("Api/get_city")
    Call<List<City>> get_city(@Field("govern_id")String govern_id);
    @FormUrlEncoded
    @POST("Api/add_client")
    Call<SuccessModel> add_client(@Field("name")String name,
                                  @Field("govern_id_fk")String govern_id_fk,
                                  @Field("city_id_fk")String city_id_fk,
                                  @Field("national_num")String national_num,
                                  @Field("mob")String mob,
                                  @Field("mob2")String mob2,
                                  @Field("adress")String adress,
                                  @Field("latitude")String latitude,
                                  @Field("longitude")String longitude,
                                  @Field("user_id")String user_id);

    @GET("Api/get_main_branches")
    Call<List<SpinnerModel>> get_main_branches();

    @FormUrlEncoded
    @POST("Api/get_sub_branches")
    Call<List<SpinnerModel>> get_sub_branches(@Field("main_branch_id_fk")String main_branch_id_fk);

    @FormUrlEncoded
    @POST("Api/get_storages")
    Call<List<SpinnerModel>> get_warehouses(@Field("sub_branch_id_fk")String sub_branch_id_fk);

    @FormUrlEncoded
    @POST("Api/get_products_by_car")
    Call<ProductModel> get_all_products(@Field("page")Integer page,@Field("user_id_fk")String user_id_fk,@Field("car_id_fk")String car_id_fk);

    @GET("Api/get_last_rkm")
    Call<LastBill> get_last_bill();
    @FormUrlEncoded
    @POST("Api/get_mandoub_clients")
    Call<ClientModel> get_clients(@Field("user_id")String user_id,@Field("page")Integer page);

    @POST("Api/add_fatora")
    Call<SuccessModel> add_bill(@Body Bill bill);

    @FormUrlEncoded
    @POST("Api/get_mandoub_fatoras")
    Call<AllBillsModel> get_bills(@Field("user_id")String user_id, @Field("page")Integer page,@Field("from_date")String from_date,@Field("to_date")String to_date,@Field("client_id_fk")String client_id,@Field("rkm_fatora")String rkm_fatora);

    @FormUrlEncoded
    @POST("Api/get_fatora_details")
    Call<BillDetailsModel> get_bill_details(@Field("fatora_id")String fatora_id);
    @GET("Api/get_last_hadback_rkm")
    Call<LastBill> get_last_bill2();

    @POST("Api/add_hadbackfatora")
    Call<SuccessModel> add_bill2(@Body Bill bill);

    @FormUrlEncoded
    @POST("Api/get_mandoub_hadbackfatoras")
    Call<AllBillsModel> get_bills2(@Field("user_id")String user_id, @Field("page")Integer page);

    @FormUrlEncoded
    @POST("Api/get_hadbackfatora_details")
    Call<BillDetailsModel> get_bill_details2(@Field("fatora_id")String fatora_id);

    @FormUrlEncoded
    @POST("Api/get_client_discount_nesba")
    Call<ClientDiscount> get_client_discount(@Field("client_id_fk")String client_id_fk);

    @FormUrlEncoded
    @POST("Api/search_product")
    Call<ProductModel> search_product(@Field("page")Integer page,
                                      @Field("car_id_fk")String car_id_fk,
                                      @Field("search_word")String search_word,
                                      @Field("user_id")String user_id);
    @FormUrlEncoded
    @POST("Api/search_clients")
    Call<ClientModel> search_clients(@Field("user_id")String user_id,@Field("search_word")String search_word,@Field("page")Integer page);

    @FormUrlEncoded
    @POST("Api/add_sand")
    Call<SuccessModel> add_sand(@Field("type")String type,
                                @Field("user_id_fk")String user_id,
                                @Field("date_esal")String date_esal,
                                @Field("client_id_fk")String client_id_fk,
                                @Field("value")String value,
                                @Field("notes")String notes);

    @GET("Api/get_rkm_esal")
    Call<RKM> get_rkm();

    @FormUrlEncoded
    @POST("Api/get_all_sandat")
    Call<SandModel> get_all_sandat(@Field("user_id")String user_id,
                                   @Field("type")String type,
                                   @Field("page")Integer page,
                                   @Field("from_date")String from_date,
                                   @Field("to_date")String to_dt,
                                   @Field("client_id_fk")String client_id);

    @FormUrlEncoded
    @POST("Api/add_visit")
    Call<SuccessModel> add_visit(@Field("client_id_fk")String client_id_fk,
                                 @Field("lat_map")String lat_map,
                                 @Field("long_map")String long_map,
                                 @Field("notes")String notes,
                                 @Field("user_id_fk")String user_id_fk);
    @FormUrlEncoded
    @POST("Api/get_user_visit")
    Call<VisitModel> get_all_visits(@Field("user_id")String user_id,
                                    @Field("page")Integer page);

    @FormUrlEncoded
    @POST("Api/clients_locations")
    Call<ClientLocation> get_all_clients(@Field("user_id")String user_id,
                                         @Field("page")Integer page,
                                         @Field("search_word")String search);

    @FormUrlEncoded
    @POST("Api/update_client_location")
    Call<SuccessModel> update_location(@Field("user_id_fk")String user_id_fk,
                                       @Field("client_id_fk")String client_id_fk,
                                       @Field("lat")String lat,
                                       @Field("long")String lon);

    @FormUrlEncoded
    @POST("Api/get_esal_detais")
    Call<EsalModel> get_esal_details(@Field("esal_id")String esal_id);

    @FormUrlEncoded
    @POST("Api/get_mandoub_safe")
    Call<SafeModel> get_mandoub_safe(@Field("user_id_fk")String user_id, @Field("day_date")String day_date);
    @FormUrlEncoded
    @POST("Api/get_url_by_code")
    Call<Code> get_code(@Field("code")String code);

    @GET("Api/masrofat")
    Call<List<Masrouf>> get_masroufat();

    @FormUrlEncoded
    @POST("Api/add_masrof")
    Call<SuccessModel> add_masrouf(@Field("fe2a")String fe2a,
                                   @Field("date_esal")String date_esal,
                                   @Field("value")String value,
                                   @Field("user_id")String user_id);

    @FormUrlEncoded
    @POST("Api/get_all_masrofat")
    Call<AllMasroufat> get_all_masrofat(@Field("user_id")String user_id,@Field("page")Integer page);
}


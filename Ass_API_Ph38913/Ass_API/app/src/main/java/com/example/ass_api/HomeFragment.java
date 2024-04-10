package com.example.ass_api;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ass_api.Adapter.AdapterProduct;
import com.example.ass_api.Handle.Item_Product_Handle;
import com.example.ass_api.Model.Product;
import com.example.ass_api.Model.Response_Model;
import com.example.ass_api.Services.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements Item_Product_Handle {
        private HttpRequest httpRequest;
        private AdapterProduct adapterProduct;
        private RecyclerView rcv_product;
        private ArrayList<Product> originList;
        private ArrayList<Product> displayList;
        private Item_Product_Handle handle;

        EditText edtSearch;
        FloatingActionButton btn_add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rcv_product = view.findViewById(R.id.rcv_product);
        btn_add = view.findViewById(R.id.btn_add);
        edtSearch = view.findViewById(R.id.edtsearch);

        httpRequest = new HttpRequest();
        httpRequest.callApi().getListProduct().enqueue(getProductAPI);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //lấy từ khoá từ ô tìm kiếm
                    String key = edtSearch.getText().toString();

                    httpRequest.callApi().searchProduct(key)//phương thức api cần thực thi
                            .enqueue(getProductAPI);//xử lý bất đồng bộ
                    //vì giá trị trả về vẫ là một list distributor
                    //nên có thể sử dụng lại callback của getListDistributor
                    return true;
                }

                return false;
            }
        });
        //tìm kiếm
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    //lấy từ khóa từ ô tìm kiếm
                    String key=edtSearch.getText().toString();
                    httpRequest.callApi()
                            .searchProduct(key) //phung thức API cần thực thi
                            .enqueue(getProductAPI);//xử lý bất đồng bộ
                    return true;
                }
                return false;
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAdd();
            }
        });
        handle = new Item_Product_Handle() {
            @Override
            public void Delete(String id) {
                httpRequest.callApi().deleteProductById(id).enqueue(responseProductAPI);
            }

            @Override
            public void Update(String id, Product product) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_update, null);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();

                EditText edtname_pro = view.findViewById(R.id.edt_name_update);
                EditText edtprice_pro = view.findViewById(R.id.edt_price_update);
                EditText edtweight_pro = view.findViewById(R.id.edt_weight_update);
                Button btnUpdate = view.findViewById(R.id.btn_update_dialog);

                edtname_pro.setText(product.getName_pro());
                edtprice_pro.setText(product.getPrice_pro());
                edtweight_pro.setText(String.valueOf(product.getWeight_pro()));

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edtname_pro.getText().toString();
                        String price = edtprice_pro.getText().toString();
                        String weight = edtweight_pro.getText().toString();
                        String id = product.getId();
                        if(name.isEmpty()){
                            Toast.makeText(getContext(), "Vui lòng nhập tên Sản Phẩm", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Product product = new Product();
                            product.setName_pro(name);
                            product.setPrice_pro(Integer.parseInt(price));
                            product.setWeight_pro(Integer.parseInt(weight));
                            httpRequest.callApi().updateProductById(id, product).enqueue(responseProductAPI);
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        return view;
    }
    private void getData(ArrayList<Product> list) {
        adapterProduct = new AdapterProduct(getContext(), list,handle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv_product.setLayoutManager(layoutManager);
        rcv_product.setAdapter(adapterProduct);

    }
    private void openDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View diaglogAdd = inflater.inflate(R.layout.dialog_add, null);
        builder.setView(diaglogAdd);
        Dialog dialog = builder.create();
        dialog.show();

        // Khởi tạo đối tượng btnAdd từ layout dialog_add.xml
        Button btnAdd = diaglogAdd.findViewById(R.id.btn_add_dialog);
        EditText edt_image_add = diaglogAdd.findViewById(R.id.edt_image_add);
        EditText edt_name_add = diaglogAdd.findViewById(R.id.edt_name_add);
        EditText edt_weight_add = diaglogAdd.findViewById(R.id.edt_weight_add);
        EditText edt_price_add =diaglogAdd.findViewById(R.id.edt_price_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image_add = edt_image_add.getText().toString();
                String name_add = edt_name_add.getText().toString();
                String weight_add = edt_weight_add.getText().toString();
                String price_add = edt_price_add.getText().toString();
                String img_pro = edt_image_add.getText().toString();
                Product product = new Product();

                if (image_add.isEmpty()||name_add.isEmpty()||weight_add.isEmpty()||price_add.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập du thong tin", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    product.setImage_pro(image_add);
                    product.setName_pro(name_add);
                    product.setWeight_pro(Integer.parseInt(weight_add));
                    product.setPrice_pro(Integer.parseInt(price_add));
                    product.setImage_pro(img_pro);
                    httpRequest.callApi().addProduct(product).enqueue(responseProductAPI);
                    Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

    }
    Callback<Response_Model<ArrayList<Product>>> getProductAPI = new Callback<Response_Model<ArrayList<Product>>>() {
        @Override
        public void onResponse(Call<Response_Model<ArrayList<Product>>> call, Response<Response_Model<ArrayList<Product>>> response) {
            // khi call thành công sẽ chạy vào hàm này
            if (response.isSuccessful()) {
                // check status
                if (response.body().getStatus() == 200) {
                    ArrayList<Product> list = response.body().getData();
                    Log.d("List", "onResponse: " + list);
                    getData(list);

                }
            }
        }
        @Override
        public void onFailure(Call<Response_Model<ArrayList<Product>>> call, Throwable t) {
            Log.d(">>>GetlistStudent", "onFailure: " + t.getMessage());
        }
    };
    Callback<Response_Model<Product>> responseProductAPI = new Callback<Response_Model<Product>>() {
        @Override
        public void onResponse(Call<Response_Model<Product>> call, Response<Response_Model<Product>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    httpRequest.callApi().getListProduct().enqueue(getProductAPI);
                }
            }
        }

        @Override
        public void onFailure(Call<Response_Model<Product>> call, Throwable t) {
            Log.d(">>>GetlistStudent", "onFailure: " + t.getMessage());
        }
    };


    @Override
    public void Delete(String id) {
        httpRequest.callApi().deleteProductById(id).enqueue(responseProductAPI);
    }

    @Override
    public void Update(String id, Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtname_pro = view.findViewById(R.id.edt_name_update);
        EditText edtprice_pro = view.findViewById(R.id.edt_price_update);
        EditText edtimg_pro = view.findViewById(R.id.edt_image_update);
        EditText edtweight_pro = view.findViewById(R.id.edt_weight_update);
        Button btnUpdate = view.findViewById(R.id.btn_update_dialog);

        edtname_pro.setText(product.getName_pro());
        edtprice_pro.setText(product.getPrice_pro());
        edtweight_pro.setText(String.valueOf(product.getWeight_pro()));
        edtimg_pro.setText(product.getImage_pro());
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtname_pro.getText().toString();
                String price = edtprice_pro.getText().toString();
                String weight = edtweight_pro.getText().toString();
                String id = product.getId();
                if(name.isEmpty()){
                    Toast.makeText(getContext(), "Vui lòng nhập tên Sản Phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Product product1 = new Product();
                    product1.setName_pro(name);
                    product1.setPrice_pro(Integer.parseInt(price));
                    product1.setWeight_pro(Integer.parseInt(weight));
                    httpRequest.callApi().updateProductById(id, product1).enqueue(responseProductAPI);
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
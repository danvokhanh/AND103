package com.example.ass_api.Handle;

import com.example.ass_api.Model.Product;

public interface Item_Product_Handle {
    public void Delete(String id);
    public void Update(String id, Product product);
}

package com.example.chillcup02_ui.domain.usecase;

import com.example.chillcup02_ui.data.repository.CatalogRepository;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.domain.model.Product;

import java.util.List;

public class LoadCatalogUseCase {

    private final CatalogRepository repository;

    public LoadCatalogUseCase(CatalogRepository repository) {
        this.repository = repository;
    }

    public void loadCategories(CatalogRepository.ResultCallback<List<Category>> callback) {
        repository.getCategories(callback);
    }

    public void loadAllProducts(CatalogRepository.ResultCallback<List<Product>> callback) {
        repository.getAllProducts(callback);
    }

    public void loadProductsByCategory(String categoryId, CatalogRepository.ResultCallback<List<Product>> callback) {
        repository.getProductsByCategory(categoryId, callback);
    }
}

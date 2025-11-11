package com.example.chillcup02_ui.domain.usecase;

import com.example.chillcup02_ui.data.repository.CatalogRepository;
import com.example.chillcup02_ui.domain.model.Category;

import java.util.List;

public class LoadCatalogUseCase {

    private final CatalogRepository repository;

    public LoadCatalogUseCase(CatalogRepository repository) {
        this.repository = repository;
    }

    public void execute(CatalogRepository.ResultCallback<List<Category>> callback) {
        repository.getCategories(callback);
    }
}

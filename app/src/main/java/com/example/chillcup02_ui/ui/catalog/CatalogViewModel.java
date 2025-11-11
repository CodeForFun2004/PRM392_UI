package com.example.chillcup02_ui.ui.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.data.repository.CatalogRepository;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.domain.usecase.LoadCatalogUseCase;

import java.util.List;

public class CatalogViewModel extends ViewModel {

    private final LoadCatalogUseCase loadCatalogUseCase;
    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>();

    public CatalogViewModel() {
        CatalogRepository repository = new CatalogRepository();
        this.loadCatalogUseCase = new LoadCatalogUseCase(repository);
    }

    public LiveData<List<Category>> getCategories() {
        return _categories;
    }

    public LiveData<String> getError() {
        return _error;
    }

    public LiveData<Boolean> getLoading() {
        return _loading;
    }

    public void loadCategories() {
        _loading.setValue(true);
        _error.setValue(null);

        loadCatalogUseCase.execute(new CatalogRepository.ResultCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> categories) {
                _loading.setValue(false);
                _categories.setValue(categories);
            }

            @Override
            public void onError(String message) {
                _loading.setValue(false);
                _error.setValue(message);
            }
        });
    }
}

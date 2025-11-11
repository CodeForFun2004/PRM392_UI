package com.example.chillcup02_ui.data.repository;

import com.example.chillcup02_ui.domain.model.Favourite;

public class ToggleResult {
    private Favourite favourite;
    private String status;

    public ToggleResult(Favourite favourite, String status) {
        this.favourite = favourite;
        this.status = status;
    }

    public Favourite getFavourite() {
        return favourite;
    }

    public void setFavourite(Favourite favourite) {
        this.favourite = favourite;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

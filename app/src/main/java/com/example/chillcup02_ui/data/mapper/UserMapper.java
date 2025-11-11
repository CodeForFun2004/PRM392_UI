package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.domain.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserMapper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    public User toDomainModel(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User.UserRole role = User.UserRole.CUSTOMER;
        if (dto.getRole() != null) {
            try {
                role = User.UserRole.valueOf(dto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) { /* default to CUSTOMER */ }
        }

        User.StaffStatus status = null;
        if (dto.getStatus() != null) {
            try {
                status = User.StaffStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) { /* default to null */ }
        }

        Date banExpiresDate = null;
        if (dto.getBanExpires() != null) {
            try {
                banExpiresDate = dateFormat.parse(dto.getBanExpires());
            } catch (ParseException e) { e.printStackTrace(); }
        }

        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getFullname(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAvatar(),
                dto.getAddress(),
                role,
                dto.getStaffId(),
                status,
                dto.isBanned(),
                dto.getBanReason(),
                banExpiresDate,
                dto.getGoogleId(),
                dto.getStoreId()
        );
    }
}

package com.ronex.backend.service;

import java.util.Map;

public interface StaffAuthService {

    Map<String, String> login(String username, String password);
}
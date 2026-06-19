/*
 * package com.ronex.backend.purchase;
 * 
 * import org.springframework.stereotype.Service;
 * 
 * import com.google.api.services.androidpublisher.AndroidPublisher; import
 * com.google.api.services.androidpublisher.model.ProductPurchase;
 * 
 * import lombok.RequiredArgsConstructor;
 * 
 * @Service
 * 
 * @RequiredArgsConstructor public class GooglePlayVerifyService {
 * 
 * private final AndroidPublisher androidPublisher;
 * 
 * private static final String PACKAGE_NAME = "com.ronex.app";
 * 
 * public boolean verify(String productId, String token) { try {
 * AndroidPublisher.Purchases.Products.Get request =
 * androidPublisher.purchases() .products() .get(PACKAGE_NAME, productId,
 * token);
 * 
 * ProductPurchase purchase = request.execute();
 * 
 * // 0 = Purchased return purchase.getPurchaseState() == 0;
 * 
 * } catch (Exception e) { e.printStackTrace(); return false; } } }
 */
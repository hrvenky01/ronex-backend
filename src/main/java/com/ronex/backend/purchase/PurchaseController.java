/*
 * package com.ronex.backend.purchase;
 * 
 * import com.ronex.backend.wallet.WalletService; import
 * lombok.RequiredArgsConstructor; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.*;
 * 
 * @RestController
 * 
 * @RequestMapping("/api/purchase")
 * 
 * @RequiredArgsConstructor public class PurchaseController {
 * 
 * private final GooglePlayVerifyService verifyService; private final
 * WalletService walletService; private final PurchaseRecordRepository
 * purchaseRepo;
 * 
 * @PostMapping("/verify") public ResponseEntity<?> verifyPurchase(
 * 
 * @RequestBody PurchaseRequest req ) {
 * 
 * // 🛑 STEP-1: Duplicate block if
 * (purchaseRepo.existsByPurchaseToken(req.getPurchaseToken())) { return
 * ResponseEntity .badRequest() .body("Purchase already processed"); }
 * 
 * // 🔍 STEP-2: Google verify boolean valid = verifyService.verify(
 * req.getProductId(), req.getPurchaseToken() );
 * 
 * if (!valid) { return ResponseEntity .badRequest()
 * .body("Invalid Play Store purchase"); }
 * 
 * // 💰 STEP-3: Credit wallet walletService.credit( req.getUserId(),
 * req.getCoins(), "PLAY_STORE" );
 * 
 * // 🧾 STEP-4: Save purchase record PurchaseRecord record = new
 * PurchaseRecord(); record.setUserId(req.getUserId());
 * record.setProductId(req.getProductId());
 * record.setPurchaseToken(req.getPurchaseToken());
 * record.setCoins(req.getCoins());
 * 
 * purchaseRepo.save(record);
 * 
 * return ResponseEntity.ok("Coins credited successfully"); } }
 */
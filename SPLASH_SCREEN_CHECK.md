# Kiểm tra Splash Screen

## Layout hiện tại (`activity_splash.xml`):
- ✅ Background: `@color/primary_green` (màu xanh lá #2E7D32)
- ✅ Logo: `@mipmap/ic_launcher` (120x120dp)
- ✅ Text: "ChillCup" màu trắng, 36sp, bold
- ✅ Căn giữa màn hình

## Theme (`Theme.Splash`):
- ✅ Background: `@color/primary_green`
- ✅ Status bar: màu xanh lá, icon trắng
- ✅ Đã set trong AndroidManifest

## Các bước để xác nhận:

1. **Kiểm tra file layout:**
   - File: `app/src/main/res/layout/activity_splash.xml`
   - Nội dung đã đúng với layout mới

2. **Kiểm tra manifest:**
   - File: `app/src/main/AndroidManifest.xml`
   - SplashActivity đã dùng `android:theme="@style/Theme.Splash"`

3. **Clean và rebuild:**
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

4. **Uninstall app cũ:**
   - Gỡ app khỏi thiết bị/emulator
   - Cài lại app mới

5. **Kiểm tra trong Android Studio:**
   - Mở `activity_splash.xml`
   - Xem Preview/Design
   - Nếu preview đúng nhưng app không đúng → cache issue

## Nếu vẫn không đúng:

1. Kiểm tra xem có file layout nào override không:
   - `app/src/dev/res/layout/activity_splash.xml`
   - `app/src/prod/res/layout/activity_splash.xml`

2. Kiểm tra build variants:
   - Đảm bảo đang build đúng variant (dev/prod)

3. Clear all caches:
   - File → Invalidate Caches → Invalidate and Restart
   - Xóa thư mục `.gradle` và `.idea` (nếu cần)


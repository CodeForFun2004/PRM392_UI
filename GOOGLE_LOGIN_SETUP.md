# Hướng dẫn cấu hình Google Login với Firebase

## Bước 1: Đảm bảo google-services.json có Web Client ID

1. Kiểm tra file `app/src/dev/google-services.json` (hoặc `app/src/prod/google-services.json`)
2. Đảm bảo trong phần `oauth_client` có client với `client_type: 3` (Web client)
3. Nếu chưa có, làm theo Bước 2

## Bước 2: Tạo OAuth 2.0 Web Client ID (nếu chưa có)

1. Truy cập [Firebase Console](https://console.firebase.google.com/)
2. Chọn project của bạn (chill-2eaf9)
3. Vào **Project Settings** (⚙️) > **Your apps**
4. Scroll xuống phần **OAuth 2.0 Client IDs**
5. Nếu chưa có Web client (`client_type: 3`), có 2 cách:

   **Cách A: Từ Firebase Console**
   - Firebase sẽ tự động tạo Web client khi bạn enable Google Sign-In
   - Hoặc vào **Google Cloud Console** > **APIs & Services** > **Credentials** > **Create Credentials** > **OAuth 2.0 Client ID** > chọn **Web application**

   **Cách B: Firebase tự động tạo**
   - Khi enable Google Sign-In method trong Firebase Authentication, Firebase sẽ tự động tạo Web client
   - Download lại `google-services.json` từ Firebase Console và thay thế file cũ

## Bước 3: Cập nhật google-services.json

1. Download file `google-services.json` mới nhất từ Firebase Console
2. Thay thế file trong `app/src/dev/google-services.json` (và `app/src/prod/google-services.json` nếu cần)
3. Đảm bảo file có OAuth client với `client_type: 3`:

```json
"oauth_client": [
  {
    "client_id": "xxxxx-xxxxx.apps.googleusercontent.com",
    "client_type": 3  // Đây là Web client ID
  }
]
```

## Bước 4: Web Client ID tự động được sử dụng

✅ **Không cần cấu hình thủ công!**

Google Services plugin sẽ tự động:
- Đọc `google-services.json`
- Tìm OAuth client có `client_type: 3` (Web client)
- Generate `R.string.default_web_client_id` tự động
- Code đã sử dụng `getString(R.string.default_web_client_id)` để lấy Web Client ID

**Lưu ý:** Không cần định nghĩa `default_web_client_id` trong `strings.xml` - nó được generate tự động!

## Bước 5: Kiểm tra cấu hình

Sau khi build project, bạn có thể kiểm tra xem Web Client ID đã được generate chưa:
- File được generate tại: `build/generated/res/google-services/{flavor}/values/values.xml`
- Tìm `default_web_client_id` trong file đó

## Bước 6: Test

1. Build và chạy ứng dụng
2. Mở **TestGoogleLoginActivity** (sẽ là launcher activity mặc định)
3. Click **"Đăng nhập với Google"**
4. Chọn tài khoản Google và cho phép quyền
5. Kiểm tra thông tin người dùng hiển thị

## Lưu ý quan trọng

- ✅ **Đảm bảo đã enable Google Sign-In method** trong Firebase Authentication
- ✅ **Kiểm tra package name** trong Firebase Console phải khớp với `applicationId` trong `build.gradle.kts`
- ✅ **SHA-1 fingerprint** cần được thêm vào Firebase Console (nếu test trên thiết bị thật)
- ✅ **Web Client ID tự động từ google-services.json** - không cần cấu hình thủ công trong `strings.xml`
- ✅ **Đảm bảo google-services.json có OAuth client với `client_type: 3`** (Web client)

## Troubleshooting

### Lỗi: "default_web_client_id not found"
- **Nguyên nhân:** `google-services.json` không có OAuth client với `client_type: 3`
- **Giải pháp:** 
  1. Đảm bảo đã tạo Web client trong Firebase Console
  2. Download lại `google-services.json` từ Firebase Console
  3. Thay thế file trong project
  4. Clean và rebuild project

### Lỗi: "10: Developer Error" khi đăng nhập
- **Nguyên nhân:** Web Client ID không khớp với Firebase project
- **Giải pháp:**
  1. Kiểm tra `google-services.json` có đúng project không
  2. Đảm bảo Web Client ID trong `google-services.json` đúng với Firebase Console
  3. Clean và rebuild project để regenerate resources

## Lấy SHA-1 Fingerprint

### Windows (PowerShell):
```powershell
cd android
./gradlew signingReport
```

Hoặc:
```powershell
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

Copy SHA-1 và thêm vào Firebase Console > Project Settings > Your apps > Android app > Add fingerprint


# ChillCup - Trình tự và Sơ lược về Project

## 📱 Tổng quan

Ứng dụng ChillCup là một ứng dụng đặt đồ uống với các tính năng:
- Đăng nhập/đăng ký bằng Google
- Duyệt sản phẩm và đặt hàng
- Quản lý đơn hàng
- Quản lý thông tin cá nhân

## 🔄 Luồng hoạt động của ứng dụng

### 1. Khởi động ứng dụng (SplashActivity)
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/auth/SplashActivity.java`
- **Chức năng**: 
  - Hiển thị màn hình splash trong 2 giây
  - Kiểm tra trạng thái đăng nhập của user (Firebase Auth)
  - Điều hướng đến MainActivity

### 2. Màn hình chính (MainActivity)
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/MainActivity.java`
- **Chức năng**:
  - Quản lý BottomNavigationView với 4 tabs
  - Khởi tạo AuthViewModel (chia sẻ giữa các fragments)
  - Điều hướng giữa các fragments

### 3. Bottom Navigation - 4 Tabs

#### Tab 1: Home (CatalogFragment)
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/ui/catalog/CatalogFragment.java`
- **Chức năng**:
  - Hiển thị màn hình home
  - Hiển thị card welcome với nút đăng nhập (nếu chưa đăng nhập)
  - Ẩn card welcome khi đã đăng nhập
  - Nút đăng nhập điều hướng đến LoginActivity

#### Tab 2: Menu (CatalogFragment)
- **Hiện tại**: Sử dụng cùng fragment với Home
- **Tương lai**: Có thể tách riêng để hiển thị menu đầy đủ

#### Tab 3: Orders (OrdersFragment)
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/ui/orders/OrdersFragment.java`
- **Chức năng**:
  - **Nếu chưa đăng nhập**: Hiển thị card yêu cầu đăng nhập với nút "Đăng nhập với Google"
  - **Nếu đã đăng nhập**: Hiển thị danh sách đơn hàng (hiện tại hiển thị "Chưa có đơn hàng nào")
  - Quan sát trạng thái đăng nhập từ AuthViewModel

#### Tab 4: Profile (ProfileFragment)
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/ui/profile/ProfileFragment.java`
- **Chức năng**:
  - **Nếu chưa đăng nhập**: Hiển thị card yêu cầu đăng nhập với nút "Đăng nhập với Google"
  - **Nếu đã đăng nhập**: 
    - Hiển thị thông tin user (tên, email)
    - Hiển thị avatar (placeholder)
    - Nút đăng xuất
  - Quan sát trạng thái đăng nhập từ AuthViewModel

### 4. Đăng nhập (LoginActivity)
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/auth/LoginActivity.java`
- **Chức năng**:
  - Hiển thị màn hình đăng nhập với Google Sign-In
  - Xử lý đăng nhập Google với Firebase Auth
  - Sau khi đăng nhập thành công, điều hướng về MainActivity
  - Nếu đã đăng nhập, tự động điều hướng về MainActivity

## 🔐 Quản lý Authentication

### AuthViewModel
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/ui/auth/AuthViewModel.java`
- **Chức năng**:
  - Quản lý trạng thái đăng nhập (LiveData)
  - Cung cấp thông tin user hiện tại (FirebaseUser)
  - Lắng nghe thay đổi trạng thái đăng nhập từ Firebase Auth
  - Xử lý đăng xuất

### TokenProvider
- **Vị trí**: `app/src/main/java/com/example/chillcup02_ui/auth/TokenProvider.java`
- **Hiện tại**: Chưa được implement (có thể dùng để lưu token)

## 🎨 Theme và Styling

### Colors
- **Vị trí**: `app/src/main/res/values/colors.xml`
- **Màu chủ đạo**: Xanh lá (Green) - `#2E7D32`
- **Màu phụ**: Xanh lá nhạt, cam accent

### Strings
- **Vị trí**: `app/src/main/res/values/strings.xml`
- Chứa tất cả các string resources cho ứng dụng

### Themes
- **Vị trí**: `app/src/main/res/values/themes.xml`
- Sử dụng Material3 theme với màu xanh lá làm primary color

## 📂 Cấu trúc thư mục

```
app/src/main/java/com/example/chillcup02_ui/
├── auth/                      # Authentication
│   ├── SplashActivity.java    # Màn hình splash
│   ├── LoginActivity.java     # Màn hình đăng nhập
│   ├── TokenProvider.java     # Token provider (chưa implement)
│   └── AuthInterceptor.java   # Auth interceptor
├── ui/                        # UI Components
│   ├── auth/
│   │   └── AuthViewModel.java # ViewModel quản lý auth
│   ├── catalog/
│   │   └── CatalogFragment.java # Fragment home/menu
│   ├── orders/
│   │   └── OrdersFragment.java  # Fragment orders
│   └── profile/
│       └── ProfileFragment.java # Fragment profile
├── data/                      # Data layer
│   ├── api/                   # API services
│   ├── dto/                   # Data Transfer Objects
│   ├── local/                 # Local database (Room)
│   └── repository/            # Repositories
├── domain/                    # Domain layer
│   ├── model/                 # Domain models
│   └── usecase/               # Use cases
└── MainActivity.java          # Main activity
```

## 🔧 Dependencies chính

- **Firebase Auth**: Xử lý đăng nhập Google
- **Firebase Analytics**: Phân tích sử dụng
- **Material Components**: UI components
- **Lifecycle & ViewModel**: Quản lý lifecycle và state
- **Fragment**: Fragment support
- **Room**: Local database
- **Retrofit**: API calls
- **Glide**: Image loading

## 🚀 Luồng đăng nhập

1. User mở app → SplashActivity
2. SplashActivity kiểm tra trạng thái đăng nhập
3. Điều hướng đến MainActivity
4. MainActivity hiển thị Home tab (CatalogFragment)
5. Nếu chưa đăng nhập:
   - Home tab hiển thị card welcome với nút đăng nhập
   - Orders tab hiển thị yêu cầu đăng nhập
   - Profile tab hiển thị yêu cầu đăng nhập
6. User click nút đăng nhập → LoginActivity
7. User đăng nhập bằng Google → Firebase Auth xử lý
8. Sau khi đăng nhập thành công:
   - LoginActivity điều hướng về MainActivity
   - AuthViewModel cập nhật trạng thái
   - Các fragments tự động cập nhật UI:
     - Home: Ẩn card welcome
     - Orders: Hiển thị danh sách đơn hàng
     - Profile: Hiển thị thông tin user

## 📝 Lưu ý

1. **Google Sign-In**: Cần cấu hình `google-services.json` đúng cách
2. **Firebase Auth**: Đảm bảo đã enable Google Sign-In trong Firebase Console
3. **default_web_client_id**: Được tự động generate từ `google-services.json`
4. **AuthViewModel**: Được chia sẻ giữa các fragments thông qua MainActivity
5. **Bottom Navigation**: Sử dụng Material BottomNavigationView với 4 tabs

## 🎯 Các tính năng đã hoàn thành

✅ Splash screen với kiểm tra đăng nhập
✅ MainActivity với BottomNavigationView
✅ 4 tabs: Home, Menu, Orders, Profile
✅ LoginActivity với Google Sign-In
✅ AuthViewModel quản lý authentication state
✅ OrdersFragment với kiểm tra đăng nhập
✅ ProfileFragment với kiểm tra đăng nhập và hiển thị thông tin user
✅ CatalogFragment (Home) với card welcome
✅ Theme và styling nhất quán

## 🔜 Các tính năng cần phát triển

- [ ] Hiển thị danh sách sản phẩm trong CatalogFragment
- [ ] Tích hợp API để load đơn hàng
- [ ] Tích hợp API để load thông tin user
- [ ] Thêm tính năng chỉnh sửa profile
- [ ] Thêm tính năng xem chi tiết đơn hàng
- [ ] Thêm tính năng đặt hàng
- [ ] Thêm tính năng giỏ hàng


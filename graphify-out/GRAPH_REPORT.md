# Graph Report - Ecommerce  (2026-09-01)

## Corpus Check
- Corpus is ~28,631 words - fits in a single context window. You may not need a graph.

## Summary
- 456 nodes · 851 edges · 27 communities (25 shown, 2 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 9 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Core Subsystem 0
- Checkout & Native Payment Processing
- Checkout & Native Payment Processing
- Product Catalog & Dynamic Admin Management
- Checkout & Native Payment Processing
- Core Subsystem 5
- Core Subsystem 6
- Product Catalog & Dynamic Admin Management
- Authentication & User Management
- Product Catalog & Dynamic Admin Management
- Product Catalog & Dynamic Admin Management
- Product Catalog & Dynamic Admin Management
- Authentication & User Management
- Checkout & Native Payment Processing
- Product Catalog & Dynamic Admin Management
- Checkout & Native Payment Processing
- Authentication & User Management
- Core Subsystem 17
- Core Subsystem 18
- Core Subsystem 19

## God Nodes (most connected - your core abstractions)
1. `EcommerceApiService` - 29 edges
2. `Product` - 29 edges
3. `formatRupiah()` - 27 edges
4. `AdminViewModel` - 26 edges
5. `CartItem` - 25 edges
6. `Order` - 24 edges
7. `CheckoutViewModel` - 19 edges
8. `OrderStatus` - 18 edges
9. `ProductRepository` - 17 edges
10. `EcommerceNavHost()` - 16 edges

## Surprising Connections (you probably didn't know these)
- `AdminDashboardScreen()` --calls--> `ModernProductFormSheet()`  [INFERRED]
  app/src/main/java/com/ecommerce/ui/admin/AdminDashboardScreen.kt → app/src/main/java/com/ecommerce/ui/admin/ModernProductFormSheet.kt
- `CheckoutScreen()` --calls--> `InAppLocationPickerSheet()`  [INFERRED]
  app/src/main/java/com/ecommerce/ui/checkout/CheckoutScreen.kt → app/src/main/java/com/ecommerce/ui/checkout/InAppLocationPickerSheet.kt
- `AppContainer` --references--> `OrderRepository`  [EXTRACTED]
  app/src/main/java/com/ecommerce/data/AppContainer.kt → app/src/main/java/com/ecommerce/domain/repository/OrderRepository.kt
- `AppContainer` --references--> `ProductRepository`  [EXTRACTED]
  app/src/main/java/com/ecommerce/data/AppContainer.kt → app/src/main/java/com/ecommerce/domain/repository/ProductRepository.kt
- `DefaultAppContainer` --calls--> `OrderRepositoryImpl`  [EXTRACTED]
  app/src/main/java/com/ecommerce/data/AppContainer.kt → app/src/main/java/com/ecommerce/data/repository/OrderRepositoryImpl.kt

## Import Cycles
- None detected.

## Communities (27 total, 2 thin omitted)

### Community 0 - "Core Subsystem 0"
Cohesion: 0.06
Nodes (26): AppContainer, DefaultAppContainer, AuthRepositoryImpl, Flow, Result, CartRepositoryImpl, Flow, User (+18 more)

### Community 1 - "Checkout & Native Payment Processing"
Cohesion: 0.08
Nodes (22): MidtransApiService, BankTransferRequest, CoreChargeRequest, CoreChargeResponse, CustomerDetails, ItemDetails, QrisAction, QrisRequest (+14 more)

### Community 2 - "Checkout & Native Payment Processing"
Cohesion: 0.09
Nodes (19): OrderItemDto, Flow, Result, OrderRepositoryImpl, Order, OrderStatus, COMPLETED, PENDING (+11 more)

### Community 3 - "Product Catalog & Dynamic Admin Management"
Cohesion: 0.09
Nodes (20): NetworkClient, ProductCategory, DRIED_SPICE, HERBAL_EXTRACT, PURE_OIL, Modifier, ProductCard(), Modifier (+12 more)

### Community 4 - "Checkout & Native Payment Processing"
Cohesion: 0.07
Nodes (4): OrderDto, ProductDto, ProductVariantDto, EcommerceApiService

### Community 5 - "Core Subsystem 5"
Cohesion: 0.10
Nodes (9): CartItem, Flow, CheckoutUiState, Content, ShippingOption, CheckoutViewModel, StateFlow, ViewModel (+1 more)

### Community 6 - "Core Subsystem 6"
Cohesion: 0.09
Nodes (21): CartUiState, Loading, Success, CartViewModel, StateFlow, ViewModel, Error, Loading (+13 more)

### Community 7 - "Product Catalog & Dynamic Admin Management"
Cohesion: 0.13
Nodes (18): AddCategoryDialog(), AddShippingDialog(), AddVoucherDialog(), AdminDashboardScreen(), AdminOrderItem(), AdminProductItem(), Modifier, CartScreen() (+10 more)

### Community 8 - "Authentication & User Management"
Cohesion: 0.12
Nodes (15): UserRole, ADMIN, CUSTOMER, MainActivity, NavHostController, MainScreen(), BottomNavItem, ACCOUNT (+7 more)

### Community 9 - "Product Catalog & Dynamic Admin Management"
Cohesion: 0.16
Nodes (5): Result, ProductRepositoryImpl, Product, Result, ProductRepository

### Community 10 - "Product Catalog & Dynamic Admin Management"
Cohesion: 0.13
Nodes (11): ProductVariant, AddToCartSuccessDialog(), DetailProdukScreen(), Modifier, DetailProductViewModel, StateFlow, ViewModel, DetailProdukUiState (+3 more)

### Community 11 - "Product Catalog & Dynamic Admin Management"
Cohesion: 0.18
Nodes (4): AdminUiState, AdminViewModel, StateFlow, ViewModel

### Community 12 - "Authentication & User Management"
Cohesion: 0.16
Nodes (14): AuthUiState, AuthViewModel, StateFlow, ViewModel, Modifier, LoginScreen(), Modifier, RegisterScreen() (+6 more)

### Community 13 - "Checkout & Native Payment Processing"
Cohesion: 0.20
Nodes (11): CheckoutScreen(), Modifier, InAppLocationPickerSheet(), Modifier, OrderSuccessCelebration(), MidtransSnapWebViewDialog(), WebViewClient, NativePaymentBottomSheet() (+3 more)

### Community 14 - "Product Catalog & Dynamic Admin Management"
Cohesion: 0.14
Nodes (12): Account, AdminDashboard, Cart, Checkout, DetailProduk, Katalog, Login, Onboarding (+4 more)

### Community 15 - "Checkout & Native Payment Processing"
Cohesion: 0.33
Nodes (5): PaymentCategory, BANK_TRANSFER, E_WALLET, VIRTUAL_ACCOUNT, PaymentMethod

### Community 16 - "Authentication & User Management"
Cohesion: 0.50
Nodes (3): AuthRequest, AuthResponse, UserDto

### Community 17 - "Core Subsystem 17"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **47 isolated node(s):** `VaNumber`, `QrisAction`, `AuthRequest`, `AuthResponse`, `UserDto` (+42 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **2 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `EcommerceApiService` connect `Checkout & Native Payment Processing` to `Core Subsystem 0`, `Product Catalog & Dynamic Admin Management`, `Checkout & Native Payment Processing`, `Product Catalog & Dynamic Admin Management`?**
  _High betweenness centrality (0.106) - this node is a cross-community bridge._
- **Why does `CartItem` connect `Core Subsystem 5` to `Core Subsystem 0`, `Checkout & Native Payment Processing`, `Checkout & Native Payment Processing`, `Core Subsystem 6`, `Product Catalog & Dynamic Admin Management`, `Checkout & Native Payment Processing`?**
  _High betweenness centrality (0.100) - this node is a cross-community bridge._
- **Why does `AdminViewModel` connect `Product Catalog & Dynamic Admin Management` to `Checkout & Native Payment Processing`, `Authentication & User Management`, `Core Subsystem 6`, `Product Catalog & Dynamic Admin Management`?**
  _High betweenness centrality (0.088) - this node is a cross-community bridge._
- **What connects `VaNumber`, `QrisAction`, `AuthRequest` to the rest of the system?**
  _47 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Core Subsystem 0` be split into smaller, more focused modules?**
  _Cohesion score 0.05727644652250146 - nodes in this community are weakly interconnected._
- **Should `Checkout & Native Payment Processing` be split into smaller, more focused modules?**
  _Cohesion score 0.0796221322537112 - nodes in this community are weakly interconnected._
- **Should `Checkout & Native Payment Processing` be split into smaller, more focused modules?**
  _Cohesion score 0.09309309309309309 - nodes in this community are weakly interconnected._
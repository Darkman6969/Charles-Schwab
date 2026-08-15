# Brokerage App Implementation Plan

This plan outlines the steps to build a high-fidelity brokerage and wealth management app using Jetpack Compose, Clean Architecture, and Hilt.

## User Review Required

> [!IMPORTANT]
> - **Architecture**: We will follow Clean Architecture (Data -> Domain -> UI). This ensures scalability and testability.
> - **Dependency Injection**: Hilt will be used for DI. This requires an `Application` class and some boilerplate setup.
> - **Navigation**: We will use Jetpack Compose Navigation with **Type-Safe Routes** (Kotlin Serialization), which is the current best practice.
> - **Charting**: We will integrate **Vico**, a modern, Compose-first charting library, for the stock charts.

## Proposed Changes

### Configuration & Dependencies

#### [MODIFY] [libs.versions.toml](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/gradle/libs.versions.toml)
Add versions and library definitions for:
- Hilt (DI)
- Navigation Compose
- Kotlin Serialization (for Type-Safe Navigation)
- Vico (Charting)
- ViewModel & Lifecycle utilities

#### [MODIFY] [build.gradle.kts (App)](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/build.gradle.kts)
Apply Hilt and Serialization plugins. Add the new dependencies.

---

### Design System & Theme

#### [MODIFY] [Color.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/ui/theme/Color.kt)
Define the primary "Schwab Navy", success green, and error red color tokens.

#### [MODIFY] [Theme.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/ui/theme/Theme.kt)
Configure the `MaterialTheme` with the new color palette and typography.

---

### Domain Layer (Models & Repository Interfaces)

#### [NEW] [Models.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/domain/model/Models.kt)
Define `StockQuote`, `Account`, `Holding`, `OrderTicket`, and `ChartPoint`.

#### [NEW] [StockRepository.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/domain/repository/StockRepository.kt)
Interface for fetching stock data and accounts.

---

### Data Layer (Mock Implementation)

#### [NEW] [MockStockRepository.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/data/repository/MockStockRepository.kt)
Implement `StockRepository` using Kotlin `Flow` to emit realistic price variations every 2-3 seconds.

---

### UI Layer (Components & Screens)

#### [NEW] [Navigation.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/ui/navigation/Navigation.kt)
Define the type-safe routes and the `NavHost`.

#### [NEW] [DashboardScreen.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/ui/dashboard/DashboardScreen.kt)
The "Home" tab with account overview and asset allocation.

#### [NEW] [StockDetailScreen.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/ui/quote/StockDetailScreen.kt)
The stock quote screen featuring the interactive Vico chart.

#### [NEW] [TradeOrderBottomSheet.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/ui/trade/TradeOrderBottomSheet.kt)
The modal bottom sheet for buy/sell orders with swipe-to-confirm.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/MUIGAI/AndroidStudioProjects/CharlesSchwab/app/src/main/java/com/example/charlesschwab/MainActivity.kt)
Initialize Hilt and set up the main navigation entry point.

## Verification Plan

### Automated Tests
- Unit test for `MockStockRepository` to ensure price updates are emitted correctly.
- Unit test for `OrderViewModel` (to be created) to verify cost calculations.

### Manual Verification
- Launch the app and verify the splash screen.
- Observe the "Home" tab for shimmer effects and real-time ticker updates.
- Interact with the "Stock Detail" chart (scrubbing to see price at point).
- Execute a mock trade and verify the success animation and balance update.

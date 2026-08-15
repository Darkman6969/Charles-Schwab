# Charles Schwab - Wealth Management & Brokerage App

A high-fidelity, production-quality Android brokerage application built with Jetpack Compose, targeting a professional financial software aesthetic. This project follows Clean Architecture principles and modern Android development best practices.

## 🚀 Features

### 1. Enterprise-Grade Authentication
* **Login Gate**: Mandatory authentication screen with "Schwab Navy" branding.
* **Biometrics**: Simulated BiometricPrompt (Fingerprint/Face Unlock) for quick access.
* **Pre-login Features**: Links for account opening, security guarantees, and privacy policies.

### 2. High-Density Financial Dashboard (Summary)
* **Account Overview**: Masked account IDs (`*1234`) and real-time total balance calculation.
* **Market Status**: Live indicators for Market Open/Closed/After Hours states.
* **Compliance**: Integrated 10sp micro-text financial disclaimers and SIPC/FDIC notifications.

### 3. Technical Charting (Custom Canvas)
* **High-Performance Candlesticks**: Custom-built `Canvas` implementation for dense historical data.
* **Volume Analysis**: Integrated volume bar charts synchronized with price action.
* **Interactive Crosshairs**: Long-press gesture detection for precise OHLC (Open, High, Low, Close) and Volume data inspection.

### 4. Advanced Trade Ticket
* **Multi-Step Execution**: Support for BUY, SELL, SELL SHORT, and BUY TO COVER.
* **Order Complexity**: Market, Limit, Stop, Stop Limit, and Trailing Stop order types.
* **Professional UI**: Tabular numerals (`tnum`) to prevent jitter, sharp 2dp corners, and dense 1dp dividers.
* **Order Verification**: Strict verification dialog with trade summaries and legal disclosures.

### 5. Architecture & Tech Stack
* **Language**: 100% Kotlin.
* **UI**: Jetpack Compose with Material Design 3 (Customized for enterprise density).
* **Architecture**: Clean Architecture (Domain, Data, UI) + MVVM.
* **DI**: Hilt for Dependency Injection.
* **Navigation**: Type-safe Jetpack Navigation with Kotlin Serialization.
* **Async**: Kotlin Coroutines and Flow for real-time mock data streaming.

## 🛠️ Project Structure
```text
com.example.charlesschwab
├── data
│   └── repository      # MockStockRepository implementation with Flow-based updates
├── di                  # Hilt modules (RepositoryModule)
├── domain
│   ├── model           # Financial models (StockQuote, Account, OrderTicket, etc.)
│   └── repository      # Domain repository interfaces
└── ui
    ├── auth            # Login and Biometric logic
    ├── components      # Reusable UI components (DisclaimerFooter, etc.)
    ├── navigation      # Type-safe navigation definitions
    ├── quote           # Stock Detail and Custom Technical Charts
    ├── summary         # Account Summary and Dashboard
    ├── theme           # Brand colors (Schwab Navy), Typography, and MaterialTheme
    └── trade           # Advanced Trade Ticket and Order Review flow
```

## 📋 Requirements
* **Android Studio**: Ladybug (2024.2.1) or newer.
* **Minimum SDK**: 24.
* **Target SDK**: 35.
* **Kotlin**: 2.0.21.

## 🚦 Getting Started
1. Clone the repository: `git clone https://github.com/Darkman6969/Charles-Schwab.git`
2. Open the project in Android Studio.
3. Sync Gradle and run the `app` module on an emulator or physical device.

---
*Disclaimer: This is a university mobile development project. All financial data is simulated/mocked. Not affiliated with Charles Schwab & Co., Inc.*

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

**Build the project:**
```bash
./gradlew assembleDebug    # Build debug APK
./gradlew assembleRelease  # Build release APK
```

**Install and run:**
```bash
./gradlew installDebug     # Install debug APK to connected device
```

**Gradle wrapper:**
- Uses Gradle 8.13 with Kotlin DSL
- Distribution mirror: `https://mirrors.cloud.tencent.com/gradle/gradle-8.13-bin.zip`

## Architecture Overview

This is an **Xposed module** for Android that provides granular control over location services. The project has two main components:

### 1. Xposed Hook System (`app/src/main/java/fuck/location/xposed/`)
The core hooking infrastructure that intercepts Android system calls:

- **HookEntry.kt**: Main entry point that initializes all hooks based on Android version
- **Location Hookers**: Version-specific location API hooks
  - `LocationHookerPreQ.kt` - Android pre-Q (API 28)
  - `LocationHookerR.kt` - Android R (API 30)
  - `LocationHookerAfterS.kt` - Android S+ (API 31+)
- **GNSS Hookers**: GPS/GNSS system level hooks
  - `GnssHookerPreQ.kt`
  - `GnssManagerServiceHookerR.kt`
  - `GnssManagerServiceHookerS.kt`
- **OEM-Specific Hookers**: Manufacturer customizations
  - `miui/` - MIUI location manager hooks
  - `oplus/` - OnePlus/Oppo location hooks
- **Cellar Hooks**: Telephony/cellular information hooks
  - `PhoneInterfaceManagerHooker.kt`
  - `TelephonyRegistryHooker.kt`

### 2. Android App UI (`app/src/main/java/fuck/location/app/`)
The user interface for configuring the module:

- **MainActivity.kt**: Main configuration screen for setting fake coordinates
- **ModuleActivity.kt**: App selection screen with search functionality
- **AboutActivity.kt**: About and information screen

### 3. Configuration System (`app/src/main/java/fuck/location/xposed/helpers/`)

- **ConfigGateway.kt**: Central configuration management between UI and hooks
- **Workaround classes**: Device-specific compatibility fixes
  - `Miui.kt` - MIUI-specific workarounds
  - `Oplus.kt` - OnePlus/Oppo workarounds

## Key Technical Details

**Xposed Scope Configuration:**
- Target packages defined in `app/src/main/res/values/arrays.xml`
- Currently hooks: `android` and `com.android.phone`
- Manifest metadata enables Xposed module functionality

**Hook Strategy:**
- Version-specific hookers are conditionally loaded in `HookEntry.kt`
- Uses EzXHelper library for simplified Xposed development
- HiddenApiBypass for accessing restricted Android APIs

**UI Architecture:**
- ViewBinding for type-safe view access
- MaterialDialogs for settings UI
- OneAdapter for efficient list rendering
- Multi-language support (English, Chinese, Vietnamese)

**Build Configuration:**
- Kotlin DSL build system (Gradle 8.13)
- Target SDK 36, Min SDK 31 (Android 12+)
- Java 17 compatibility
- LSPosed API 82 (compile-only)

## Development Notes

- **Null Safety**: The codebase uses Kotlin, ensure proper null safety handling
- **Android Versions**: Hooks are version-specific - always check Android version compatibility
- **OEM Compatibility**: Different manufacturers have custom location implementations requiring specific hookers
- **Configuration**: UI changes require updating `ConfigGateway.kt` to persist settings
- **Testing**: Test on actual devices with Xposed framework installed (LSPosed recommended)

## Security Context

This is a legitimate Xposed module for location control - it requires:
- Xposed framework access
- System-level hooking capabilities
- Access to location APIs and telephony services
- QUERY_ALL_PACKAGES permission for app enumeration
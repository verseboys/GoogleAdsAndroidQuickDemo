# Google Ads Android Quick Demo

[![Build](https://github.com/verseboys/GoogleAdsAndroidQuickDemo/actions/workflows/build.yml/badge.svg)](https://github.com/verseboys/GoogleAdsAndroidQuickDemo/actions/workflows/build.yml)
[![Release](https://github.com/verseboys/GoogleAdsAndroidQuickDemo/actions/workflows/release.yml/badge.svg)](https://github.com/verseboys/GoogleAdsAndroidQuickDemo/releases)

一个简洁的 Android 单页 Demo，演示 Google AdMob 三种主要广告类型的集成。

## 📱 功能展示

| 广告类型 | 说明 | 测试广告 ID |
|---------|------|------------|
| **Banner** | 横幅广告，固定在底部 | `ca-app-pub-3940256099942544/6300978111` |
| **Interstitial** | 插屏广告，全屏展示 | `ca-app-pub-3940256099942544/1033173712` |
| **Rewarded** | 激励视频，观看获奖励 | `ca-app-pub-3940256099942544/5224354917` |

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/verseboys/GoogleAdsAndroidQuickDemo.git
```

### 2. 用 Android Studio 打开
- File → Open → 选择项目目录
- 等待 Gradle 同步完成

### 3. 运行
- 连接 Android 设备或启动模拟器
- 点击 Run

## 📋 项目结构

```
app/
├── src/main/
│   ├── java/com/example/googleadsdemo/
│   │   └── MainActivity.java      # 主界面，包含所有广告逻辑
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml  # 布局文件
│   │   └── values/
│   │       ├── strings.xml
│   │       └── themes.xml
│   └── AndroidManifest.xml        # 配置文件，包含 AdMob App ID
└── build.gradle                    # 依赖配置
```

## ⚠️ 注意事项

### 测试广告 ID
本项目使用 **Google 官方测试广告 ID**，可直接运行测试。

### 正式上线前
1. 在 [AdMob 控制台](https://admob.google.com) 创建账号
2. 注册应用并创建广告单元
3. 替换以下 ID：
   - `AndroidManifest.xml` 中的 `APPLICATION_ID`
   - `MainActivity.java` 中的 `BANNER_AD_UNIT_ID`
   - `MainActivity.java` 中的 `INTERSTITIAL_AD_UNIT_ID`
   - `MainActivity.java` 中的 `REWARDED_AD_UNIT_ID`
   - `activity_main.xml` 中的 `adUnitId`

### 重要
🚫 **绝对不要用测试广告 ID 上线正式应用！**

## 📦 依赖

```gradle
// Google Mobile Ads SDK
implementation 'com.google.android.gms:play-services-ads:22.6.0'
```

## 🔄 CI/CD

项目使用 GitHub Actions 自动构建和发布。

### 自动构建 (CI)
每次 push 到 `main`/`develop` 分支或 PR 时自动触发：
- 构建 Debug APK
- 构建 Release APK
- 上传到 Artifacts (保留 7 天)

### 自动发布 (CD)
打 tag 时自动发布到 GitHub Releases：
```bash
git tag v1.0.0
git push origin v1.0.0
```

### 配置签名 (可选)
在仓库 Settings → Secrets 添加以下密钥以签名 Release APK：

| Secret | 说明 |
|--------|------|
| `KEYSTORE_BASE64` | keystore 文件的 base64 编码 |
| `KEYSTORE_PASSWORD` | keystore 密码 |
| `KEY_ALIAS` | key 别名 |
| `KEY_PASSWORD` | key 密码 |

生成 base64：
```bash
base64 -i your-release.keystore | pbcopy  # macOS
base64 your-release.keystore | xclip      # Linux
```

## 📚 参考文档

- [Google AdMob 官方文档](https://developers.google.com/admob/android/quick-start)
- [Banner 广告实现指南](https://developers.google.com/admob/android/banner)
- [插屏广告实现指南](https://developers.google.com/admob/android/interstitial)
- [激励视频实现指南](https://developers.google.com/admob/android/rewarded)

## 📄 License

MIT License

## 设置debug和release版本不同的过期时间
### 使用buildConfigField设置BuildTimestamp

>参考链接：https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code

* 在build.gradle中添加

```groovy
android {
    ...
    defaultConfig {
        def now = (long) (System.currentTimeMillis() / 1000)
        //use git committer date(UNIX timestamp) to set buildTimestamp
        buildConfigField "Long", "BuildTimestamp",
                "${getStdout(['git', 'log', '-n', '1', '--format=%ct'], now)}000L"
    }
    ...
}
```

* 在`app/build/generated/source/buildConfig/debug/com/example/vera/expirytest/BuildConfig.java`中可以看到生成的

```java
// Fields from default config.
public static final Long BuildTimestamp = 1537325758000L;
```

* 在代码中使用BuildTimestamp设置过期时间

```java
boolean IS_DEBUG_BUILD = BuildConfig.DEBUG;
//Debug expire after 90 days. Final release builds expire after 292 million years.
long EXPIRY_DATE = IS_DEBUG_BUILD ? BuildConfig.BuildTimestamp + 90 * 24 * 60 * 60 * 1000L : Long.MAX_VALUE;
```
BuildConfig.DEBUG是自动生成的，可以在`app/build/generated/source/buildConfig/debug/com/example/vera/expirytest/BuildConfig.java`中看到

### 设置debug版本和release版本的不同包名，不同app_name；使得手机上可以同时安装debug版本和release版本

* 设置不同的包名

```groovy
android {
    compileSdkVersion 26
    ...
    defaultConfig {
        applicationId "com.example.vera.expirytest"
        ...
    }
    buildTypes {
        release{
            ...
        }
        debug{
            //add build config field, find in app/build/generated/source/buildConfig/debug/com/example/vera/gradlebuildconfig/BuildConfig.java
            applicationIdSuffix ".debug"
        }
    }
}
```
在debug模块中使用`applicationIdSuffix`设置debug版本的包名后缀
所以debug版本的包名是`com.example.vera.expirytest.debug`,release版本的后缀是`com.example.vera.expirytest`

* 设置不同的app_name

```groovy
android {
    compileSdkVersion 26
    ...
    defaultConfig {
        //set app_name
        resValue "string", "app_name", "Briar"
        ...
    }
    buildTypes {
        release{
            ...
        }
        debug{
            //set debug app_name(use different name for debug and release,这样可以同时安装两个版本)
            resValue "string", "app_name", "Briar Debug"
        }
    }
}
```

在strings.xml中屏蔽app_name字段

```xml
<resources>
    <!--<string name="app_name">ExpiryTest</string>-->
</resources>
```
因为在AndroidManifest.xml中
`android:label="@string/app_name"`
所以debug版本的app_name是`Briar Debug`，在release版本中的app_name是`Briar`


### 测试

```java
Log.i("EXPIRY", "days before expiry:" + daysBeforeExpiry);
```
安装上之后可以看到release版本打印出的expiry day是一个很大的数，debug版本打印出的expiry day是一个小于90的数。
超过该过期时间，会直接跳转到`ExpiryActivity`


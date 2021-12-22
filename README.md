# Currency-Converter Android Kotlin

For this project i have used www.currencyconverterapi.com Api which is free for limited use, get your key and in the local.properties

```
KEY = **************
```

Later add the Api Key in the meta data
```
<meta-data
android:name="keyValue"
android:value="${KEY}"/>
```            

And you can access it in the main as shown below

```
val ai: ApplicationInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
val key = ai.metaData["keyValue"].toString()
```


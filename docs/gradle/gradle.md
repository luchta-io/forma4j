# Gradle

forma4jをGradleと組み合わせる方法について説明します。

forma4jは[Maven Central](https://central.sonatype.com/artifact/io.luchta/forma4j)で利用可能なので、Gradleでダウンロードすることができます。また、任意のビルドツールを使用することが可能です。

Gradleと組み合わせる場合、`build.gradle` は以下のようになります。

Gradle

```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation group: 'io.luchta', name: 'forma4j', version: '1.5.0', classifier: 'all'
}
```

Gradle (short)

```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation 'io.luchta:forma4j:1.5.0:all'
}
```

# forma4j

`forma4j` はExcelの読み書きを行うライブラリです。

## 概要

`forma4j` は XML で記述された設定ファイルに従って EXCEL の読み書きを行います。行数、列数が不定である EXCEL の読み書きの設定が簡単に行えるように設計されています。設定ファイルが XML であるため Git などのバージョン管理システムによる差分管理が容易に行えます。

## 使い方

以下のドキュメントを参照してください。

- [はじめに](docs/README.md)
- [Gradle](docs/gradle/gradle.md)
- Writer
  - [XML](docs/writer/xml.md)
  - [formaタグ](docs/writer/forma.md)
  - [sheetタグ](docs/writer/sheet.md)
  - [rowタグ](docs/writer/row.md)
  - [columnタグ](docs/writer/column.md)
  - [cellタグ](docs/writer/cell.md)
  - [vertical-forタグ](docs/writer/vertical-for.md)
  - [horizontal-forタグ](docs/writer/horizontal-for.md)
  - [style](docs/writer/style.md)
- Reader
  - [XML](docs/reader/xml.md)
  - [forma-readerタグ](docs/reader/forma-reader.md)
  - [sheetタグ](docs/reader/sheet.md)
  - [cellタグ](docs/reader/cell.md)
  - [v-forタグ](docs/reader/v-for.md)
  - [h-forタグ](docs/reader/h-for.md)
- Java
  - [Java](docs/java/java.md)

以下は簡単な使い方の例です。

### Gradle

Gradle

```gradle
implementation group: 'io.luchta', name: 'forma4j', version: '1.8.2'
```

Gradle (short)

```gradle
implementation 'io.luchta:forma4j:1.8.2'
```

[Maven Repository](https://mvnrepository.com/artifact/io.luchta/forma4j)

### 設定ファイル (Writer)

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="sheetName">
    <list
      startRowIndex="2"
      startColumnIndex="1"
      headerStyle="border:thin"
      detailStyle="border:thin"
    />
  </sheet>
</forma>
```

### Java (Writer)

```java
package example;

import io.luchta.forma4j.context.databind.json.JsonNode;
import io.luchta.forma4j.context.databind.json.JsonNodes;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.Writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class main {

    public static void main(String[] args) throws Exception {
        JsonNodes jsonNodes = new JsonNodes();

        JsonNode jsonNode1 = new JsonNode();
        jsonNode1.putVar("col1", new JsonObject("value1-1"));
        jsonNode1.putVar("col2", new JsonObject("value1-2"));
        jsonNodes.add(jsonNode1);

        JsonNode jsonNode2 = new JsonNode();
        jsonNode2.putVar("col1", new JsonObject("value2-1"));
        jsonNode2.putVar("col2", new JsonObject("value2-2"));
        jsonNodes.add(jsonNode2);

        ClassLoader classLoader = main.class.getClassLoader();
        InputStream in = classLoader.getResource("writer/example.xml").openStream();
        File outFile = new File("example.xlsx");
        FileOutputStream out = new FileOutputStream(outFile);

        JsonNode jsonNode = new JsonNode();
        jsonNode.putVar("list", new JsonObject(jsonNodes));

        FormaWriter writer = new FormaWriter();
        writer.write(in, out, new JsonObject(jsonNode));
    }
}
```

### 設定ファイル (Reader)

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet name="sheetName">
    <list 
      headerStartRow="4"
      headerStartCol="1"
      detailStartRow="5"
      detailStartCol="1"
      name="list"
    />
  </sheet>
</forma-reader>
```

### Java  (Reader)

```java
package example;

import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;

import java.io.FileInputStream;
import java.io.InputStream;

public class main {

    public static void main(String[] args) throws Exception {
        try (InputStream xml = new FileInputStream(main.class.getClassLoader().getResource("reader/example.xml").getPath());
             InputStream excel = new FileInputStream(main.class.getClassLoader().getResource("reader/example.xlsx").getPath());) {
            FormaReader formaReader = new FormaReader();
            JsonObject obj = formaReader.read(xml, excel);
            JsonSerializer serializer = new JsonSerializer();
            String json = serializer.serializeFromJsonObject(obj);
            System.out.println(json);
        }
    }
}
```

## ライセンス

[Apache License 2.0](/LICENSE)

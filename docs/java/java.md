# Java

Forma4jはXMLで記述された定義に従ってExcelファイルの読み書きを行います。JavaによるExcelファイルの読み書きの方法です。

## JsonObject・JsonNode・JsonNodes

Forma4jは `JsonNode` `JsonNodes` `JsonObject` クラスで表現されたJSON形式のデータを使用してExcelファイルの読み書きを行います。

`JsonNode` はオブジェクトを表現します。

```json
{
  "key1": "value1",
  "key2": "value2"
}
```

`JsonNodes` は配列を表現します。

```json
[
  {
    "key1": "value1-1",
    "key2": "value1-2"
  },
  {
    "key1": "value2-1",
    "key2": "value2-2"
  }
]
```

`JsonObject` は `JsonNode` や `JsonNodes` をセットするためのオブジェクトです。

これらのクラスの使用方法やXMLとの関連は以下の場所にあるテストコードを参照してください。

- 書き込み: `src/test/java/io/luchta/forma4j/writer/FormaWriteTest.java`
- 読み込み: `src/test/java/io/luchta/forma4j/reader/FormaReaderTest.java`
- XML: `src/test/resources`

## JSON形式の文字列をJsonObjectに変換する

Forma4jにはJSON形式の文字列を `JsonNode` などに変換する機能が用意されています。`Jackson` などのJsonパーサーライブラリを使用してJavaのオブジェクトをJsonに変換して使用してください。

```java
import io.luchta.forma4j.context.databind.convert.JsonDeserializer;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.writer.FormaWriter;

public class FormaWriterExample {
  public void example() {
    String json = "{ \"key1\": \"value1\", \"key2\": \"value2\" }";
    JsonDeserializer deserializer = new JsonDeserializer();
    JsonObject jsonObject = deserializer.deserializeFromJson(json);

    FormaWriter writer = new FormaWriter();
    writer.write("/example.xml", "/output.xlsx", jsonObject);
  }
}
```

## JsonObjectを文字列に変換する

Forma4jには `JsonObject` をJSON形式の文字列に変換する機能が用意されています。変換後の文字列を `Jackson` などのJsonパーサーライブラリを使用してJavaのオブジェクトに変換する際に使用してください。

```java
import io.luchta.forma4j.context.databind.convert.JsonSerializer;
import io.luchta.forma4j.context.databind.json.JsonObject;
import io.luchta.forma4j.reader.FormaReader;

public class FormaReaderExample {
  public void example() {
    FormaReader formaReader = new FormaReader();
    JsonObject jsonObject = formaReader.read("/example.xml", "/input.xlsx");

    JsonSerializer serializer = new JsonSerializer();
    String json = serializer.serializeFromJsonObject(jsonObject);
  }
}
```

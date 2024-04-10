# XML

このページではforma4jを利用してExcelファイルを読み込む方法を説明します。

```json
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet name="Example">
    <v-for from="0" name="data">
      <cell col="0" name="key" />
      <cell col="1" name="value" />
    </v-for>
  </sheet>
</forma-reader>
```

このXMLファイルで読み込むExcelファイルです。

![excel](image/reader-xml-1.svg)

読み込んだ結果はこのようになります。

```json
{
  "forma" : {
    "Example" : {
      "data" : [
        {
          "key" : "key1", 
          "value" : "value1"
        },
        {
          "key" : "key2", 
          "value" : "value2"
        },
        {
          "key" : "key3", 
          "value" : "value3"
        }
      ]
    }
  }
}
```

XMLファイルには以下のタグを使用することができます。これらのタグを組み合わせることでExcel読み込み定義を作成します。

| タグ | 役割 |
| --- | --- |
| forma-reader | ルートタグです。 |
| sheet | Excelのシートを表すタグです。 |
| cell | Excelのセルを表すタグです。 |
| v-for | 縦方向の繰り返しを行います。 |
| h-for | 横方向の繰り返しを行います。 |

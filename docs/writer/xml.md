# XML

forma4jでExcelを出力するにはXMLファイルで出力方法を定義します。以下はXMLファイルの一例です。

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="データ">
    <row rowIndex="0" startColumnIndex="0">
        <cell>ヘッダ（キー）</cell>
        <cell>ヘッダ（値）</cell>
    </row>
    <vertical-for item="データ" collection="データリスト" startRowIndex="1" startColumnIndex="0">
      <row>
        <cell>#{データ.キー}</cell>
        <cell>#{データ.値}</cell>
      </row>
    </vertical-for>
  </sheet>
</forma>
```

このXMLファイルで出力されるExcelファイルをこのようになります。

![Excel](image/writer-xml-1.svg)

XMLファイルには以下のタグを使用することができます。これらのタグを組み合わせることでExcel出力定義を作成します。

| タグ | 役割 |
| --- | --- |
| forma | ルートタグです。 |
| sheet | Excelのシートを表すタグです。 |
| row | Excelの1行を表すタグです。 |
| column | Excelの1列を表すタグです。 |
| cell | Excelのセルを表すタグです。 |
| vertical-for | 縦方向の繰り返しを行います。 |
| horizontal-for | 横方向の繰り返しを行います。 |

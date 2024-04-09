# vertical-forタグ

vertical-forタグは縦方向の繰り返しを行います。

## 子要素

vertical-forタグは以下のタグを子要素として持つことができます。

- row
- column
- cell
- vertical-for
- horizontal-for

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| startRowIndex | ループを開始する行を設定します。 |
| startColumnIndex | ループを開始する列を設定します。 |
| collection | データコレクションです。データ件数の数だけループします。 |
| item | データが格納される変数です。ループの中でこの変数を使用してデータにアクセスします。 |

## vertical-forを使用して複数行に値を出力する

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="Example">
    <vertical-for item="data" collection="list" startRowIndex="0" startColumnIndex="0">
      <row>
        <cell>#{data.value1}</cell>
        <cell>#{data.value2}</cell>
        <cell>#{data.value3}</cell>
      </row>
    </vertical-for>
  </sheet>
</forma>
```

![Excel](image/writer-vertical-for-1.svg)

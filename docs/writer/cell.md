# cellタグ

cellタグはExcelのセルを表すタグです。

## 子要素

cellタグは子要素を持つことができません。

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| style | セルのスタイルを設定する。 |

## セルに値を出力する

[row](row.md)、[column](column.md)を参照してください。

## スタイルを設定する

セルにスタイルを設定することで罫線を引いたり、フォントサイズの設定などを行えます。

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="Example">
    <row rowIndex="1" startColumnIndex="1">
      <cell style="border:thin;">value</cell>
    </row>
  </sheet>
</forma>
```

![Excel](image/writer-cell-1.svg)

上記はセルに罫線を引く設定です。設定できるスタイルは[こちら](style.md)参照してください。

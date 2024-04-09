# cellタグ

cellタグはExcelのセルを表すタグです。

## 子要素

cellタグは子要素を持つことができません。

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| row | 行のインデックスを設定します。 |
| col | 列のインデックスを設定します。 |
| name | セルの名前を設定します。必須入力。 |

## セルの値を読み込む

XML

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet name="Example">
    <cell row="0" col="0" name="key" />
  </sheet>
</forma-reader>
```

Excel

![excel](image/reader-cell-1.svg)

読み取り結果

```json
{
  "forma" : {
    "Example" : {
      "key" : "key1"
    }
  }
}
```

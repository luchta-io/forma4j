# rowタグ

rowタグはExcelの1行を表すタグです。

## 子要素

rowタグは以下のタグを子要素として持つことができます。

- cell
- vertical-for
- horizontal-for

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| rowIndex | 子要素のcellを出力する行を設定します。 |
| startColumnIndex | 子要素のcellを出力する列の開始位置を設定します。 |
| autoFilter | 行にオートフィルターを設定するかどうかを設定します。 |

## セルに値を出力する

rowタグはcellタグと組み合わせることでExcelの特定のセルに値を出力することができます。

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="Example">
    <row rowIndex="0" startColumnIndex="0">
      <cell>value1</cell>
      <cell>value2</cell>
    </row>
  </sheet>
</forma>
```

![Excel](image/writer-row-1.svg)

rowIndexプロパティに指定された行にstartColumnIndexプロパティに指定された列から順に横方向に出力されます。

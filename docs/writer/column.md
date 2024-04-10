# columnタグ

columnタグはExcelの1列を表すタグです。

## 子要素

columnタグは以下のタグを子要素として持つことができます。

- cell
- vertical-for
- horizontal-for

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| startRowIndex | 子要素のcellを出力する行の開始位置を設定します。 |
| columnIndex | 子要素のcellを出力する列を設定します。 |

## セルに値を出力する

columnタグはcellタグと組み合わせることでExcelの特定のセルに値を出力することができます。

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="Example">
    <column startRowIndex="0" columnIndex="0">
      <cell>value1</cell>
      <cell>value2</cell>
    </column>
  </sheet>
</forma>
```

![Excel](image/writer-column-1.svg)

columnIndexプロパティに指定された列にstartRowIndexプロパティに指定された行から順に縦方向に出力されます。

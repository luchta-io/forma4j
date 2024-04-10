# sheetタグ

sheetタグはExcelのシートを表すタグです。

## 子要素

sheetタグは以下のタグを子要素として持つことができます。また、sheetタグには必ず子要素が必要となります。

- cell
- v-for
- h-for

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| name | シート名を設定します。設定された名前のシートを読み込みます。 |
| index | シートのインデックスを設定します。設定されたインデックスのシートを読み込みます。 |
| allSheets | すべてのシートを読み込むかどうかのフラグです。 |

## nameを使用したシートの読み込み

![Excel](image/reader-sheet-1.svg)

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet name="Example">
    <cell row="0" col="0" name="value" />
  </sheet>
</forma-reader>
```

```json
{
  "forma" : {
    "Example" : {
      "value" : "example"
    }
  }
}
```

## indexを使用したシートの読み込み

![Excel](image/reader-sheet-1.svg)

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet index="0">
    <cell row="0" col="0" name="value" />
  </sheet>
</forma-reader>
```

```json
{
  "forma" : {
    "Example" : {
      "value" : "example"
    }
  }
}
```

## allSheetsを使用したシートの読み込み

![Excel](image/reader-sheet-2.svg)

![Excel](image/reader-sheet-3.svg)

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet allSheets="true">
    <cell row="0" col="0" name="value" />
  </sheet>
</forma-reader>
```

```json
{
  "forma" : [
    {
      "Example1" : {
        "value" : "example"
      }
    },
    {
      "Example2" : {
        "value" : "example2"
      }
    }
  ]
}
```

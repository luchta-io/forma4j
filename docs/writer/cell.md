# cellタグ

cellタグはExcelのセルを表すタグです。

## 子要素

cellタグは子要素を持つことができません。

## プロパティ

| プロパティ名 | 役割                                     |
| ------------ | ---------------------------------------- |
| rowIndex     | セルの行インデックスを設定します         |
| columnIndex  | セルの列インデックスを設定します         |
| style        | セルのスタイルを設定します               |
| cellType     | セルの表示形式を設定します               |
| dataFormat   | セルの表示形式のフォーマットを設定します |

## セルに値を出力する

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="Example">
    <!-- 値を出力する -->
    <cell rowIndex="1" columnIndex="1">value</cell>
    <!-- 先頭が = の場合は計算式として出力される -->
    <cell rowIndex="1" columnIndex="2">=A1*3</cell>
  </sheet>
</forma>
```

`row` タグや `column` タグと組み合わせて出力する方法は[row](row.md)、[column](column.md)を参照してください。

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

## 表示形式を設定する

`cellType` と `dataFormat` を設定することで、セルの表示形式設定を行えます。

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma>
  <sheet name="Example">
    <cell rowIndex="1" columnIndex="1" cellType="NUMERIC" dataFormat="#,##0">value</cell>
  </sheet>
</forma>
```

### cellType

`cellType` には以下の値をセットします。大文字、小文字は区別しません。設定されている `cellType` に変換できない値の場合はすべて文字列として扱われます。

| cellType | Excel               | 説明                                        |
| -------- | ------------------- | ------------------------------------------- |
| BLANK    | 空セル（値なし）    | 参照時も null 扱いではなく BLANK となります |
| BOOLEAN  | 真偽値型            | TRUE または FALSE を表示します              |
| DATE     | 日付                | 日付として値を表示します                    |
| DATETIME | 日時                | 日時として値を表示します                    |
| FORMULA  | 数式型（=で始まる） | 実際の値は計算結果型に依存します            |
| NUMERIC  | 数値                | 数値として値を表示します                    |
| STRING   | 文字列              | 書式コード「@」が適用されます               |

### dataFormat

セルへ `#,##0` といったフォーマットを指定します。各 `cellType` にはデフォルトのフォーマットがあります。未設定の場合はデフォルトのフォーマットが適用されます。`cellType` が `STRING` または `FORMULA` の結果が文字列のときは無効です。

| cellType | デフォルトフォーマット |
| -------- | ---------------------- |
| NUMERIC  | #,##0                  |
| DATE     | yyyy/mm/dd             |
| DATETIME | yyyy/mm/dd hh:mm:ss    |
| STRING   | @                      |
| FORMULA  | なし                   |
| BOOLEAN  | TRUE or FALSE          |
| BLANK    | なし                   |

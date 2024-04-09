# h-forタグ

h-forタグは横方向の繰り返しを行います。

## 子要素

h-forタグは以下のタグを子要素として持つことができます。また、h-forタグには必ず子要素が必要となります。

- cell
- v-for
- h-for

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| from | ループを開始する列を0以上の整数値で設定します。 |
| to | ループを終了する列を0以上の整数値で設定します。 |
| name | ループの名前を設定します。 |

## h-forを使用して複数列を読み込む

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet name="Example">
    <h-for from="0" name="data">
      <cell col="0" name="row1" />
      <cell col="1" name="row2" />
      <cell col="2" name="row3" />
    </h-for>
  </sheet>
</forma-reader>
```

![Excel](image/reader-hfor-1.svg)

```json
{
  "forma": {
    "Example": {
      "data": {
        "row11": "value1-1",
        "row21": "value1-2",
        "row31": "value1-3",
        "row12": "value2-1",
        "row22": "value2-2",
        "row32": "value2-3",
        "row13": "value3-1",
        "row23": "value3-2",
        "row33": "value3-3"
      }
    }
  }
}
```

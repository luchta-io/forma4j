# v-forタグ

v-forタグは縦方向の繰り返しを行います。

## 子要素

v-forタグは以下のタグを子要素として持つことができます。また、v-forタグには必ず子要素が必要となります。

- cell
- v-for
- h-for

## プロパティ

| プロパティ名 | 役割 |
| --- | --- |
| from | ループを開始する行を0以上の整数値で設定します。 |
| to | ループを終了する行を0以上の整数値で設定します。 |
| name | ループの名前を設定します。 |

## v-forを使用して複数行を読み込む

```xml
<?xml version="1.0" encoding="utf-8"?>
<forma-reader>
  <sheet name="Example">
    <v-for from="0" name="data">
      <cell col="0" name="col1" />
      <cell col="1" name="col2" />
      <cell col="2" name="col3" />
    </v-for>
  </sheet>
</forma-reader>
```

![Excel](image/reader-vfor-1.svg)

```json
{
  "forma" : {
    "Example" : {
      "data" : [
        {
          "col1" : "value1-1", 
          "col2" : "value2-1", 
          "col3" : "value3-1"
        },
        {
          "col1" : "value1-2", 
          "col2" : "value2-2", 
          "col3" : "value3-2"
        },
        {
          "col1" : "value1-3", 
          "col2" : "value2-3", 
          "col3" : "value3-3"
        }
      ]
    }
  }
}
```

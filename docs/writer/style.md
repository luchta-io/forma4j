# style

セルに設定できるスタイルです。

## スタイル一覧

| プロパティ名 | 役割 |
| --- | --- |
| border | 罫線の設定を行います。 |
| font-size | フォントサイズの設定を行います。|
| font-style | フォントスタイルの設定を行います。 |
| wrapText | テキストの折り返し設定を行います。 |
| background-color | 背景色の設定を行います。 |

## border

設定例

```xml
<cell style="border:thin;">value</cell>
```

種別

| 種別 | 説明 |
| --- | --- |
| thin | 細線 |
| medium | 中太線 |
| dashed | 破線 |
| dotted | 点線 |
| thick | 太線 |
| double | 二重線 |
| hair | 点線 |
| medium_dashed | 破線（中太線） |
| dash_dot | 長点線 |
| medium_dash_dot | 長点線（中太線） |
| dash_dot_dot | 長点点線 |
| medium_dash_dot_dot | 長点点線（中太線） |
| slanted_dash_dot | 斜長点線 |

## font-size

設定例

```xml
<cell style="font-size:16;">value</cell>
```

フォントサイズを数値で設定します。

## font-style

設定例

```xml
<cell style="font-style:bold;">value</cell>
```

種別

| 種別 | 説明 |
| --- | --- |
| bold | 太字 |
| italic | 斜体 |
| bold-italic | 太字斜体 |

## wrapText

設定例

```xml
<cell style="wrapText:true;">value</cell>
```

種別

| 種別 | 説明 |
| --- | --- |
| true | セルに折り返し設定を行います。 |
| false | セルに折り返し設定を行いません。 |

## background-color

設定例

```xml
<cell style="background-color:#FFC000">value</cell>
```

背景色をRBG値で設定します。

## 複数スタイル設定

設定例

```xml
<cell style="border:thin;background-color:#FFC000">value</cell>
```

セミコロンで区切って複数のスタイルを設定することができます。

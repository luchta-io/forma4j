# style

セルに設定できるスタイルです。

## スタイル一覧

| プロパティ名 | 役割 |
| --- | --- |
| background-color | 背景色の設定を行います。 |
| border | 罫線の設定を行います。 |
| border-left | セルの左の罫線の設定を行います。 |
| border-right | セルの右の罫線の設定を行います。 |
| border-top | セルの上部の罫線の設定を行います。 |
| border-bottom | セルの下部の罫線の設定を行います。 |
| color | 文字色の設定を行います。 |
| font-family | フォントファミリーの設定を行います。|
| font-size | フォントサイズの設定を行います。|
| font-style | フォントスタイルの設定を行います。 |
| h-align | セル内の水平方向の文字位置設定を行います。 |
| v-align | セル内の垂直方向の文字位置設定を行います。 |
| width | カラム幅の設置を行います。 |
| wrapText | テキストの折り返し設定を行います。 |

## background-color

設定例

```xml
<cell style="background-color:#FFC000">value</cell>
```

背景色をRBG値で設定します。

## border, border-left, border-right, border-top, border-bottom

設定例

```xml
<cell style="border:thin;">value</cell>
<cell style="border-left:thin;">value</cell>
<cell style="border-right:thin;">value</cell>
<cell style="border-top:thin;">value</cell>
<cell style="border-bottom:thin;">value</cell>
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

## color

設定例

```xml
<cell style="color:#FFC000">value</cell>
```

文字色をRBG値で設定します。

## font-size

設定例

```xml
<cell style="font-size:16;">value</cell>
```

フォントサイズを数値で設定します。

## font-family

設定例

```xml
<cell style="font-family:Meiryo UI;">value</cell>
```

フォントファミリーを設定します。

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

## width

設定例

```xml
<cell style="width:100">value</cell>
```

幅を数値で指定します。0から255までの値を設定できます。

## h-align

設定例

```xml
<cell style="h-align:left">value</cell>
```

種別

| 種別 | 説明 |
| --- | --- |
| left | 左詰め |
| center | 中央揃え |
| right | 右詰め |
| justify | 両端揃え |
| fill | 繰り返し |
| center-selection | 選択範囲内で中央 |

## v-align

設定例

```xml
<cell style="v-align:top">value</cell>
```

種別

| 種別 | 説明 |
| --- | --- |
| top | 上詰め |
| center | 中央揃え |
| bottom | 下詰め |
| justify | 両端揃え |

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

## 複数スタイル設定

設定例

```xml
<cell style="border:thin;background-color:#FFC000">value</cell>
```

セミコロンで区切って複数のスタイルを設定することができます。

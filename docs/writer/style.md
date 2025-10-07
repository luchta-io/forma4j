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

### 設定例

```xml
<cell style="background-color:#FFC000">value</cell>
```

背景色をRBG値で設定します。

## border, border-left, border-right, border-top, border-bottom

### 設定例

```xml
<cell style="border:thin;">value</cell>
<cell style="border-left:thin;">value</cell>
<cell style="border-right:thin;">value</cell>
<cell style="border-top:thin;">value</cell>
<cell style="border-bottom:thin;">value</cell>
```

### 種別

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

### 設定例

```xml
<cell style="color:#FFC000">value</cell>
```

文字色をRBG値で設定します。

## font-size

### 設定例

```xml
<cell style="font-size:16;">value</cell>
```

フォントサイズを数値で設定します。

## font-family

### 設定例

```xml
<cell style="font-family:Meiryo UI;">value</cell>
```

フォントファミリーを設定します。

## font-style

### 設定例

```xml
<cell style="font-style:bold;">value</cell>
```

### 種別

| 種別 | 説明 |
| --- | --- |
| bold | 太字 |
| italic | 斜体 |
| bold-italic | 太字斜体 |

## width

### 設定例

```xml
<cell style="width:100">value</cell>
```

幅を数値で指定します。0から255までの値を設定できます。

## h-align

### 設定例

```xml
<cell style="h-align:left">value</cell>
```

### 種別

| 種別 | 説明 |
| --- | --- |
| left | 左詰め |
| center | 中央揃え |
| right | 右詰め |
| justify | 両端揃え |
| fill | 繰り返し |
| center-selection | 選択範囲内で中央 |

## v-align

### 設定例

```xml
<cell style="v-align:top">value</cell>
```

### 種別

| 種別 | 説明 |
| --- | --- |
| top | 上詰め |
| center | 中央揃え |
| bottom | 下詰め |
| justify | 両端揃え |

## wrapText

### 設定例

```xml
<cell style="wrapText:true;">value</cell>
```

### 種別

| 種別 | 説明 |
| --- | --- |
| true | セルに折り返し設定を行います。 |
| false | セルに折り返し設定を行いません。 |

## 複数スタイル設定

### 設定例

```xml
<cell style="border:thin;background-color:#FFC000">value</cell>
```

セミコロンで区切って複数のスタイルを設定することができます。

## IF文

### 設定例

```xml
<cell style="IF(#{value1 EQ value2}, `background-color: #FFC000`, `background-color: #00C000`)">value</cell>
<cell style="IF(#{value1 EQ value2}, `background-color: #FFC000`, `IF(#{value3 NE value4}, `background-color: #FFC000`, `background-color: #00C000`)`)">value</cell>
```

### 記述方法

```xml
<cell style="IF(#{条件式}, `TRUEスタイル`, `FALSEスタイル`)">value</cell>
```

スタイルに `IF` 文を記述することで条件に応じてセルのスタイルを切り替えることができます。`IF` は大文字・小文字を区別しません。

条件式は `#{` と `}` で囲んで記述します。TRUE・FALSEスタイルはバッククォート（`）で囲んで記述します。

### 条件演算子

| 条件演算子 | 例      | 説明                |
|----|--------|-------------------|
| EQ | 1 EQ 1 | 等しい       |
| NE | 1 NE 1 | 等しくない   |
| GE | 2 GE 1 | 大きいまたは等しい |
| LE | 1 LE 2 | 小さいまたは等しい |
| GT | 2 GT 1 | 大きい     |
| LT | 1 LT 2 | 小さい     |

条件演算子は大文字・小文字を区別しません。

### 論理演算子

| 条件演算子 | 例                 | 説明       |
|-------|-------------------|----------|
| AND   | 1 EQ 1 AND 2 EQ 2 | 論理積 |
| OR    | 1 NE 1 OR 2 NE 2  | 論理和 |

論理演算子は大文字・小文字を区別しません。

### 真偽値

IF文の条件式では真偽値を扱うことができます。 真偽値は `TRUE` / `FALSE` を指定できます。大文字・小文字を区別しません。

```xml
<cell style="IF(#{value EQ TRUE}, `background-color: #FFC000`, `background-color: #00C000`)">value</cell>
<cell style="IF(#{value EQ FALSE}, `background-color: #FFC000`, `background-color: #00C000`)">value</cell>
```

### NULL

IF文の条件式では `NULL` を扱うことができます。大文字・小文字を区別しません。

```xml
<cell style="IF(#{value EQ NULL}, `background-color: #FFC000`, `background-color: #00C000`)">value</cell>
```

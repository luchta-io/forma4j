grammar Style;

@header {
package jp.co.actier.luchta.forma4j.antlr.style;
}

styles 
  : style+
  ;

style 
  : property ':' property_value ';'?
  ;

property
  : Any+
  ;

property_value
  : Any+
  ;

Any
  : (Any_Alphabet | Unsigned_Integer | '-' | ' ')
  ;

Any_Alphabet
  : [a-zA-Z]
  ;

Unsigned_Integer
  : [0-9]
  ;

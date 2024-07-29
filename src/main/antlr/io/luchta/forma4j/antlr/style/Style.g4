grammar Style;

@header {
package io.luchta.forma4j.antlr.style;
}

styles 
  : style+
  ;

style 
  : property ':' property_value ';'?
  ;

property
  : Property_Character+
  ;

property_value
  : Property_Character+
  ;

Property_Character
  : ~([,\n\r"] | ':' | ';' | [ \t])
  ;

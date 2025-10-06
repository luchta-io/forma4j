grammar Style;

@header {
package io.luchta.forma4j.antlr.style;
}

styles 
  : style*
  ;

style
  : expression SEMICOLON?
  | property SEMICOLON?
  ;

property
  : property_name COLON property_value
  ;

property_name
  : (IDENTIFIER | NUMBER)+
  ;

property_value
  : (IDENTIFIER | NUMBER)+
  ;

expression
  : if_expression
  ;

if_expression
  : IF LPAREN condition COMMA BACK_QUOTE styles BACK_QUOTE COMMA BACK_QUOTE styles BACK_QUOTE RPAREN
  ;

condition
  : CONDITION_START boolean_expr CONDITION_END
  ;

boolean_expr
  : boolean_expr OR boolean_term
  | boolean_term
  ;

boolean_term
  : boolean_term AND boolean_factor
  | boolean_factor
  ;

boolean_factor
  : comparison
  | LPAREN boolean_expr RPAREN
  ;

comparison
  : operand comparison_operator operand
  ;

operand
  : IDENTIFIER
  | NUMBER
  | STRING
  ;

comparison_operator
  : EQ
  | NEQ
  | GT
  | GTE
  | LT
  | LTE
  ;

styles_fragment
  : (style)*
  ;

Property_Character
  : ~([0-9A-Za-z_] | [,\n\r"] | ':' | ';' | [ \t] | '(' | ')' | '{' | '}' | '<' | '>' | '=' | '!' | '&' | '|' | '`' | '\'')
  ;

IF         : [Ii][Ff];
LPAREN     : '(';
RPAREN     : ')';
COMMA      : ',';
BACK_QUOTE : '`';
HYPHEN     : '-';
COLON      : ':';
SEMICOLON  : ';';

CONDITION_START : '#{';
CONDITION_END   : '}' ;

AND : [Aa][Nn][Dd];
OR  : [Oo][Rr];

EQ  : [Ee][Qq];
NEQ : [Nn][Ee];
GTE : [Gg][Ee];
LTE : [Ll][Ee];
GT  : [Gg][Tt];
LT  : [Ll][Tt];

IDENTIFIER : [A-Za-z_#][A-Za-z0-9_-]*('.'[A-Za-z0-9_-]+)*;
NUMBER     : '-'? [0-9]+ ('.' [0-9]+)?;
STRING     : '\'' (~['\\] | '\\' .)* '\'';

WS : [ \t\r\n]+ -> skip ;

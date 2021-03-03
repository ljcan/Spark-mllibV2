grammar Calculator;

line : expr EOF;
expr : '(' expr ')'
     | expr ('*'|'/') expr
     | expr ('+'|'-') expr
     | FLOAT

WS : [ \t\n\r]+ -> skip ;
FLOAT : DIGIT+'.' DIGIT*EXPONENT?
      | '.' DIGIT+EXPONENT?
      |DIGIT + EXPONENT?;
fragment DIGIT : '0'..'9';
fragment EXPONENT : ('e'|'E') ('+'|'-')?DIGIT+ ;
# Запуск своих примеров
В Main 0 аргумент строка, которую надо обработать
# Запуск
Язык описания операций над целочисленными массивами задан следующей грамматикой:
'<digit>   ::= “0” | “1" | “2” | “3" | “4” | “5" | “6” | “7" | “8” | “9"
<number> ::= <digit> | <digit> <number>
<operation> ::= “+” | “-” | “*” | “>” | “<” | “=” | “&” | “|”
<constant-expression> ::= “-” <number> | <number>
<binary-expression> ::= “(” <expression> <operation> <expression> “)”
<expression> ::= “element” | <constant-expression> | <binary-expression>
<map-call> ::= “map{” <expression> “}”
<filter-call> ::= “filter{” <expression> “}”
<call> ::= <map-call> | <filter-call>
<call-chain> ::= <call> | <call> “%>%” <call-chain>'

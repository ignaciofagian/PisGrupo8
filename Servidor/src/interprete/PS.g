grammar PS;  
  
options { 
  language = Java;
  output=AST; 
}  

tokens { 
  BLOCK; 
  STATEMENTS; 
  ASSIGNMENT; 
  EXP; 
  IF; 
  UNARY_MIN; 
  NEGATE; 
  AVERAGE;
  DEVIATION;
  BUY;
  SELL;
  FUNC_CALL;
  HISTORY;
}  

@header {
  import java.util.Map;
  import java.util.HashMap;
  import java.util.LinkedList;
 
}

@lexer::members {
  private IErrorReporter errorReporter = null;
    public void setErrorReporter(IErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }
    
    public void emitErrorMessage(String msg) {
        errorReporter.reportError(msg);
    }
}
@members {   
    public Map<String, Variable> variables = null;  
    
    public void initializeVariables(){
        this.variables = new HashMap<String, Variable>();
    } 
    
    public Map<String, Variable> getVariables(){
        return this.variables;
    }
      
    private IErrorReporter errorReporter = null;
    public void setErrorReporter(IErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }
    
    public void emitErrorMessage(String msg) {
        errorReporter.reportError(msg);
    }
}


 
parse  
  :
  ('VAR' (t1=type i1=Identifier {
                                   
                                   String typ = $t1.text;
                                   String ident = $i1.text;
                                   if (variables.containsKey(ident)){
                                            throw new RuntimeException("Variable duplicada");
                                   }
                                   Variable var = new Variable(ident, typ, null);
                                   variables.put(ident, var);
                                   
                               } 
        (',' i2=Identifier    {
                                  String typ2 = $t1.text;
                                  String ident2 = $i2.text;
                                  if (variables.containsKey(ident2)){
                                            throw new RuntimeException("Variable duplicada");
                                   }
                                  Variable var2 = new Variable(ident2, typ2, null);
                                  variables.put(ident2, var2);
                              }
        
        )*)+ 'ENDVAR')*
  block EOF -> block  
  ;  
  
block  
  :  (statement)*  -> ^(BLOCK ^(STATEMENTS statement*))  
  ;  
  
type
    : 
      'Boolean'
    | 'Float'
    ;
    
statement  
  :  assignment  -> assignment  
  |  functionCall
  |  ifStatement  
  |  buyStatement
  |  sellStatement
  ;  

functionCall  
  :  Println '(' expression? ')'  -> ^(FUNC_CALL Println expression)
  ;  
  
assignment  
  :  Identifier  '=' expression -> ^(ASSIGNMENT Identifier expression)  
  ;  

ifStatement  
  :  ifStat elseStat? EndIf -> ^(IF ifStat elseStat?)  
  ;  
  
ifStat  
  :  If expression Then block -> ^(EXP expression block)  
  ;  
  
elseStat  
  :  Else block -> ^(EXP block)  
  ;  
  
expression  
  :  condExpr  
  ;  
  
condExpr  
  :  orExpr -> orExpr    
  ;  
  
orExpr  
  :  andExpr ('or'^ andExpr)*  
  ;  
  
andExpr  
  :  equExpr ('and'^ equExpr)*  
  ;  
  
equExpr  
  :  relExpr (('==' | '<>')^ relExpr)*  
  ;  
  
relExpr  
  :  addExpr (('>=' | '<=' | '>' | '<')^ addExpr)*  
  ;  
  
addExpr  
  :  mulExpr (('+' | '-')^ mulExpr)*  
  ;  
  
mulExpr  
  :  powExpr (('*' | '/' | 'mod')^ powExpr)*  
  ;  
  
powExpr  
  :  unaryExpr ('^'^ unaryExpr)*  
  ;  
    
unaryExpr  
  :  '-' atom -> ^(UNARY_MIN atom)  
  |  'not' atom -> ^(NEGATE atom)  
  |  atom  
  ;  
  
atom  
  :  Number  
  |  Bool 
  |  Identifier 
  |  averageExpression
  |  deviationExpression
  |  historyExpression
  ;  
   
buyStatement
    : 'BUY' '(' expression ')' -> ^(BUY expression)
    ;
    
sellStatement
    : 'SELL' '(' expression ')' -> ^(SELL expression)
    ;
         
averageExpression
    : 'avg' '[' expression ',' expression ']' -> ^(AVERAGE expression expression)
    ;

deviationExpression
    : 'dev' '[' expression ',' expression ']' -> ^(DEVIATION expression expression)
    ; 

historyExpression
    : 'HISTORY' '[' expression ']' -> ^(HISTORY expression)
    ;
    
//HISTORIA  
    
Println  : 'println';  
Print    : 'print';  
Assert   : 'assert';  
Size     : 'size';  
Def      : 'def';  
If       : 'if';  
Else     : 'else';  
Return   : 'return';  
For      : 'for';  
While    : 'while';  
To       : 'to';  
Then       : 'then';  
EndIf      : 'endif';  
In       : 'in';  
Null     : 'null';  
  
Or       : 'or';  
And      : 'and';  
Equals   : '==';  
NEquals  : '<>';  
GTEquals : '>=';  
LTEquals : '<=';  
Pow      : '^';  
Excl     : 'not';  
GT       : '>';  
LT       : '<';  
Add      : '+';  
Subtract : '-';  
Multiply : '*';  
Divide   : '/';  
Modulus  : 'mod';  
OBrace   : '{';  
CBrace   : '}';  
OBracket : '[';  
CBracket : ']';  
OParen   : '(';  
CParen   : ')';  
SColon   : ';';  
Assign   : '=';  
Comma    : ',';  
QMark    : '?';  
Colon    : ':';  
  
Bool  
  :  'true'   
  |  'false'  
  ;  
  
Number  
  :  Int ('.' Digit+)?  
  ;  
  
Identifier  
  :  ('a'..'z' | '_') ('a'..'z' | 'A'..'Z' | '_' | Digit)*  
  ;   
  
Comment  
  :  '//' ~('\r' | '\n')* {skip();}  
  |  '/*' .* '*/'         {skip();}  
  ;  
  
Space  
  :  (' ' | '\t' | '\r' | '\n' | '\u000C') {skip();}  
  ;  
  
fragment Int  
  :  '1'..'9' Digit*  
  |  '0'  
  ;  
    
fragment Digit   
  :  '0'..'9'  
  ; 
  
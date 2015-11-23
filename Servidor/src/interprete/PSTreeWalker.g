tree grammar PSTreeWalker;  
  
options { 
  tokenVocab=PS; 
  ASTLabelType=CommonTree; 
}  

@header {
  import java.util.Map;
  import java.util.HashMap;
}

@members {   
    public Map<String, Variable> variables = null; 
    public Operations oper; 
    
    public void initializeVariables(HashMap<String, Variable> vars){
        this.variables = vars;
    } 
    
    public void setOperations(){
        this.oper = new Operations();
    }
    
    public Map<String, Variable> getVariables(){
        return this.variables;
    }
    
    public Operations getOperations(){
        return this.oper;
    }
}
 
walk returns [PSNode node]
  :  block  {node = $block.node;}
  ;  
  
block returns [PSNode node]
@init { 
  BlockNode bn = new BlockNode(); 
  node = bn; 
}

  :  ^(BLOCK
             ^(STATEMENTS (statement {
	                                      bn.addStatement($statement.node); 			                            
		                             }
		                  )*
		      )
      )  
  ;  
  
statement returns [PSNode node] 
  :  assignment      {node = $assignment.node;}
  |  functionCall    {node = $functionCall.node;}   
  |  ifStatement     {node = $ifStatement.node;}
  |  buyStatement    {node = $buyStatement.node;} 
  |  sellStatement   {node = $sellStatement.node;}
  ;  
 
functionCall returns [PSNode node]  
    : 
    ^(FUNC_CALL Println expression?) {node = new PrintlnNode($expression.node);}
    ;
  
assignment  returns [PSNode node]
  :  ^(ASSIGNMENT i=Identifier e=expression)  {
                                                    node = new AssignmentNode($i.text, $e.node, variables);
                                              }
  ;  

ifStatement returns [PSNode node]  
@init  { 
  IfNode ifNode = new IfNode(); 
  node = ifNode; 
}  
  :  ^(IF   
       (^(EXP expression b1=block){ifNode.addChoice($expression.node,$b1.node);})+   
       (^(EXP b2=block)           {ifNode.addChoice(new AtomNode(true),$b2.node);})?  
     )  
  ;  
  
expression returns [PSNode node]
  :  ^('or' a=expression b=expression)          {node = new OrNode($a.node, $b.node);}
  |  ^('and' a=expression b=expression)         {node = new AndNode($a.node, $b.node);}
  |  ^('==' a=expression b=expression)          {node = new EqualsNode($a.node, $b.node);}
  |  ^('<>' a=expression b=expression)          {node = new NotEqualsNode($a.node, $b.node);}
  |  ^('>=' a=expression b=expression)          {node = new GTEqualsNode($a.node, $b.node);}
  |  ^('<=' a=expression b=expression)          {node = new LTEqualsNode($a.node, $b.node);}
  |  ^('>' a=expression b=expression)           {node = new GTNode($a.node, $b.node);}
  |  ^('<' a=expression b=expression)           {node = new LTNode($a.node, $b.node);}
  |  ^('+' a=expression b=expression)           {node = new AddNode($a.node, $b.node);}  
  |  ^('-' a=expression b=expression)           {node = new SubNode($a.node, $b.node);}
  |  ^('*' a=expression b=expression)           {node = new MulNode($a.node, $b.node);} 
  |  ^('/' a=expression b=expression)           {node = new DivNode($a.node, $b.node);}
  |  ^('mod' a=expression b=expression)         {node = new ModNode($a.node, $b.node);}
  |  ^('^' a=expression b=expression)           {node = new PowNode($a.node, $b.node);}  
  |  ^(UNARY_MIN a=expression)                  {node = new UnaryMinusNode($a.node);}
  |  ^(NEGATE a=expression)                     {node = new NegateNode($a.node);}
  |  Number                                     {node = new AtomNode(Double.parseDouble($Number.text));}
  |  Bool                                       {node = new AtomNode(Boolean.parseBoolean($Bool.text));}
  |  Identifier                                 {node = new IdentifierNode($Identifier.text, variables);}
  |  averageExpression                          {node = $averageExpression.node;}
  |  deviationExpression                        {node = $deviationExpression.node;}
  |  historyExpression                          {node = $historyExpression.node;}
  ;  
 
averageExpression returns [PSNode node]
    : ^(AVERAGE a=expression b=expression)      {node = new AverageNode($a.node, $b.node);}
    ;
    
deviationExpression returns [PSNode node]
    : ^(DEVIATION a=expression b=expression)    {node = new DeviationNode($a.node, $b.node);}
    ;

historyExpression returns [PSNode node]
    : ^(HISTORY a=expression)                   {node = new HistoryNode($a.node);}
    ;
    
buyStatement returns [PSNode node]
    : ^(BUY expression)                         {node = new BuyNode($expression.node, oper);}
    ;
 
sellStatement returns [PSNode node]
    : ^(SELL expression)                        {node = new SellNode($expression.node, oper);}
    ;
 

 
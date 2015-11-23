package interprete;

import java.util.Map;
  import java.util.HashMap;
  import java.util.LinkedList;
 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;

@SuppressWarnings("all")
public class PSParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSIGNMENT", "AVERAGE", "Add", 
		"And", "Assert", "Assign", "BLOCK", "BUY", "Bool", "CBrace", "CBracket", 
		"CParen", "Colon", "Comma", "Comment", "DEVIATION", "Def", "Digit", "Divide", 
		"EXP", "Else", "EndIf", "Equals", "Excl", "FUNC_CALL", "For", "GT", "GTEquals", 
		"HISTORY", "IF", "Identifier", "If", "In", "Int", "LT", "LTEquals", "Modulus", 
		"Multiply", "NEGATE", "NEGATION", "NEquals", "Null", "Number", "OBrace", 
		"OBracket", "OParen", "Or", "Pow", "Print", "Println", "QMark", "Return", 
		"SColon", "SELL", "STATEMENTS", "Size", "Space", "Subtract", "Then", "To", 
		"UNARY_MIN", "While", "'BUY'", "'Boolean'", "'ENDVAR'", "'Float'", "'HISTORY'", 
		"'SELL'", "'VAR'", "'avg'", "'dev'"
	};
	public static final int EOF=-1;
	public static final int T__66=66;
	public static final int T__67=67;
	public static final int T__68=68;
	public static final int T__69=69;
	public static final int T__70=70;
	public static final int T__71=71;
	public static final int T__72=72;
	public static final int T__73=73;
	public static final int T__74=74;
	public static final int ASSIGNMENT=4;
	public static final int AVERAGE=5;
	public static final int Add=6;
	public static final int And=7;
	public static final int Assert=8;
	public static final int Assign=9;
	public static final int BLOCK=10;
	public static final int BUY=11;
	public static final int Bool=12;
	public static final int CBrace=13;
	public static final int CBracket=14;
	public static final int CParen=15;
	public static final int Colon=16;
	public static final int Comma=17;
	public static final int Comment=18;
	public static final int DEVIATION=19;
	public static final int Def=20;
	public static final int Digit=21;
	public static final int Divide=22;
	public static final int EXP=23;
	public static final int Else=24;
	public static final int EndIf=25;
	public static final int Equals=26;
	public static final int Excl=27;
	public static final int FUNC_CALL=28;
	public static final int For=29;
	public static final int GT=30;
	public static final int GTEquals=31;
	public static final int HISTORY=32;
	public static final int IF=33;
	public static final int Identifier=34;
	public static final int If=35;
	public static final int In=36;
	public static final int Int=37;
	public static final int LT=38;
	public static final int LTEquals=39;
	public static final int Modulus=40;
	public static final int Multiply=41;
	public static final int NEGATE=42;
	public static final int NEGATION=43;
	public static final int NEquals=44;
	public static final int Null=45;
	public static final int Number=46;
	public static final int OBrace=47;
	public static final int OBracket=48;
	public static final int OParen=49;
	public static final int Or=50;
	public static final int Pow=51;
	public static final int Print=52;
	public static final int Println=53;
	public static final int QMark=54;
	public static final int Return=55;
	public static final int SColon=56;
	public static final int SELL=57;
	public static final int STATEMENTS=58;
	public static final int Size=59;
	public static final int Space=60;
	public static final int Subtract=61;
	public static final int Then=62;
	public static final int To=63;
	public static final int UNARY_MIN=64;
	public static final int While=65;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public PSParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public PSParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return PSParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/Users/mauriciomorinelli/Desktop/ProyectosEclipse/PlainStock/src/PS.g"; }

	   
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


	public static class parse_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parse"

	public final PSParser.parse_return parse() throws RecognitionException {
		PSParser.parse_return retval = new PSParser.parse_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token i1=null;
		Token i2=null;
		Token string_literal1=null;
		Token char_literal2=null;
		Token string_literal3=null;
		Token EOF5=null;
		ParserRuleReturnScope t1 =null;
		ParserRuleReturnScope block4 =null;

		Object i1_tree=null;
		Object i2_tree=null;
		Object string_literal1_tree=null;
		Object char_literal2_tree=null;
		Object string_literal3_tree=null;
		Object EOF5_tree=null;
		RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
		RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
		RewriteRuleTokenStream stream_Comma=new RewriteRuleTokenStream(adaptor,"token Comma");
		RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
		RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");
		RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

		try {
			
			
			{
		
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==72) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					
					{
					string_literal1=(Token)match(input,72,FOLLOW_72_in_parse154);  
					stream_72.add(string_literal1);

					
					int cnt2=0;
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( (LA2_0==67||LA2_0==69) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							
							{
							pushFollow(FOLLOW_type_in_parse159);
							t1=type();
							state._fsp--;

							stream_type.add(t1.getTree());
							i1=(Token)match(input,Identifier,FOLLOW_Identifier_in_parse163);  
							stream_Identifier.add(i1);


							                                   
							                                   String typ = (t1!=null?input.toString(t1.start,t1.stop):null);
							                                   String ident = (i1!=null?i1.getText():null);
							                                   if (variables.containsKey(ident)){
							                                            throw new RuntimeException("Variable duplicada");
							                                   }
							                                   Variable var = new Variable(ident, typ, null);
							                                   variables.put(ident, var);
							                                   
							                               
							
							loop1:
							while (true) {
								int alt1=2;
								int LA1_0 = input.LA(1);
								if ( (LA1_0==Comma) ) {
									alt1=1;
								}

								switch (alt1) {
								case 1 :
									
									{
									char_literal2=(Token)match(input,Comma,FOLLOW_Comma_in_parse177);  
									stream_Comma.add(char_literal2);

									i2=(Token)match(input,Identifier,FOLLOW_Identifier_in_parse181);  
									stream_Identifier.add(i2);


									                                  String typ2 = (t1!=null?input.toString(t1.start,t1.stop):null);
									                                  String ident2 = (i2!=null?i2.getText():null);
									                                  if (variables.containsKey(ident2)){
									                                            throw new RuntimeException("Variable duplicada");
									                                   }
									                                  Variable var2 = new Variable(ident2, typ2, null);
									                                  variables.put(ident2, var2);
									                              
									}
									break;

								default :
									break loop1;
								}
							}

							}
							break;

						default :
							if ( cnt2 >= 1 ) break loop2;
							EarlyExitException eee = new EarlyExitException(2, input);
							throw eee;
						}
						cnt2++;
					}

					string_literal3=(Token)match(input,68,FOLLOW_68_in_parse210);  
					stream_68.add(string_literal3);

					}
					break;

				default :
					break loop3;
				}
			}

			pushFollow(FOLLOW_block_in_parse216);
			block4=block();
			state._fsp--;

			stream_block.add(block4.getTree());
			EOF5=(Token)match(input,EOF,FOLLOW_EOF_in_parse218);  
			stream_EOF.add(EOF5);

			// AST REWRITE
			// elements: block
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 89:13: -> block
			{
				adaptor.addChild(root_0, stream_block.nextTree());
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "parse"


	public static class block_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "block"
	
	public final PSParser.block_return block() throws RecognitionException {
		PSParser.block_return retval = new PSParser.block_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope statement6 =null;

		RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");

		try {
			
			{
			
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= Identifier && LA4_0 <= If)||LA4_0==Println||LA4_0==66||LA4_0==71) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					
					{
					pushFollow(FOLLOW_statement_in_block245);
					statement6=statement();
					state._fsp--;

					stream_statement.add(statement6.getTree());
					}
					break;

				default :
					break loop4;
				}
			}

			// AST REWRITE
			// elements: statement
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 93:20: -> ^( BLOCK ^( STATEMENTS ( statement )* ) )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BLOCK, "BLOCK"), root_1);
				
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(STATEMENTS, "STATEMENTS"), root_2);
				
				while ( stream_statement.hasNext() ) {
					adaptor.addChild(root_2, stream_statement.nextTree());
				}
				stream_statement.reset();

				adaptor.addChild(root_1, root_2);
				}

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "block"


	public static class type_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "type"
	
	public final PSParser.type_return type() throws RecognitionException {
		PSParser.type_return retval = new PSParser.type_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set7=null;

		Object set7_tree=null;

		try {
			
			{
			root_0 = (Object)adaptor.nil();


			set7=input.LT(1);
			if ( input.LA(1)==67||input.LA(1)==69 ) {
				input.consume();
				adaptor.addChild(root_0, (Object)adaptor.create(set7));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "type"


	public static class statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "statement"
	
	public final PSParser.statement_return statement() throws RecognitionException {
		PSParser.statement_return retval = new PSParser.statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope assignment8 =null;
		ParserRuleReturnScope functionCall9 =null;
		ParserRuleReturnScope ifStatement10 =null;
		ParserRuleReturnScope buyStatement11 =null;
		ParserRuleReturnScope sellStatement12 =null;

		RewriteRuleSubtreeStream stream_assignment=new RewriteRuleSubtreeStream(adaptor,"rule assignment");

		try {
			
			int alt5=5;
			switch ( input.LA(1) ) {
			case Identifier:
				{
				alt5=1;
				}
				break;
			case Println:
				{
				alt5=2;
				}
				break;
			case If:
				{
				alt5=3;
				}
				break;
			case 66:
				{
				alt5=4;
				}
				break;
			case 71:
				{
				alt5=5;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}
			switch (alt5) {
				case 1 :
				
					{
					pushFollow(FOLLOW_assignment_in_statement319);
					assignment8=assignment();
					state._fsp--;

					stream_assignment.add(assignment8.getTree());
					// AST REWRITE
					// elements: assignment
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 103:18: -> assignment
					{
						adaptor.addChild(root_0, stream_assignment.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 2 :
					
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functionCall_in_statement333);
					functionCall9=functionCall();
					state._fsp--;

					adaptor.addChild(root_0, functionCall9.getTree());

					}
					break;
				case 3 :
				
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_ifStatement_in_statement340);
					ifStatement10=ifStatement();
					state._fsp--;

					adaptor.addChild(root_0, ifStatement10.getTree());

					}
					break;
				case 4 :
					
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_buyStatement_in_statement349);
					buyStatement11=buyStatement();
					state._fsp--;

					adaptor.addChild(root_0, buyStatement11.getTree());

					}
					break;
				case 5 :
				
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_sellStatement_in_statement356);
					sellStatement12=sellStatement();
					state._fsp--;

					adaptor.addChild(root_0, sellStatement12.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "statement"


	public static class functionCall_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "functionCall"
	
	public final PSParser.functionCall_return functionCall() throws RecognitionException {
		PSParser.functionCall_return retval = new PSParser.functionCall_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Println13=null;
		Token char_literal14=null;
		Token char_literal16=null;
		ParserRuleReturnScope expression15 =null;

		Object Println13_tree=null;
		Object char_literal14_tree=null;
		Object char_literal16_tree=null;
		RewriteRuleTokenStream stream_Println=new RewriteRuleTokenStream(adaptor,"token Println");
		RewriteRuleTokenStream stream_OParen=new RewriteRuleTokenStream(adaptor,"token OParen");
		RewriteRuleTokenStream stream_CParen=new RewriteRuleTokenStream(adaptor,"token CParen");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			
			{
			Println13=(Token)match(input,Println,FOLLOW_Println_in_functionCall374);  
			stream_Println.add(Println13);

			char_literal14=(Token)match(input,OParen,FOLLOW_OParen_in_functionCall376);  
			stream_OParen.add(char_literal14);

			
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==Bool||LA6_0==Excl||LA6_0==Identifier||LA6_0==Number||LA6_0==OParen||LA6_0==Subtract||LA6_0==70||(LA6_0 >= 73 && LA6_0 <= 74)) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					
					{
					pushFollow(FOLLOW_expression_in_functionCall378);
					expression15=expression();
					state._fsp--;

					stream_expression.add(expression15.getTree());
					}
					break;

			}

			char_literal16=(Token)match(input,CParen,FOLLOW_CParen_in_functionCall381);  
			stream_CParen.add(char_literal16);

			// AST REWRITE
			// elements: Println, expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 111:35: -> ^( FUNC_CALL Println expression )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FUNC_CALL, "FUNC_CALL"), root_1);
				adaptor.addChild(root_1, stream_Println.nextNode());
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "functionCall"


	public static class assignment_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "assignment"
	
	public final PSParser.assignment_return assignment() throws RecognitionException {
		PSParser.assignment_return retval = new PSParser.assignment_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Identifier17=null;
		Token char_literal18=null;
		ParserRuleReturnScope expression19 =null;

		Object Identifier17_tree=null;
		Object char_literal18_tree=null;
		RewriteRuleTokenStream stream_Assign=new RewriteRuleTokenStream(adaptor,"token Assign");
		RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			{
			Identifier17=(Token)match(input,Identifier,FOLLOW_Identifier_in_assignment412);  
			stream_Identifier.add(Identifier17);

			char_literal18=(Token)match(input,Assign,FOLLOW_Assign_in_assignment415);  
			stream_Assign.add(char_literal18);

			pushFollow(FOLLOW_expression_in_assignment417);
			expression19=expression();
			state._fsp--;

			stream_expression.add(expression19.getTree());
			// AST REWRITE
			// elements: Identifier, expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 115:33: -> ^( ASSIGNMENT Identifier expression )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ASSIGNMENT, "ASSIGNMENT"), root_1);
				adaptor.addChild(root_1, stream_Identifier.nextNode());
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "assignment"


	public static class ifStatement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ifStatement"
	
	public final PSParser.ifStatement_return ifStatement() throws RecognitionException {
		PSParser.ifStatement_return retval = new PSParser.ifStatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EndIf22=null;
		ParserRuleReturnScope ifStat20 =null;
		ParserRuleReturnScope elseStat21 =null;

		Object EndIf22_tree=null;
		RewriteRuleTokenStream stream_EndIf=new RewriteRuleTokenStream(adaptor,"token EndIf");
		RewriteRuleSubtreeStream stream_ifStat=new RewriteRuleSubtreeStream(adaptor,"rule ifStat");
		RewriteRuleSubtreeStream stream_elseStat=new RewriteRuleSubtreeStream(adaptor,"rule elseStat");

		try {
			
			{
			pushFollow(FOLLOW_ifStat_in_ifStatement447);
			ifStat20=ifStat();
			state._fsp--;

			stream_ifStat.add(ifStat20.getTree());
			
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==Else) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					
					{
					pushFollow(FOLLOW_elseStat_in_ifStatement449);
					elseStat21=elseStat();
					state._fsp--;

					stream_elseStat.add(elseStat21.getTree());
					}
					break;

			}

			EndIf22=(Token)match(input,EndIf,FOLLOW_EndIf_in_ifStatement452);  
			stream_EndIf.add(EndIf22);

			// AST REWRITE
			// elements: ifStat, elseStat
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 119:29: -> ^( IF ifStat ( elseStat )? )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(IF, "IF"), root_1);
				adaptor.addChild(root_1, stream_ifStat.nextTree());
				
				if ( stream_elseStat.hasNext() ) {
					adaptor.addChild(root_1, stream_elseStat.nextTree());
				}
				stream_elseStat.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "ifStatement"


	public static class ifStat_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ifStat"
	
	public final PSParser.ifStat_return ifStat() throws RecognitionException {
		PSParser.ifStat_return retval = new PSParser.ifStat_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token If23=null;
		Token Then25=null;
		ParserRuleReturnScope expression24 =null;
		ParserRuleReturnScope block26 =null;

		Object If23_tree=null;
		Object Then25_tree=null;
		RewriteRuleTokenStream stream_Then=new RewriteRuleTokenStream(adaptor,"token Then");
		RewriteRuleTokenStream stream_If=new RewriteRuleTokenStream(adaptor,"token If");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
		RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");

		try {
			
			{
			If23=(Token)match(input,If,FOLLOW_If_in_ifStat485);  
			stream_If.add(If23);

			pushFollow(FOLLOW_expression_in_ifStat487);
			expression24=expression();
			state._fsp--;

			stream_expression.add(expression24.getTree());
			Then25=(Token)match(input,Then,FOLLOW_Then_in_ifStat489);  
			stream_Then.add(Then25);

			pushFollow(FOLLOW_block_in_ifStat491);
			block26=block();
			state._fsp--;

			stream_block.add(block26.getTree());
			// AST REWRITE
			// elements: block, expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 123:31: -> ^( EXP expression block )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXP, "EXP"), root_1);
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_1, stream_block.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "ifStat"


	public static class elseStat_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "elseStat"
	
	public final PSParser.elseStat_return elseStat() throws RecognitionException {
		PSParser.elseStat_return retval = new PSParser.elseStat_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Else27=null;
		ParserRuleReturnScope block28 =null;

		Object Else27_tree=null;
		RewriteRuleTokenStream stream_Else=new RewriteRuleTokenStream(adaptor,"token Else");
		RewriteRuleSubtreeStream stream_block=new RewriteRuleSubtreeStream(adaptor,"rule block");

		try {
			
			{
			Else27=(Token)match(input,Else,FOLLOW_Else_in_elseStat523);  
			stream_Else.add(Else27);

			pushFollow(FOLLOW_block_in_elseStat525);
			block28=block();
			state._fsp--;

			stream_block.add(block28.getTree());
			// AST REWRITE
			// elements: block
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 127:17: -> ^( EXP block )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXP, "EXP"), root_1);
				adaptor.addChild(root_1, stream_block.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "elseStat"


	public static class expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "expression"
	
	public final PSParser.expression_return expression() throws RecognitionException {
		PSParser.expression_return retval = new PSParser.expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope condExpr29 =null;


		try {
			
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_condExpr_in_expression555);
			condExpr29=condExpr();
			state._fsp--;

			adaptor.addChild(root_0, condExpr29.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expression"


	public static class condExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "condExpr"
	
	public final PSParser.condExpr_return condExpr() throws RecognitionException {
		PSParser.condExpr_return retval = new PSParser.condExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope orExpr30 =null;

		RewriteRuleSubtreeStream stream_orExpr=new RewriteRuleSubtreeStream(adaptor,"rule orExpr");

		try {
			
			{
			pushFollow(FOLLOW_orExpr_in_condExpr577);
			orExpr30=orExpr();
			state._fsp--;

			stream_orExpr.add(orExpr30.getTree());
			// AST REWRITE
			// elements: orExpr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 135:13: -> orExpr
			{
				adaptor.addChild(root_0, stream_orExpr.nextTree());
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "condExpr"


	public static class orExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "orExpr"
	
	public final PSParser.orExpr_return orExpr() throws RecognitionException {
		PSParser.orExpr_return retval = new PSParser.orExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal32=null;
		ParserRuleReturnScope andExpr31 =null;
		ParserRuleReturnScope andExpr33 =null;

		Object string_literal32_tree=null;

		try {
			
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_andExpr_in_orExpr605);
			andExpr31=andExpr();
			state._fsp--;

			adaptor.addChild(root_0, andExpr31.getTree());

			
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==Or) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					
					{
					string_literal32=(Token)match(input,Or,FOLLOW_Or_in_orExpr608); 
					string_literal32_tree = (Object)adaptor.create(string_literal32);
					root_0 = (Object)adaptor.becomeRoot(string_literal32_tree, root_0);

					pushFollow(FOLLOW_andExpr_in_orExpr611);
					andExpr33=andExpr();
					state._fsp--;

					adaptor.addChild(root_0, andExpr33.getTree());

					}
					break;

				default :
					break loop8;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "orExpr"


	public static class andExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "andExpr"
	
	public final PSParser.andExpr_return andExpr() throws RecognitionException {
		PSParser.andExpr_return retval = new PSParser.andExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal35=null;
		ParserRuleReturnScope equExpr34 =null;
		ParserRuleReturnScope equExpr36 =null;

		Object string_literal35_tree=null;

		try {
			
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_equExpr_in_andExpr635);
			equExpr34=equExpr();
			state._fsp--;

			adaptor.addChild(root_0, equExpr34.getTree());

			
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==And) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
				
					{
					string_literal35=(Token)match(input,And,FOLLOW_And_in_andExpr638); 
					string_literal35_tree = (Object)adaptor.create(string_literal35);
					root_0 = (Object)adaptor.becomeRoot(string_literal35_tree, root_0);

					pushFollow(FOLLOW_equExpr_in_andExpr641);
					equExpr36=equExpr();
					state._fsp--;

					adaptor.addChild(root_0, equExpr36.getTree());

					}
					break;

				default :
					break loop9;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "andExpr"


	public static class equExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "equExpr"
	
	public final PSParser.equExpr_return equExpr() throws RecognitionException {
		PSParser.equExpr_return retval = new PSParser.equExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set38=null;
		ParserRuleReturnScope relExpr37 =null;
		ParserRuleReturnScope relExpr39 =null;

		Object set38_tree=null;

		try {
	
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_relExpr_in_equExpr665);
			relExpr37=relExpr();
			state._fsp--;

			adaptor.addChild(root_0, relExpr37.getTree());

			
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==Equals||LA10_0==NEquals) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
				
					{
					set38=input.LT(1);
					set38=input.LT(1);
					if ( input.LA(1)==Equals||input.LA(1)==NEquals ) {
						input.consume();
						root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set38), root_0);
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_relExpr_in_equExpr677);
					relExpr39=relExpr();
					state._fsp--;

					adaptor.addChild(root_0, relExpr39.getTree());

					}
					break;

				default :
					break loop10;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "equExpr"


	public static class relExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "relExpr"
	
	public final PSParser.relExpr_return relExpr() throws RecognitionException {
		PSParser.relExpr_return retval = new PSParser.relExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set41=null;
		ParserRuleReturnScope addExpr40 =null;
		ParserRuleReturnScope addExpr42 =null;

		Object set41_tree=null;

		try {
			
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_addExpr_in_relExpr701);
			addExpr40=addExpr();
			state._fsp--;

			adaptor.addChild(root_0, addExpr40.getTree());

			
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( ((LA11_0 >= GT && LA11_0 <= GTEquals)||(LA11_0 >= LT && LA11_0 <= LTEquals)) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					
					{
					set41=input.LT(1);
					set41=input.LT(1);
					if ( (input.LA(1) >= GT && input.LA(1) <= GTEquals)||(input.LA(1) >= LT && input.LA(1) <= LTEquals) ) {
						input.consume();
						root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set41), root_0);
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_addExpr_in_relExpr721);
					addExpr42=addExpr();
					state._fsp--;

					adaptor.addChild(root_0, addExpr42.getTree());

					}
					break;

				default :
					break loop11;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "relExpr"


	public static class addExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "addExpr"
	
	public final PSParser.addExpr_return addExpr() throws RecognitionException {
		PSParser.addExpr_return retval = new PSParser.addExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set44=null;
		ParserRuleReturnScope mulExpr43 =null;
		ParserRuleReturnScope mulExpr45 =null;

		Object set44_tree=null;

		try {
			
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_mulExpr_in_addExpr745);
			mulExpr43=mulExpr();
			state._fsp--;

			adaptor.addChild(root_0, mulExpr43.getTree());

			
			loop12:
			while (true) {
				int alt12=2;
				int LA12_0 = input.LA(1);
				if ( (LA12_0==Add||LA12_0==Subtract) ) {
					alt12=1;
				}

				switch (alt12) {
				case 1 :
					
					{
					set44=input.LT(1);
					set44=input.LT(1);
					if ( input.LA(1)==Add||input.LA(1)==Subtract ) {
						input.consume();
						root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set44), root_0);
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_mulExpr_in_addExpr757);
					mulExpr45=mulExpr();
					state._fsp--;

					adaptor.addChild(root_0, mulExpr45.getTree());

					}
					break;

				default :
					break loop12;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "addExpr"


	public static class mulExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "mulExpr"
	
	public final PSParser.mulExpr_return mulExpr() throws RecognitionException {
		PSParser.mulExpr_return retval = new PSParser.mulExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set47=null;
		ParserRuleReturnScope powExpr46 =null;
		ParserRuleReturnScope powExpr48 =null;

		Object set47_tree=null;

		try {
			
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_powExpr_in_mulExpr781);
			powExpr46=powExpr();
			state._fsp--;

			adaptor.addChild(root_0, powExpr46.getTree());

			
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( (LA13_0==Divide||(LA13_0 >= Modulus && LA13_0 <= Multiply)) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					
					{
					set47=input.LT(1);
					set47=input.LT(1);
					if ( input.LA(1)==Divide||(input.LA(1) >= Modulus && input.LA(1) <= Multiply) ) {
						input.consume();
						root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set47), root_0);
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_powExpr_in_mulExpr797);
					powExpr48=powExpr();
					state._fsp--;

					adaptor.addChild(root_0, powExpr48.getTree());

					}
					break;

				default :
					break loop13;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "mulExpr"


	public static class powExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "powExpr"
	
	public final PSParser.powExpr_return powExpr() throws RecognitionException {
		PSParser.powExpr_return retval = new PSParser.powExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal50=null;
		ParserRuleReturnScope unaryExpr49 =null;
		ParserRuleReturnScope unaryExpr51 =null;

		Object char_literal50_tree=null;

		try {
			
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_unaryExpr_in_powExpr821);
			unaryExpr49=unaryExpr();
			state._fsp--;

			adaptor.addChild(root_0, unaryExpr49.getTree());

			
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==Pow) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
				
					{
					char_literal50=(Token)match(input,Pow,FOLLOW_Pow_in_powExpr824); 
					char_literal50_tree = (Object)adaptor.create(char_literal50);
					root_0 = (Object)adaptor.becomeRoot(char_literal50_tree, root_0);

					pushFollow(FOLLOW_unaryExpr_in_powExpr827);
					unaryExpr51=unaryExpr();
					state._fsp--;

					adaptor.addChild(root_0, unaryExpr51.getTree());

					}
					break;

				default :
					break loop14;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "powExpr"


	public static class unaryExpr_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "unaryExpr"
	
	public final PSParser.unaryExpr_return unaryExpr() throws RecognitionException {
		PSParser.unaryExpr_return retval = new PSParser.unaryExpr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope negation52 =null;
		ParserRuleReturnScope unaryExpr53 =null;
		ParserRuleReturnScope booleanNegation54 =null;
		ParserRuleReturnScope unaryExpr55 =null;
		ParserRuleReturnScope atom56 =null;


		try {
			
			int alt15=3;
			switch ( input.LA(1) ) {
			case Subtract:
				{
				alt15=1;
				}
				break;
			case Excl:
				{
				alt15=2;
				}
				break;
			case Bool:
			case Identifier:
			case Number:
			case OParen:
			case 70:
			case 73:
			case 74:
				{
				alt15=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}
			switch (alt15) {
				case 1 :
					
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_negation_in_unaryExpr856);
					negation52=negation();
					state._fsp--;

					root_0 = (Object)adaptor.becomeRoot(negation52.getTree(), root_0);
					pushFollow(FOLLOW_unaryExpr_in_unaryExpr859);
					unaryExpr53=unaryExpr();
					state._fsp--;

					adaptor.addChild(root_0, unaryExpr53.getTree());

					}
					break;
				case 2 :
					
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_booleanNegation_in_unaryExpr869);
					booleanNegation54=booleanNegation();
					state._fsp--;

					root_0 = (Object)adaptor.becomeRoot(booleanNegation54.getTree(), root_0);
					pushFollow(FOLLOW_unaryExpr_in_unaryExpr872);
					unaryExpr55=unaryExpr();
					state._fsp--;

					adaptor.addChild(root_0, unaryExpr55.getTree());

					}
					break;
				case 3 :
				
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_atom_in_unaryExpr880);
					atom56=atom();
					state._fsp--;

					adaptor.addChild(root_0, atom56.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "unaryExpr"


	public static class negation_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "negation"
	
	public final PSParser.negation_return negation() throws RecognitionException {
		PSParser.negation_return retval = new PSParser.negation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal57=null;

		Object char_literal57_tree=null;
		RewriteRuleTokenStream stream_Subtract=new RewriteRuleTokenStream(adaptor,"token Subtract");

		try {
			
			{
			char_literal57=(Token)match(input,Subtract,FOLLOW_Subtract_in_negation903);  
			stream_Subtract.add(char_literal57);

			// AST REWRITE
			// elements: 
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 174:12: -> NEGATION
			{
				adaptor.addChild(root_0, (Object)adaptor.create(NEGATION, "NEGATION"));
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "negation"


	public static class booleanNegation_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "booleanNegation"
	
	public final PSParser.booleanNegation_return booleanNegation() throws RecognitionException {
		PSParser.booleanNegation_return retval = new PSParser.booleanNegation_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal58=null;

		Object string_literal58_tree=null;
		RewriteRuleTokenStream stream_Excl=new RewriteRuleTokenStream(adaptor,"token Excl");

		try {
		
			{
			string_literal58=(Token)match(input,Excl,FOLLOW_Excl_in_booleanNegation925);  
			stream_Excl.add(string_literal58);

			// AST REWRITE
			// elements: 
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 178:13: -> NEGATE
			{
				adaptor.addChild(root_0, (Object)adaptor.create(NEGATE, "NEGATE"));
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "booleanNegation"


	public static class atom_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "atom"
	
	public final PSParser.atom_return atom() throws RecognitionException {
		PSParser.atom_return retval = new PSParser.atom_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token Number59=null;
		Token Bool60=null;
		Token Identifier61=null;
		Token char_literal62=null;
		Token char_literal64=null;
		ParserRuleReturnScope expression63 =null;
		ParserRuleReturnScope averageExpression65 =null;
		ParserRuleReturnScope deviationExpression66 =null;
		ParserRuleReturnScope historyExpression67 =null;

		Object Number59_tree=null;
		Object Bool60_tree=null;
		Object Identifier61_tree=null;
		Object char_literal62_tree=null;
		Object char_literal64_tree=null;
		RewriteRuleTokenStream stream_OParen=new RewriteRuleTokenStream(adaptor,"token OParen");
		RewriteRuleTokenStream stream_CParen=new RewriteRuleTokenStream(adaptor,"token CParen");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			int alt16=7;
			switch ( input.LA(1) ) {
			case Number:
				{
				alt16=1;
				}
				break;
			case Bool:
				{
				alt16=2;
				}
				break;
			case Identifier:
				{
				alt16=3;
				}
				break;
			case OParen:
				{
				alt16=4;
				}
				break;
			case 73:
				{
				alt16=5;
				}
				break;
			case 74:
				{
				alt16=6;
				}
				break;
			case 70:
				{
				alt16=7;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}
			switch (alt16) {
				case 1 :
					
					{
					root_0 = (Object)adaptor.nil();


					Number59=(Token)match(input,Number,FOLLOW_Number_in_atom946); 
					Number59_tree = (Object)adaptor.create(Number59);
					adaptor.addChild(root_0, Number59_tree);

					}
					break;
				case 2 :
					
					{
					root_0 = (Object)adaptor.nil();


					Bool60=(Token)match(input,Bool,FOLLOW_Bool_in_atom955); 
					Bool60_tree = (Object)adaptor.create(Bool60);
					adaptor.addChild(root_0, Bool60_tree);

					}
					break;
				case 3 :
					
					{
					root_0 = (Object)adaptor.nil();


					Identifier61=(Token)match(input,Identifier,FOLLOW_Identifier_in_atom963); 
					Identifier61_tree = (Object)adaptor.create(Identifier61);
					adaptor.addChild(root_0, Identifier61_tree);

					}
					break;
				case 4 :
					
					{
					char_literal62=(Token)match(input,OParen,FOLLOW_OParen_in_atom971);  
					stream_OParen.add(char_literal62);

					pushFollow(FOLLOW_expression_in_atom973);
					expression63=expression();
					state._fsp--;

					stream_expression.add(expression63.getTree());
					char_literal64=(Token)match(input,CParen,FOLLOW_CParen_in_atom975);  
					stream_CParen.add(char_literal64);

					// AST REWRITE
					// elements: expression
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 184:25: -> expression
					{
						adaptor.addChild(root_0, stream_expression.nextTree());
					}


					retval.tree = root_0;

					}
					break;
				case 5 :
					
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_averageExpression_in_atom986);
					averageExpression65=averageExpression();
					state._fsp--;

					adaptor.addChild(root_0, averageExpression65.getTree());

					}
					break;
				case 6 :
					
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_deviationExpression_in_atom993);
					deviationExpression66=deviationExpression();
					state._fsp--;

					adaptor.addChild(root_0, deviationExpression66.getTree());

					}
					break;
				case 7 :
					
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_historyExpression_in_atom1000);
					historyExpression67=historyExpression();
					state._fsp--;

					adaptor.addChild(root_0, historyExpression67.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "atom"


	public static class buyStatement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "buyStatement"
	
	public final PSParser.buyStatement_return buyStatement() throws RecognitionException {
		PSParser.buyStatement_return retval = new PSParser.buyStatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal68=null;
		Token char_literal69=null;
		Token char_literal71=null;
		ParserRuleReturnScope expression70 =null;

		Object string_literal68_tree=null;
		Object char_literal69_tree=null;
		Object char_literal71_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_OParen=new RewriteRuleTokenStream(adaptor,"token OParen");
		RewriteRuleTokenStream stream_CParen=new RewriteRuleTokenStream(adaptor,"token CParen");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			{
			string_literal68=(Token)match(input,66,FOLLOW_66_in_buyStatement1020);  
			stream_66.add(string_literal68);

			char_literal69=(Token)match(input,OParen,FOLLOW_OParen_in_buyStatement1022);  
			stream_OParen.add(char_literal69);

			pushFollow(FOLLOW_expression_in_buyStatement1024);
			expression70=expression();
			state._fsp--;

			stream_expression.add(expression70.getTree());
			char_literal71=(Token)match(input,CParen,FOLLOW_CParen_in_buyStatement1026);  
			stream_CParen.add(char_literal71);

			// AST REWRITE
			// elements: expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 191:32: -> ^( BUY expression )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BUY, "BUY"), root_1);
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "buyStatement"


	public static class sellStatement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "sellStatement"
	
	public final PSParser.sellStatement_return sellStatement() throws RecognitionException {
		PSParser.sellStatement_return retval = new PSParser.sellStatement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal72=null;
		Token char_literal73=null;
		Token char_literal75=null;
		ParserRuleReturnScope expression74 =null;

		Object string_literal72_tree=null;
		Object char_literal73_tree=null;
		Object char_literal75_tree=null;
		RewriteRuleTokenStream stream_OParen=new RewriteRuleTokenStream(adaptor,"token OParen");
		RewriteRuleTokenStream stream_CParen=new RewriteRuleTokenStream(adaptor,"token CParen");
		RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			{
			string_literal72=(Token)match(input,71,FOLLOW_71_in_sellStatement1055);  
			stream_71.add(string_literal72);

			char_literal73=(Token)match(input,OParen,FOLLOW_OParen_in_sellStatement1057);  
			stream_OParen.add(char_literal73);

			pushFollow(FOLLOW_expression_in_sellStatement1059);
			expression74=expression();
			state._fsp--;

			stream_expression.add(expression74.getTree());
			char_literal75=(Token)match(input,CParen,FOLLOW_CParen_in_sellStatement1061);  
			stream_CParen.add(char_literal75);

			// AST REWRITE
			// elements: expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 195:33: -> ^( SELL expression )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SELL, "SELL"), root_1);
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sellStatement"


	public static class averageExpression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "averageExpression"
	
	public final PSParser.averageExpression_return averageExpression() throws RecognitionException {
		PSParser.averageExpression_return retval = new PSParser.averageExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal76=null;
		Token char_literal77=null;
		Token char_literal79=null;
		Token char_literal81=null;
		ParserRuleReturnScope expression78 =null;
		ParserRuleReturnScope expression80 =null;

		Object string_literal76_tree=null;
		Object char_literal77_tree=null;
		Object char_literal79_tree=null;
		Object char_literal81_tree=null;
		RewriteRuleTokenStream stream_CBracket=new RewriteRuleTokenStream(adaptor,"token CBracket");
		RewriteRuleTokenStream stream_OBracket=new RewriteRuleTokenStream(adaptor,"token OBracket");
		RewriteRuleTokenStream stream_Comma=new RewriteRuleTokenStream(adaptor,"token Comma");
		RewriteRuleTokenStream stream_73=new RewriteRuleTokenStream(adaptor,"token 73");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			{
			string_literal76=(Token)match(input,73,FOLLOW_73_in_averageExpression1095);  
			stream_73.add(string_literal76);

			char_literal77=(Token)match(input,OBracket,FOLLOW_OBracket_in_averageExpression1097);  
			stream_OBracket.add(char_literal77);

			pushFollow(FOLLOW_expression_in_averageExpression1099);
			expression78=expression();
			state._fsp--;

			stream_expression.add(expression78.getTree());
			char_literal79=(Token)match(input,Comma,FOLLOW_Comma_in_averageExpression1101);  
			stream_Comma.add(char_literal79);

			pushFollow(FOLLOW_expression_in_averageExpression1103);
			expression80=expression();
			state._fsp--;

			stream_expression.add(expression80.getTree());
			char_literal81=(Token)match(input,CBracket,FOLLOW_CBracket_in_averageExpression1105);  
			stream_CBracket.add(char_literal81);

			// AST REWRITE
			// elements: expression, expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 199:47: -> ^( AVERAGE expression expression )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(AVERAGE, "AVERAGE"), root_1);
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "averageExpression"


	public static class deviationExpression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "deviationExpression"
	
	public final PSParser.deviationExpression_return deviationExpression() throws RecognitionException {
		PSParser.deviationExpression_return retval = new PSParser.deviationExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal82=null;
		Token char_literal83=null;
		Token char_literal85=null;
		Token char_literal87=null;
		ParserRuleReturnScope expression84 =null;
		ParserRuleReturnScope expression86 =null;

		Object string_literal82_tree=null;
		Object char_literal83_tree=null;
		Object char_literal85_tree=null;
		Object char_literal87_tree=null;
		RewriteRuleTokenStream stream_CBracket=new RewriteRuleTokenStream(adaptor,"token CBracket");
		RewriteRuleTokenStream stream_OBracket=new RewriteRuleTokenStream(adaptor,"token OBracket");
		RewriteRuleTokenStream stream_Comma=new RewriteRuleTokenStream(adaptor,"token Comma");
		RewriteRuleTokenStream stream_74=new RewriteRuleTokenStream(adaptor,"token 74");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			{
			string_literal82=(Token)match(input,74,FOLLOW_74_in_deviationExpression1132);  
			stream_74.add(string_literal82);

			char_literal83=(Token)match(input,OBracket,FOLLOW_OBracket_in_deviationExpression1134);  
			stream_OBracket.add(char_literal83);

			pushFollow(FOLLOW_expression_in_deviationExpression1136);
			expression84=expression();
			state._fsp--;

			stream_expression.add(expression84.getTree());
			char_literal85=(Token)match(input,Comma,FOLLOW_Comma_in_deviationExpression1138);  
			stream_Comma.add(char_literal85);

			pushFollow(FOLLOW_expression_in_deviationExpression1140);
			expression86=expression();
			state._fsp--;

			stream_expression.add(expression86.getTree());
			char_literal87=(Token)match(input,CBracket,FOLLOW_CBracket_in_deviationExpression1142);  
			stream_CBracket.add(char_literal87);

			// AST REWRITE
			// elements: expression, expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 203:47: -> ^( DEVIATION expression expression )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DEVIATION, "DEVIATION"), root_1);
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "deviationExpression"


	public static class historyExpression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "historyExpression"
	
	public final PSParser.historyExpression_return historyExpression() throws RecognitionException {
		PSParser.historyExpression_return retval = new PSParser.historyExpression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal88=null;
		Token char_literal89=null;
		Token char_literal91=null;
		ParserRuleReturnScope expression90 =null;

		Object string_literal88_tree=null;
		Object char_literal89_tree=null;
		Object char_literal91_tree=null;
		RewriteRuleTokenStream stream_CBracket=new RewriteRuleTokenStream(adaptor,"token CBracket");
		RewriteRuleTokenStream stream_OBracket=new RewriteRuleTokenStream(adaptor,"token OBracket");
		RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
		RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");

		try {
			
			{
			string_literal88=(Token)match(input,70,FOLLOW_70_in_historyExpression1170);  
			stream_70.add(string_literal88);

			char_literal89=(Token)match(input,OBracket,FOLLOW_OBracket_in_historyExpression1172);  
			stream_OBracket.add(char_literal89);

			pushFollow(FOLLOW_expression_in_historyExpression1174);
			expression90=expression();
			state._fsp--;

			stream_expression.add(expression90.getTree());
			char_literal91=(Token)match(input,CBracket,FOLLOW_CBracket_in_historyExpression1176);  
			stream_CBracket.add(char_literal91);

			// AST REWRITE
			// elements: expression
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 207:36: -> ^( HISTORY expression )
			{
				
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(HISTORY, "HISTORY"), root_1);
				adaptor.addChild(root_1, stream_expression.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "historyExpression"

	// Delegated rules



	public static final BitSet FOLLOW_72_in_parse154 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000028L});
	public static final BitSet FOLLOW_type_in_parse159 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_Identifier_in_parse163 = new BitSet(new long[]{0x0000000000020000L,0x0000000000000038L});
	public static final BitSet FOLLOW_Comma_in_parse177 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_Identifier_in_parse181 = new BitSet(new long[]{0x0000000000020000L,0x0000000000000038L});
	public static final BitSet FOLLOW_68_in_parse210 = new BitSet(new long[]{0x0020000C00000000L,0x0000000000000184L});
	public static final BitSet FOLLOW_block_in_parse216 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_parse218 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statement_in_block245 = new BitSet(new long[]{0x0020000C00000002L,0x0000000000000084L});
	public static final BitSet FOLLOW_assignment_in_statement319 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functionCall_in_statement333 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ifStatement_in_statement340 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_buyStatement_in_statement349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sellStatement_in_statement356 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Println_in_functionCall374 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_OParen_in_functionCall376 = new BitSet(new long[]{0x2002400408009000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_functionCall378 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_CParen_in_functionCall381 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Identifier_in_assignment412 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_Assign_in_assignment415 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_assignment417 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ifStat_in_ifStatement447 = new BitSet(new long[]{0x0000000003000000L});
	public static final BitSet FOLLOW_elseStat_in_ifStatement449 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_EndIf_in_ifStatement452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_If_in_ifStat485 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_ifStat487 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_Then_in_ifStat489 = new BitSet(new long[]{0x0020000C00000000L,0x0000000000000084L});
	public static final BitSet FOLLOW_block_in_ifStat491 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Else_in_elseStat523 = new BitSet(new long[]{0x0020000C00000000L,0x0000000000000084L});
	public static final BitSet FOLLOW_block_in_elseStat525 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_condExpr_in_expression555 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_orExpr_in_condExpr577 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_andExpr_in_orExpr605 = new BitSet(new long[]{0x0004000000000002L});
	public static final BitSet FOLLOW_Or_in_orExpr608 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_andExpr_in_orExpr611 = new BitSet(new long[]{0x0004000000000002L});
	public static final BitSet FOLLOW_equExpr_in_andExpr635 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_And_in_andExpr638 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_equExpr_in_andExpr641 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_relExpr_in_equExpr665 = new BitSet(new long[]{0x0000100004000002L});
	public static final BitSet FOLLOW_set_in_equExpr668 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_relExpr_in_equExpr677 = new BitSet(new long[]{0x0000100004000002L});
	public static final BitSet FOLLOW_addExpr_in_relExpr701 = new BitSet(new long[]{0x000000C0C0000002L});
	public static final BitSet FOLLOW_set_in_relExpr704 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_addExpr_in_relExpr721 = new BitSet(new long[]{0x000000C0C0000002L});
	public static final BitSet FOLLOW_mulExpr_in_addExpr745 = new BitSet(new long[]{0x2000000000000042L});
	public static final BitSet FOLLOW_set_in_addExpr748 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_mulExpr_in_addExpr757 = new BitSet(new long[]{0x2000000000000042L});
	public static final BitSet FOLLOW_powExpr_in_mulExpr781 = new BitSet(new long[]{0x0000030000400002L});
	public static final BitSet FOLLOW_set_in_mulExpr784 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_powExpr_in_mulExpr797 = new BitSet(new long[]{0x0000030000400002L});
	public static final BitSet FOLLOW_unaryExpr_in_powExpr821 = new BitSet(new long[]{0x0008000000000002L});
	public static final BitSet FOLLOW_Pow_in_powExpr824 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_unaryExpr_in_powExpr827 = new BitSet(new long[]{0x0008000000000002L});
	public static final BitSet FOLLOW_negation_in_unaryExpr856 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_unaryExpr_in_unaryExpr859 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_booleanNegation_in_unaryExpr869 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_unaryExpr_in_unaryExpr872 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_atom_in_unaryExpr880 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Subtract_in_negation903 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Excl_in_booleanNegation925 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Number_in_atom946 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Bool_in_atom955 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Identifier_in_atom963 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OParen_in_atom971 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_atom973 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_CParen_in_atom975 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_averageExpression_in_atom986 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_deviationExpression_in_atom993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_historyExpression_in_atom1000 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_66_in_buyStatement1020 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_OParen_in_buyStatement1022 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_buyStatement1024 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_CParen_in_buyStatement1026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_sellStatement1055 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_OParen_in_sellStatement1057 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_sellStatement1059 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_CParen_in_sellStatement1061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_averageExpression1095 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_OBracket_in_averageExpression1097 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_averageExpression1099 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_Comma_in_averageExpression1101 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_averageExpression1103 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_CBracket_in_averageExpression1105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_74_in_deviationExpression1132 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_OBracket_in_deviationExpression1134 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_deviationExpression1136 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_Comma_in_deviationExpression1138 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_deviationExpression1140 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_CBracket_in_deviationExpression1142 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_historyExpression1170 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_OBracket_in_historyExpression1172 = new BitSet(new long[]{0x2002400408001000L,0x0000000000000640L});
	public static final BitSet FOLLOW_expression_in_historyExpression1174 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_CBracket_in_historyExpression1176 = new BitSet(new long[]{0x0000000000000002L});
}

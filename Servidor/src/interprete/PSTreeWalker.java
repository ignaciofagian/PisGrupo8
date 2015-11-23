package interprete;

import java.util.Map;
import java.util.HashMap;


import java.util.Stack;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;
@SuppressWarnings("all")
public class PSTreeWalker extends TreeParser {
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
	public TreeParser[] getDelegates() {
		return new TreeParser[] {};
	}

	// delegators


	public PSTreeWalker(TreeNodeStream input) {
		this(input, new RecognizerSharedState());
	}
	public PSTreeWalker(TreeNodeStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return PSTreeWalker.tokenNames; }
	@Override public String getGrammarFileName() { return "/Users/mauriciomorinelli/Desktop/ProyectosEclipse/PlainStock/src/PSTreeWalker.g"; }

	   
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



	// $ANTLR start "walk"
	
	public final PSNode walk() throws RecognitionException {
		PSNode node = null;


		PSNode block1 =null;

		try {
			
			{
			pushFollow(FOLLOW_block_in_walk60);
			block1=block();
			state._fsp--;

			node = block1;
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "walk"



	// $ANTLR start "block"
	
	public final PSNode block() throws RecognitionException {
		PSNode node = null;


		PSNode statement2 =null;

		 
		  BlockNode bn = new BlockNode(); 
		  node = bn; 

		try {
			
			{
			match(input,BLOCK,FOLLOW_BLOCK_in_block92); 
			match(input, Token.DOWN, null); 
			match(input,STATEMENTS,FOLLOW_STATEMENTS_in_block108); 
			if ( input.LA(1)==Token.DOWN ) {
				match(input, Token.DOWN, null); 
				
				loop1:
				while (true) {
					int alt1=2;
					int LA1_0 = input.LA(1);
					if ( (LA1_0==ASSIGNMENT||LA1_0==BUY||LA1_0==FUNC_CALL||LA1_0==IF||LA1_0==SELL) ) {
						alt1=1;
					}

					switch (alt1) {
					case 1 :
						
						{
						pushFollow(FOLLOW_statement_in_block111);
						statement2=statement();
						state._fsp--;


							                                      bn.addStatement(statement2); 			                            
								                             
						}
						break;

					default :
						break loop1;
					}
				}

				match(input, Token.UP, null); 
			}

			match(input, Token.UP, null); 

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "block"



	// $ANTLR start "statement"
	
	public final PSNode statement() throws RecognitionException {
		PSNode node = null;


		PSNode assignment3 =null;
		PSNode functionCall4 =null;
		PSNode ifStatement5 =null;
		PSNode buyStatement6 =null;
		PSNode sellStatement7 =null;

		try {
			
			int alt2=5;
			switch ( input.LA(1) ) {
			case ASSIGNMENT:
				{
				alt2=1;
				}
				break;
			case FUNC_CALL:
				{
				alt2=2;
				}
				break;
			case IF:
				{
				alt2=3;
				}
				break;
			case BUY:
				{
				alt2=4;
				}
				break;
			case SELL:
				{
				alt2=5;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}
			switch (alt2) {
				case 1 :
					
					{
					pushFollow(FOLLOW_assignment_in_statement179);
					assignment3=assignment();
					state._fsp--;

					node = assignment3;
					}
					break;
				case 2 :
					
					{
					pushFollow(FOLLOW_functionCall_in_statement193);
					functionCall4=functionCall();
					state._fsp--;

					node = functionCall4;
					}
					break;
				case 3 :
					
					{
					pushFollow(FOLLOW_ifStatement_in_statement208);
					ifStatement5=ifStatement();
					state._fsp--;

					node = ifStatement5;
					}
					break;
				case 4 :
					
					{
					pushFollow(FOLLOW_buyStatement_in_statement221);
					buyStatement6=buyStatement();
					state._fsp--;

					node = buyStatement6;
					}
					break;
				case 5 :
					
					{
					pushFollow(FOLLOW_sellStatement_in_statement234);
					sellStatement7=sellStatement();
					state._fsp--;

					node = sellStatement7;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "statement"



	// $ANTLR start "functionCall"
	
	public final PSNode functionCall() throws RecognitionException {
		PSNode node = null;


		PSNode expression8 =null;

		try {
			
			{
			match(input,FUNC_CALL,FOLLOW_FUNC_CALL_in_functionCall268); 
			match(input, Token.DOWN, null); 
			match(input,Println,FOLLOW_Println_in_functionCall270); 
			
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( ((LA3_0 >= AVERAGE && LA3_0 <= And)||LA3_0==Bool||LA3_0==DEVIATION||LA3_0==Divide||LA3_0==Equals||(LA3_0 >= GT && LA3_0 <= HISTORY)||LA3_0==Identifier||(LA3_0 >= LT && LA3_0 <= NEquals)||LA3_0==Number||(LA3_0 >= Or && LA3_0 <= Pow)||LA3_0==Subtract) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					
					{
					pushFollow(FOLLOW_expression_in_functionCall272);
					expression8=expression();
					state._fsp--;

					}
					break;

			}

			match(input, Token.UP, null); 

			node = new PrintlnNode(expression8);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "functionCall"



	// $ANTLR start "assignment"
	
	public final PSNode assignment() throws RecognitionException {
		PSNode node = null;


		CommonTree i=null;
		PSNode e =null;

		try {
			
			{
			match(input,ASSIGNMENT,FOLLOW_ASSIGNMENT_in_assignment300); 
			match(input, Token.DOWN, null); 
			i=(CommonTree)match(input,Identifier,FOLLOW_Identifier_in_assignment304); 
			pushFollow(FOLLOW_expression_in_assignment308);
			e=expression();
			state._fsp--;

			match(input, Token.UP, null); 


			                                                    node = new AssignmentNode((i!=null?i.getText():null), e, variables);
			                                              
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "assignment"



	// $ANTLR start "ifStatement"
	
	public final PSNode ifStatement() throws RecognitionException {
		PSNode node = null;


		PSNode b1 =null;
		PSNode b2 =null;
		PSNode expression9 =null;

		 
		  IfNode ifNode = new IfNode(); 
		  node = ifNode; 

		try {
			
			{
			match(input,IF,FOLLOW_IF_in_ifStatement343); 
			match(input, Token.DOWN, null); 
			
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==EXP) ) {
					int LA4_1 = input.LA(2);
					if ( (LA4_1==DOWN) ) {
						int LA4_3 = input.LA(3);
						if ( ((LA4_3 >= AVERAGE && LA4_3 <= And)||LA4_3==Bool||LA4_3==DEVIATION||LA4_3==Divide||LA4_3==Equals||(LA4_3 >= GT && LA4_3 <= HISTORY)||LA4_3==Identifier||(LA4_3 >= LT && LA4_3 <= NEquals)||LA4_3==Number||(LA4_3 >= Or && LA4_3 <= Pow)||LA4_3==Subtract) ) {
							alt4=1;
						}

					}

				}

				switch (alt4) {
				case 1 :
					
					{
					match(input,EXP,FOLLOW_EXP_in_ifStatement357); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_ifStatement359);
					expression9=expression();
					state._fsp--;

					pushFollow(FOLLOW_block_in_ifStatement363);
					b1=block();
					state._fsp--;

					match(input, Token.UP, null); 

					ifNode.addChoice(expression9,b1);
					}
					break;

				default :
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
			}

			
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==EXP) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					
					{
					match(input,EXP,FOLLOW_EXP_in_ifStatement381); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_block_in_ifStatement385);
					b2=block();
					state._fsp--;

					match(input, Token.UP, null); 

					ifNode.addChoice(new AtomNode(true),b2);
					}
					break;

			}

			match(input, Token.UP, null); 

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "ifStatement"



	// $ANTLR start "expression"
	
	public final PSNode expression() throws RecognitionException {
		PSNode node = null;


		CommonTree Number10=null;
		CommonTree Bool11=null;
		CommonTree Identifier12=null;
		PSNode a =null;
		PSNode b =null;
		PSNode averageExpression13 =null;
		PSNode deviationExpression14 =null;
		PSNode historyExpression15 =null;

		try {
			
			int alt6=22;
			switch ( input.LA(1) ) {
			case Or:
				{
				alt6=1;
				}
				break;
			case And:
				{
				alt6=2;
				}
				break;
			case Equals:
				{
				alt6=3;
				}
				break;
			case NEquals:
				{
				alt6=4;
				}
				break;
			case GTEquals:
				{
				alt6=5;
				}
				break;
			case LTEquals:
				{
				alt6=6;
				}
				break;
			case GT:
				{
				alt6=7;
				}
				break;
			case LT:
				{
				alt6=8;
				}
				break;
			case Add:
				{
				alt6=9;
				}
				break;
			case Subtract:
				{
				alt6=10;
				}
				break;
			case Multiply:
				{
				alt6=11;
				}
				break;
			case Divide:
				{
				alt6=12;
				}
				break;
			case Modulus:
				{
				alt6=13;
				}
				break;
			case Pow:
				{
				alt6=14;
				}
				break;
			case NEGATION:
				{
				alt6=15;
				}
				break;
			case NEGATE:
				{
				alt6=16;
				}
				break;
			case Number:
				{
				alt6=17;
				}
				break;
			case Bool:
				{
				alt6=18;
				}
				break;
			case Identifier:
				{
				alt6=19;
				}
				break;
			case AVERAGE:
				{
				alt6=20;
				}
				break;
			case DEVIATION:
				{
				alt6=21;
				}
				break;
			case HISTORY:
				{
				alt6=22;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}
			switch (alt6) {
				case 1 :
					
					{
					match(input,Or,FOLLOW_Or_in_expression434); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression438);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression442);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new OrNode(a, b);
					}
					break;
				case 2 :
					
					{
					match(input,And,FOLLOW_And_in_expression462); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression466);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression470);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new AndNode(a, b);
					}
					break;
				case 3 :
					
					{
					match(input,Equals,FOLLOW_Equals_in_expression489); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression493);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression497);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new EqualsNode(a, b);
					}
					break;
				case 4 :
					
					{
					match(input,NEquals,FOLLOW_NEquals_in_expression517); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression521);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression525);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new NotEqualsNode(a, b);
					}
					break;
				case 5 :
					
					{
					match(input,GTEquals,FOLLOW_GTEquals_in_expression545); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression549);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression553);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new GTEqualsNode(a, b);
					}
					break;
				case 6 :
					
					{
					match(input,LTEquals,FOLLOW_LTEquals_in_expression573); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression577);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression581);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new LTEqualsNode(a, b);
					}
					break;
				case 7 :
					
					{
					match(input,GT,FOLLOW_GT_in_expression601); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression605);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression609);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new GTNode(a, b);
					}
					break;
				case 8 :
					
					{
					match(input,LT,FOLLOW_LT_in_expression630); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression634);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression638);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new LTNode(a, b);
					}
					break;
				case 9 :
				
					{
					match(input,Add,FOLLOW_Add_in_expression659); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression663);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression667);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new AddNode(a, b);
					}
					break;
				case 10 :
					
					{
					match(input,Subtract,FOLLOW_Subtract_in_expression690); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression694);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression698);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new SubNode(a, b);
					}
					break;
				case 11 :
					
					{
					match(input,Multiply,FOLLOW_Multiply_in_expression719); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression723);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression727);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new MulNode(a, b);
					}
					break;
				case 12 :
				
					{
					match(input,Divide,FOLLOW_Divide_in_expression749); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression753);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression757);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new DivNode(a, b);
					}
					break;
				case 13 :
					
					{
					match(input,Modulus,FOLLOW_Modulus_in_expression778); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression782);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression786);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new ModNode(a, b);
					}
					break;
				case 14 :
					
					{
					match(input,Pow,FOLLOW_Pow_in_expression805); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression809);
					a=expression();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression813);
					b=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new PowNode(a, b);
					}
					break;
				case 15 :
					
					{
					match(input,NEGATION,FOLLOW_NEGATION_in_expression836); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression840);
					a=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new UnaryMinusNode(a);
					}
					break;
				case 16 :
					
					{
					match(input,NEGATE,FOLLOW_NEGATE_in_expression869); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expression_in_expression873);
					a=expression();
					state._fsp--;

					match(input, Token.UP, null); 

					node = new NegateNode(a);
					}
					break;
				case 17 :
					
					{
					Number10=(CommonTree)match(input,Number,FOLLOW_Number_in_expression903); 
					node = new AtomNode(Double.parseDouble((Number10!=null?Number10.getText():null)));
					}
					break;
				case 18 :
					
					{
					Bool11=(CommonTree)match(input,Bool,FOLLOW_Bool_in_expression948); 
					node = new AtomNode(Boolean.parseBoolean((Bool11!=null?Bool11.getText():null)));
					}
					break;
				case 19 :
					
					{
					Identifier12=(CommonTree)match(input,Identifier,FOLLOW_Identifier_in_expression995); 
					node = new IdentifierNode((Identifier12!=null?Identifier12.getText():null), variables);
					}
					break;
				case 20 :
				
					{
					pushFollow(FOLLOW_averageExpression_in_expression1036);
					averageExpression13=averageExpression();
					state._fsp--;

					node = averageExpression13;
					}
					break;
				case 21 :
					
					{
					pushFollow(FOLLOW_deviationExpression_in_expression1070);
					deviationExpression14=deviationExpression();
					state._fsp--;

					node = deviationExpression14;
					}
					break;
				case 22 :
					
					{
					pushFollow(FOLLOW_historyExpression_in_expression1102);
					historyExpression15=historyExpression();
					state._fsp--;

					node = historyExpression15;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "expression"



	// $ANTLR start "averageExpression"
	
	public final PSNode averageExpression() throws RecognitionException {
		PSNode node = null;


		PSNode a =null;
		PSNode b =null;

		try {
			
			{
			match(input,AVERAGE,FOLLOW_AVERAGE_in_averageExpression1152); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_expression_in_averageExpression1156);
			a=expression();
			state._fsp--;

			pushFollow(FOLLOW_expression_in_averageExpression1160);
			b=expression();
			state._fsp--;

			match(input, Token.UP, null); 

			node = new AverageNode(a, b);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "averageExpression"



	// $ANTLR start "deviationExpression"
	
	public final PSNode deviationExpression() throws RecognitionException {
		PSNode node = null;


		PSNode a =null;
		PSNode b =null;

		try {
			
			{
			match(input,DEVIATION,FOLLOW_DEVIATION_in_deviationExpression1194); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_expression_in_deviationExpression1198);
			a=expression();
			state._fsp--;

			pushFollow(FOLLOW_expression_in_deviationExpression1202);
			b=expression();
			state._fsp--;

			match(input, Token.UP, null); 

			node = new DeviationNode(a, b);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "deviationExpression"



	// $ANTLR start "historyExpression"
	
	public final PSNode historyExpression() throws RecognitionException {
		PSNode node = null;


		PSNode a =null;

		try {
			
			{
			match(input,HISTORY,FOLLOW_HISTORY_in_historyExpression1230); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_expression_in_historyExpression1234);
			a=expression();
			state._fsp--;

			match(input, Token.UP, null); 

			node = new HistoryNode(a);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "historyExpression"



	// $ANTLR start "buyStatement"
	
	public final PSNode buyStatement() throws RecognitionException {
		PSNode node = null;


		PSNode expression16 =null;

		try {
			
			{
			match(input,BUY,FOLLOW_BUY_in_buyStatement1281); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_expression_in_buyStatement1283);
			expression16=expression();
			state._fsp--;

			match(input, Token.UP, null); 

			node = new BuyNode(expression16, oper);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "buyStatement"



	// $ANTLR start "sellStatement"
	
	public final PSNode sellStatement() throws RecognitionException {
		PSNode node = null;


		PSNode expression17 =null;

		try {
			
			{
			match(input,SELL,FOLLOW_SELL_in_sellStatement1333); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_expression_in_sellStatement1335);
			expression17=expression();
			state._fsp--;

			match(input, Token.UP, null); 

			node = new SellNode(expression17, oper);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return node;
	}
	// $ANTLR end "sellStatement"

	// Delegated rules



	public static final BitSet FOLLOW_block_in_walk60 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BLOCK_in_block92 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_STATEMENTS_in_block108 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_statement_in_block111 = new BitSet(new long[]{0x0200000210000818L});
	public static final BitSet FOLLOW_assignment_in_statement179 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functionCall_in_statement193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ifStatement_in_statement208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_buyStatement_in_statement221 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sellStatement_in_statement234 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNC_CALL_in_functionCall268 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_Println_in_functionCall270 = new BitSet(new long[]{0x200C5FC5C44810E8L});
	public static final BitSet FOLLOW_expression_in_functionCall272 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_ASSIGNMENT_in_assignment300 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_Identifier_in_assignment304 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_assignment308 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_IF_in_ifStatement343 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_EXP_in_ifStatement357 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_ifStatement359 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_block_in_ifStatement363 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_EXP_in_ifStatement381 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_block_in_ifStatement385 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Or_in_expression434 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression438 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression442 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_And_in_expression462 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression466 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression470 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Equals_in_expression489 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression493 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression497 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NEquals_in_expression517 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression521 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression525 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_GTEquals_in_expression545 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression549 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression553 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_LTEquals_in_expression573 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression577 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression581 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_GT_in_expression601 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression605 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression609 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_LT_in_expression630 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression634 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression638 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Add_in_expression659 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression663 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression667 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Subtract_in_expression690 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression694 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression698 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Multiply_in_expression719 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression723 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression727 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Divide_in_expression749 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression753 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression757 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Modulus_in_expression778 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression782 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression786 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Pow_in_expression805 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression809 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_expression813 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NEGATION_in_expression836 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression840 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NEGATE_in_expression869 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_expression873 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_Number_in_expression903 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Bool_in_expression948 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_Identifier_in_expression995 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_averageExpression_in_expression1036 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_deviationExpression_in_expression1070 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_historyExpression_in_expression1102 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_AVERAGE_in_averageExpression1152 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_averageExpression1156 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_averageExpression1160 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_DEVIATION_in_deviationExpression1194 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_deviationExpression1198 = new BitSet(new long[]{0x200C5FC5C44810E0L});
	public static final BitSet FOLLOW_expression_in_deviationExpression1202 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_HISTORY_in_historyExpression1230 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_historyExpression1234 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_BUY_in_buyStatement1281 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_buyStatement1283 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_SELL_in_sellStatement1333 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expression_in_sellStatement1335 = new BitSet(new long[]{0x0000000000000008L});
}

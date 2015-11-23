package interprete;

import java.util.Stack;

import org.antlr.runtime.*;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class PSLexer extends Lexer {
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

	  private IErrorReporter errorReporter = null;
	    public void setErrorReporter(IErrorReporter errorReporter) {
	        this.errorReporter = errorReporter;
	    }
	    
	    public void emitErrorMessage(String msg) {
	        errorReporter.reportError(msg);
	    }


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public PSLexer() {} 
	public PSLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public PSLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "/Users/mauriciomorinelli/Desktop/ProyectosEclipse/PlainStock/src/PS.g"; }

	// $ANTLR start "T__66"
	public final void mT__66() throws RecognitionException {
		try {
			int _type = T__66;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("BUY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__66"

	// $ANTLR start "T__67"
	public final void mT__67() throws RecognitionException {
		try {
			int _type = T__67;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("Boolean"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__67"

	// $ANTLR start "T__68"
	public final void mT__68() throws RecognitionException {
		try {
			int _type = T__68;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("ENDVAR"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__68"

	// $ANTLR start "T__69"
	public final void mT__69() throws RecognitionException {
		try {
			int _type = T__69;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("Float"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__69"

	// $ANTLR start "T__70"
	public final void mT__70() throws RecognitionException {
		try {
			int _type = T__70;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("HISTORY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__70"

	// $ANTLR start "T__71"
	public final void mT__71() throws RecognitionException {
		try {
			int _type = T__71;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("SELL"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__71"

	// $ANTLR start "T__72"
	public final void mT__72() throws RecognitionException {
		try {
			int _type = T__72;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("VAR"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__72"

	// $ANTLR start "T__73"
	public final void mT__73() throws RecognitionException {
		try {
			int _type = T__73;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("avg"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__73"

	// $ANTLR start "T__74"
	public final void mT__74() throws RecognitionException {
		try {
			int _type = T__74;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("dev"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__74"

	// $ANTLR start "Println"
	public final void mPrintln() throws RecognitionException {
		try {
			int _type = Println;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("println"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Println"

	// $ANTLR start "Print"
	public final void mPrint() throws RecognitionException {
		try {
			int _type = Print;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("print"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Print"

	// $ANTLR start "Assert"
	public final void mAssert() throws RecognitionException {
		try {
			int _type = Assert;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("assert"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Assert"

	// $ANTLR start "Size"
	public final void mSize() throws RecognitionException {
		try {
			int _type = Size;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("size"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Size"

	// $ANTLR start "Def"
	public final void mDef() throws RecognitionException {
		try {
			int _type = Def;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("def"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Def"

	// $ANTLR start "If"
	public final void mIf() throws RecognitionException {
		try {
			int _type = If;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("if"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "If"

	// $ANTLR start "Else"
	public final void mElse() throws RecognitionException {
		try {
			int _type = Else;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("else"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Else"

	// $ANTLR start "Return"
	public final void mReturn() throws RecognitionException {
		try {
			int _type = Return;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("return"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Return"

	// $ANTLR start "For"
	public final void mFor() throws RecognitionException {
		try {
			int _type = For;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("for"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "For"

	// $ANTLR start "While"
	public final void mWhile() throws RecognitionException {
		try {
			int _type = While;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("while"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "While"

	// $ANTLR start "To"
	public final void mTo() throws RecognitionException {
		try {
			int _type = To;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("to"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "To"

	// $ANTLR start "Then"
	public final void mThen() throws RecognitionException {
		try {
			int _type = Then;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("then"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Then"

	// $ANTLR start "EndIf"
	public final void mEndIf() throws RecognitionException {
		try {
			int _type = EndIf;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("endif"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EndIf"

	// $ANTLR start "In"
	public final void mIn() throws RecognitionException {
		try {
			int _type = In;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("in"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "In"

	// $ANTLR start "Null"
	public final void mNull() throws RecognitionException {
		try {
			int _type = Null;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("null"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Null"

	// $ANTLR start "Or"
	public final void mOr() throws RecognitionException {
		try {
			int _type = Or;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("or"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Or"

	// $ANTLR start "And"
	public final void mAnd() throws RecognitionException {
		try {
			int _type = And;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("and"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "And"

	// $ANTLR start "Equals"
	public final void mEquals() throws RecognitionException {
		try {
			int _type = Equals;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("=="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Equals"

	// $ANTLR start "NEquals"
	public final void mNEquals() throws RecognitionException {
		try {
			int _type = NEquals;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("<>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NEquals"

	// $ANTLR start "GTEquals"
	public final void mGTEquals() throws RecognitionException {
		try {
			int _type = GTEquals;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GTEquals"

	// $ANTLR start "LTEquals"
	public final void mLTEquals() throws RecognitionException {
		try {
			int _type = LTEquals;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LTEquals"

	// $ANTLR start "Pow"
	public final void mPow() throws RecognitionException {
		try {
			int _type = Pow;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('^'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Pow"

	// $ANTLR start "Excl"
	public final void mExcl() throws RecognitionException {
		try {
			int _type = Excl;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("not"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Excl"

	// $ANTLR start "GT"
	public final void mGT() throws RecognitionException {
		try {
			int _type = GT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GT"

	// $ANTLR start "LT"
	public final void mLT() throws RecognitionException {
		try {
			int _type = LT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LT"

	// $ANTLR start "Add"
	public final void mAdd() throws RecognitionException {
		try {
			int _type = Add;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Add"

	// $ANTLR start "Subtract"
	public final void mSubtract() throws RecognitionException {
		try {
			int _type = Subtract;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Subtract"

	// $ANTLR start "Multiply"
	public final void mMultiply() throws RecognitionException {
		try {
			int _type = Multiply;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Multiply"

	// $ANTLR start "Divide"
	public final void mDivide() throws RecognitionException {
		try {
			int _type = Divide;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Divide"

	// $ANTLR start "Modulus"
	public final void mModulus() throws RecognitionException {
		try {
			int _type = Modulus;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match("mod"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Modulus"

	// $ANTLR start "OBrace"
	public final void mOBrace() throws RecognitionException {
		try {
			int _type = OBrace;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OBrace"

	// $ANTLR start "CBrace"
	public final void mCBrace() throws RecognitionException {
		try {
			int _type = CBrace;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CBrace"

	// $ANTLR start "OBracket"
	public final void mOBracket() throws RecognitionException {
		try {
			int _type = OBracket;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OBracket"

	// $ANTLR start "CBracket"
	public final void mCBracket() throws RecognitionException {
		try {
			int _type = CBracket;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CBracket"

	// $ANTLR start "OParen"
	public final void mOParen() throws RecognitionException {
		try {
			int _type = OParen;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OParen"

	// $ANTLR start "CParen"
	public final void mCParen() throws RecognitionException {
		try {
			int _type = CParen;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CParen"

	// $ANTLR start "SColon"
	public final void mSColon() throws RecognitionException {
		try {
			int _type = SColon;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SColon"

	// $ANTLR start "Assign"
	public final void mAssign() throws RecognitionException {
		try {
			int _type = Assign;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Assign"

	// $ANTLR start "Comma"
	public final void mComma() throws RecognitionException {
		try {
			int _type = Comma;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Comma"

	// $ANTLR start "QMark"
	public final void mQMark() throws RecognitionException {
		try {
			int _type = QMark;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QMark"

	// $ANTLR start "Colon"
	public final void mColon() throws RecognitionException {
		try {
			int _type = Colon;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Colon"

	// $ANTLR start "Bool"
	public final void mBool() throws RecognitionException {
		try {
			int _type = Bool;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0=='t') ) {
				alt1=1;
			}
			else if ( (LA1_0=='f') ) {
				alt1=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}

			switch (alt1) {
				case 1 :
				
					{
					match("true"); 

					}
					break;
				case 2 :
					
					{
					match("false"); 

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Bool"

	// $ANTLR start "Number"
	public final void mNumber() throws RecognitionException {
		try {
			int _type = Number;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			
			{
			mInt(); 

			
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0=='.') ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					
					{
					match('.'); 
					
					int cnt2=0;
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
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

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Number"

	// $ANTLR start "Identifier"
	public final void mIdentifier() throws RecognitionException {
		try {
			int _type = Identifier;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			
			{
			if ( input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '0' && LA4_0 <= '9')||(LA4_0 >= 'A' && LA4_0 <= 'Z')||LA4_0=='_'||(LA4_0 >= 'a' && LA4_0 <= 'z')) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop4;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Identifier"

	// $ANTLR start "Comment"
	public final void mComment() throws RecognitionException {
		try {
			int _type = Comment;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0=='/') ) {
				int LA7_1 = input.LA(2);
				if ( (LA7_1=='/') ) {
					alt7=1;
				}
				else if ( (LA7_1=='*') ) {
					alt7=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 7, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					
					{
					match("//"); 

					
					loop5:
					while (true) {
						int alt5=2;
						int LA5_0 = input.LA(1);
						if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\t')||(LA5_0 >= '\u000B' && LA5_0 <= '\f')||(LA5_0 >= '\u000E' && LA5_0 <= '\uFFFF')) ) {
							alt5=1;
						}

						switch (alt5) {
						case 1 :
							
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop5;
						}
					}

					skip();
					}
					break;
				case 2 :
					
					{
					match("/*"); 

					
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( (LA6_0=='*') ) {
							int LA6_1 = input.LA(2);
							if ( (LA6_1=='/') ) {
								alt6=2;
							}
							else if ( ((LA6_1 >= '\u0000' && LA6_1 <= '.')||(LA6_1 >= '0' && LA6_1 <= '\uFFFF')) ) {
								alt6=1;
							}

						}
						else if ( ((LA6_0 >= '\u0000' && LA6_0 <= ')')||(LA6_0 >= '+' && LA6_0 <= '\uFFFF')) ) {
							alt6=1;
						}

						switch (alt6) {
						case 1 :
							
							{
							matchAny(); 
							}
							break;

						default :
							break loop6;
						}
					}

					match("*/"); 

					skip();
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Comment"

	// $ANTLR start "Space"
	public final void mSpace() throws RecognitionException {
		try {
			int _type = Space;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			skip();
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Space"

	// $ANTLR start "Int"
	public final void mInt() throws RecognitionException {
		try {
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( ((LA9_0 >= '1' && LA9_0 <= '9')) ) {
				alt9=1;
			}
			else if ( (LA9_0=='0') ) {
				alt9=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}

			switch (alt9) {
				case 1 :
					
					{
					matchRange('1','9'); 
					
					loop8:
					while (true) {
						int alt8=2;
						int LA8_0 = input.LA(1);
						if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
							alt8=1;
						}

						switch (alt8) {
						case 1 :
						
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop8;
						}
					}

					}
					break;
				case 2 :
					
					{
					match('0'); 
					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Int"

	// $ANTLR start "Digit"
	public final void mDigit() throws RecognitionException {
		try {
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Digit"

	@Override
	public void mTokens() throws RecognitionException {
		
		int alt10=55;
		alt10 = dfa10.predict(input);
		switch (alt10) {
			case 1 :
				
				{
				mT__66(); 

				}
				break;
			case 2 :
				
				{
				mT__67(); 

				}
				break;
			case 3 :
				
				{
				mT__68(); 

				}
				break;
			case 4 :
				
				{
				mT__69(); 

				}
				break;
			case 5 :
				
				{
				mT__70(); 

				}
				break;
			case 6 :
				
				{
				mT__71(); 

				}
				break;
			case 7 :
				
				{
				mT__72(); 

				}
				break;
			case 8 :
				
				{
				mT__73(); 

				}
				break;
			case 9 :
				
				{
				mT__74(); 

				}
				break;
			case 10 :
				
				{
				mPrintln(); 

				}
				break;
			case 11 :
				
				{
				mPrint(); 

				}
				break;
			case 12 :
				
				{
				mAssert(); 

				}
				break;
			case 13 :
				
				{
				mSize(); 

				}
				break;
			case 14 :
				
				{
				mDef(); 

				}
				break;
			case 15 :
				
				{
				mIf(); 

				}
				break;
			case 16 :
				
				{
				mElse(); 

				}
				break;
			case 17 :
				
				{
				mReturn(); 

				}
				break;
			case 18 :
				
				{
				mFor(); 

				}
				break;
			case 19 :
			
				{
				mWhile(); 

				}
				break;
			case 20 :
				
				{
				mTo(); 

				}
				break;
			case 21 :
			
				{
				mThen(); 

				}
				break;
			case 22 :
				
				{
				mEndIf(); 

				}
				break;
			case 23 :
				
				{
				mIn(); 

				}
				break;
			case 24 :
				
				{
				mNull(); 

				}
				break;
			case 25 :
				
				{
				mOr(); 

				}
				break;
			case 26 :
				
				{
				mAnd(); 

				}
				break;
			case 27 :
				
				{
				mEquals(); 

				}
				break;
			case 28 :
				
				{
				mNEquals(); 

				}
				break;
			case 29 :
				
				{
				mGTEquals(); 

				}
				break;
			case 30 :
				
				{
				mLTEquals(); 

				}
				break;
			case 31 :
				
				{
				mPow(); 

				}
				break;
			case 32 :
				
				{
				mExcl(); 

				}
				break;
			case 33 :
				
				{
				mGT(); 

				}
				break;
			case 34 :
				
				{
				mLT(); 

				}
				break;
			case 35 :
				
				{
				mAdd(); 

				}
				break;
			case 36 :
				
				{
				mSubtract(); 

				}
				break;
			case 37 :
				
				{
				mMultiply(); 

				}
				break;
			case 38 :
				
				{
				mDivide(); 

				}
				break;
			case 39 :
				
				{
				mModulus(); 

				}
				break;
			case 40 :
				
				{
				mOBrace(); 

				}
				break;
			case 41 :
				
				{
				mCBrace(); 

				}
				break;
			case 42 :
				
				{
				mOBracket(); 

				}
				break;
			case 43 :
				
				{
				mCBracket(); 

				}
				break;
			case 44 :
				
				{
				mOParen(); 

				}
				break;
			case 45 :
				
				{
				mCParen(); 

				}
				break;
			case 46 :
				
				{
				mSColon(); 

				}
				break;
			case 47 :
				
				{
				mAssign(); 

				}
				break;
			case 48 :
				
				{
				mComma(); 

				}
				break;
			case 49 :
				
				{
				mQMark(); 

				}
				break;
			case 50 :
				
				{
				mColon(); 

				}
				break;
			case 51 :
				
				{
				mBool(); 

				}
				break;
			case 52 :
				
				{
				mNumber(); 

				}
				break;
			case 53 :
				
				{
				mIdentifier(); 

				}
				break;
			case 54 :
				
				{
				mComment(); 

				}
				break;
			case 55 :
				
				{
				mSpace(); 

				}
				break;

		}
	}


	protected DFA10 dfa10 = new DFA10(this);
	static final String DFA10_eotS =
		"\7\uffff\14\47\1\100\1\103\1\105\4\uffff\1\107\1\47\17\uffff\6\47\1\120"+
		"\1\121\6\47\1\130\4\47\1\135\11\uffff\1\47\1\137\1\47\1\141\1\142\1\143"+
		"\2\47\2\uffff\3\47\1\151\2\47\1\uffff\3\47\1\157\1\uffff\1\160\1\uffff"+
		"\1\47\3\uffff\1\47\1\163\1\164\2\47\1\uffff\2\47\1\171\1\172\1\173\2\uffff"+
		"\1\47\1\176\2\uffff\1\177\1\47\1\172\1\u0081\3\uffff\1\u0082\1\47\2\uffff"+
		"\1\u0084\2\uffff\1\u0085\2\uffff";
	static final String DFA10_eofS =
		"\u0086\uffff";
	static final String DFA10_minS =
		"\1\11\1\125\5\uffff\1\156\1\145\1\162\1\151\1\146\1\154\1\145\1\141\2"+
		"\150\1\157\1\162\3\75\4\uffff\1\52\1\157\17\uffff\1\147\1\163\1\144\1"+
		"\146\1\151\1\172\2\60\1\163\1\144\1\164\1\162\1\154\1\151\1\60\1\145\1"+
		"\165\1\154\1\164\1\60\11\uffff\1\144\1\60\1\145\3\60\1\156\1\145\2\uffff"+
		"\1\145\1\151\1\165\1\60\1\163\1\154\1\uffff\1\156\1\145\1\154\1\60\1\uffff"+
		"\1\60\1\uffff\1\162\3\uffff\1\164\2\60\1\146\1\162\1\uffff\2\145\3\60"+
		"\2\uffff\1\164\1\60\2\uffff\1\60\1\156\2\60\3\uffff\1\60\1\156\2\uffff"+
		"\1\60\2\uffff\1\60\2\uffff";
	static final String DFA10_maxS =
		"\1\175\1\157\5\uffff\1\166\1\145\1\162\1\151\2\156\1\145\1\157\1\150\1"+
		"\162\1\165\1\162\1\75\1\76\1\75\4\uffff\1\57\1\157\17\uffff\1\147\1\163"+
		"\1\144\1\166\1\151\3\172\1\163\1\144\1\164\1\162\1\154\1\151\1\172\1\145"+
		"\1\165\1\154\1\164\1\172\11\uffff\1\144\1\172\1\145\3\172\1\156\1\145"+
		"\2\uffff\1\145\1\151\1\165\1\172\1\163\1\154\1\uffff\1\156\1\145\1\154"+
		"\1\172\1\uffff\1\172\1\uffff\1\162\3\uffff\1\164\2\172\1\146\1\162\1\uffff"+
		"\2\145\3\172\2\uffff\1\164\1\172\2\uffff\1\172\1\156\2\172\3\uffff\1\172"+
		"\1\156\2\uffff\1\172\2\uffff\1\172\2\uffff";
	static final String DFA10_acceptS =
		"\2\uffff\1\3\1\4\1\5\1\6\1\7\17\uffff\1\37\1\43\1\44\1\45\2\uffff\1\50"+
		"\1\51\1\52\1\53\1\54\1\55\1\56\1\60\1\61\1\62\1\64\1\65\1\67\1\1\1\2\24"+
		"\uffff\1\33\1\57\1\34\1\36\1\42\1\35\1\41\1\66\1\46\10\uffff\1\17\1\27"+
		"\6\uffff\1\24\4\uffff\1\31\1\uffff\1\10\1\uffff\1\32\1\11\1\16\5\uffff"+
		"\1\22\5\uffff\1\40\1\47\2\uffff\1\15\1\20\4\uffff\1\25\1\63\1\30\2\uffff"+
		"\1\13\1\26\1\uffff\1\23\1\14\1\uffff\1\21\1\12";
	static final String DFA10_specialS =
		"\u0086\uffff}>";
	static final String[] DFA10_transitionS = {
			"\2\50\1\uffff\2\50\22\uffff\1\50\7\uffff\1\40\1\41\1\31\1\27\1\43\1\30"+
			"\1\uffff\1\32\12\46\1\45\1\42\1\24\1\23\1\25\1\44\2\uffff\1\1\2\uffff"+
			"\1\2\1\3\1\uffff\1\4\12\uffff\1\5\2\uffff\1\6\4\uffff\1\36\1\uffff\1"+
			"\37\1\26\1\47\1\uffff\1\7\2\47\1\10\1\14\1\16\2\47\1\13\3\47\1\33\1\21"+
			"\1\22\1\11\1\47\1\15\1\12\1\20\2\47\1\17\3\47\1\34\1\uffff\1\35",
			"\1\51\31\uffff\1\52",
			"",
			"",
			"",
			"",
			"",
			"\1\55\4\uffff\1\54\2\uffff\1\53",
			"\1\56",
			"\1\57",
			"\1\60",
			"\1\61\7\uffff\1\62",
			"\1\63\1\uffff\1\64",
			"\1\65",
			"\1\67\15\uffff\1\66",
			"\1\70",
			"\1\72\6\uffff\1\71\2\uffff\1\73",
			"\1\75\5\uffff\1\74",
			"\1\76",
			"\1\77",
			"\1\102\1\101",
			"\1\104",
			"",
			"",
			"",
			"",
			"\1\106\4\uffff\1\106",
			"\1\110",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\111",
			"\1\112",
			"\1\113",
			"\1\115\17\uffff\1\114",
			"\1\116",
			"\1\117",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\122",
			"\1\123",
			"\1\124",
			"\1\125",
			"\1\126",
			"\1\127",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\131",
			"\1\132",
			"\1\133",
			"\1\134",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\136",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\140",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\144",
			"\1\145",
			"",
			"",
			"\1\146",
			"\1\147",
			"\1\150",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\152",
			"\1\153",
			"",
			"\1\154",
			"\1\155",
			"\1\156",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"",
			"\1\161",
			"",
			"",
			"",
			"\1\162",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\165",
			"\1\166",
			"",
			"\1\167",
			"\1\170",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"",
			"",
			"\1\174",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\13\47\1\175\16\47",
			"",
			"",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\u0080",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"",
			"",
			"",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"\1\u0083",
			"",
			"",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"",
			"",
			"\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32\47",
			"",
			""
	};

	static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
	static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
	static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
	static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
	static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
	static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
	static final short[][] DFA10_transition;

	static {
		int numStates = DFA10_transitionS.length;
		DFA10_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
		}
	}

	protected class DFA10 extends DFA {

		public DFA10(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 10;
			this.eot = DFA10_eot;
			this.eof = DFA10_eof;
			this.min = DFA10_min;
			this.max = DFA10_max;
			this.accept = DFA10_accept;
			this.special = DFA10_special;
			this.transition = DFA10_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | Println | Print | Assert | Size | Def | If | Else | Return | For | While | To | Then | EndIf | In | Null | Or | And | Equals | NEquals | GTEquals | LTEquals | Pow | Excl | GT | LT | Add | Subtract | Multiply | Divide | Modulus | OBrace | CBrace | OBracket | CBracket | OParen | CParen | SColon | Assign | Comma | QMark | Colon | Bool | Number | Identifier | Comment | Space );";
		}
	}

}

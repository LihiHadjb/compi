/***************************/
/* Based on a template by Oren Ish-Shalom */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;



/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
%state comment

/*****************************************************/
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */
/*****************************************************************************/
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine()    { return yyline + 1; }
	public int getCharPos() { return yycolumn;   }
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
WhiteSpace		= [\t ] | {LineTerminator}
DIGIT           = [0-9]
INTEGER			= 0 | [1-9][0-9]*
LETTER          = [a-zA-Z]
ID				= {LETTER}({LETTER} | {DIGIT} | [_])*

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {
"class"             { return symbol(sym.CLASS); }
"extends"           { return symbol(sym.EXTENDS); }
"public"            { return symbol(sym.PUBLIC); }
"static"            { return symbol(sym.STATIC); }
"void"              { return symbol(sym.VOID); }
"String[]"          { return symbol(sym.STRING_ARR); }
"int"               { return symbol(sym.INT); }
"boolean"           { return symbol(sym.BOOLEAN); }
"new"               { return symbol(sym.NEW); }
"main"              { return symbol(sym.MAIN); }
"int[]"             { return symbol(sym.INT_ARRAY); }
".length"            { return symbol(sym.ARRAY_LENGTH); }
"while"             { return symbol(sym.WHILE); }
"if"                { return symbol(sym.IF); }
"else"              { return symbol(sym.ELSE); }
"System.out.println"{ return symbol(sym.PRINT); }
"true"              { return symbol(sym.TRUE); }
"false"              { return symbol(sym.FALSE); }
"return"              { return symbol(sym.RETURN); }
"this"              { return symbol(sym.THIS); }

";"			   { return symbol(sym.SEMICOLON); }
"!"			   { return symbol(sym.NOT); }
"&&"			   { return symbol(sym.AND); }
"||"			   { return symbol(sym.OR); }
"<"			   { return symbol(sym.LT); }
"="			   { return symbol(sym.ASSIGN); }
"."			   { return symbol(sym.DOT); }
","			   { return symbol(sym.COMMA); }
"+"            { return symbol(sym.PLUS); }
"-"            { return symbol(sym.MINUS); }
"*"            { return symbol(sym.MULT); }
"("            { return symbol(sym.LPAREN); }
")"            { return symbol(sym.RPAREN); }
"{"            { return symbol(sym.LCURLY); }
"}"            { return symbol(sym.RCURLY); }
"["            { return symbol(sym.LSQUARE); }
"]"            { return symbol(sym.RSQUARE; }
{ID}		   { return symbol(sym.ID, new String(yytext())); }
{INTEGER}      { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }
{WhiteSpace}   { /* do nothing */ }

"//".*			   { /* do nothing */ }
"/*"~"*/"			   { /* do nothing */ }

<<EOF>>				{ return symbol(sym.EOF); }
}
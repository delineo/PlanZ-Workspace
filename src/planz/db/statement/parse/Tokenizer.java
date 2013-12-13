package planz.db.statement.parse;

import java.util.*;
import java.util.regex.*;

public class Tokenizer
{
/* 
 * select field1, field2                     <-- 일반 SQL 구문
 *   from $attachParam1 a, $attachParam2 b   <-- Attach 구문이 있는 경우
 *  where field
 *  
 *  @if (:param1 != null)                    <-- 컴파일이 필요한 구문
 *  {
 *    and field1 = :param                    <-- 컴파일을 통해 표시될 SQL 구문
 *  }
 *  
 *  @if (:param1 != null)                    <-- 컴파일이 필요한 구문
 *  {
 *      @if ( :param1 is null )              <-- 중첩된 컴파일이 필요한 구문
 *          and field1 != null               <-- 컴파일을 통해 표시될 SQL 구문
 *          
 *    and field1 = :param                    <-- 컴파일을 통해 표시될 SQL 구문
 *  }
 *  @else if ( true )
 *  {
 *  
 *  }
 *  @else
 *  {
 *  
 *  }
 *  
 *  and field                                <-- 일반 SQL 구문
 *  
 *  @switch( :param2 )                       <-- 컴파일이 필요한 구문
 *  {  
 *    @case "5":
 *    {
 *    and field2 = :param
 *    
 *    } 
 *  }
 */
    public enum Action
    {
        CutLetter
      , Push
      , Pop
    };

    public enum TokenType
    {
        None
      , Check
      , Space
      , NewLine
      , SQuot
      , DQuot
      , Symbol
      , Open
      , Close
      , If
      , Case
      , Else
      , Break
      , Oper
      , OperOpen
      , OperClose
      , OperEQ
      , OperNEQ
      , OperGT
      , OperGTE
      , OperLT
      , OperLTE
      , OperAND
      , OperOR
      , OperIS
      , OperNOT
      , OperNULL
      , Param
      , Attach
      , LineComment
      , OpenComment
      , CloseComment
    };

    public class Token
    {
        public TokenType Type;
        public String    Token;
        
        public Token(TokenType type, String token)
        {
            Type  = type;
            Token = token;
        }

        public Token(TokenType type, char token)
        {
            Type  = type;
            Token = Character.toString(token);
        }
    };

    protected List<Token> _tokens;
    
    protected static final HashMap<Character, Action> _action = new HashMap<Character, Action>()
    {
        private static final long serialVersionUID = 1L;
        {
            put('@', Action.CutLetter);
            put(':', Action.CutLetter);
            put('$', Action.CutLetter);
            put('{', Action.Push     );
            put('}', Action.Pop      );
        }
    };
    protected static final HashMap<String, TokenType> _symbols = new HashMap<String, TokenType>()
    {
        private static final long serialVersionUID = 1L;
        {
            put(" ",    TokenType.Space        );
            put("\t",   TokenType.Space        );
            put("@",    TokenType.Symbol       );  
            put(":",    TokenType.Param        );
            put("$",    TokenType.Attach       );
            put("'",    TokenType.SQuot        );
            put("\"",   TokenType.DQuot        );
            put("\r",   TokenType.NewLine      );
            put("\n",   TokenType.NewLine      );
            put("if",   TokenType.If           );
            put("case", TokenType.Case         ); 
            put("else", TokenType.Else         );
            put("break",TokenType.Break        );
            put("{",    TokenType.Open         );
            put("}",    TokenType.Close        );
            put("=",    TokenType.OperEQ       ); //--
            put("==",   TokenType.OperEQ       );
            put("!",    TokenType.OperNOT      ); //--
            put("!=",   TokenType.OperNEQ      );
            put("<>",   TokenType.OperNEQ      );
            put("<",    TokenType.OperGT       ); //--
            put("<=",   TokenType.OperGTE      );
            put("=<",   TokenType.OperGTE      );
            put(">",    TokenType.OperLT       ); //--
            put(">=",   TokenType.OperLTE      );
            put("=>",   TokenType.OperLTE      );
            put("and",  TokenType.OperAND      );
            put("or",   TokenType.OperOR       );
            put("is",   TokenType.OperIS       );
            put("not",  TokenType.OperNOT      );
            put("null", TokenType.OperNULL     );
            put("(",    TokenType.OperOpen     );
            put(")",    TokenType.OperClose    );
            put("-",    TokenType.LineComment  ); //--
            put("--",   TokenType.LineComment  );
            put("/",    TokenType.OpenComment  ); //--
            put("/*",   TokenType.OpenComment  );
            put("*",    TokenType.CloseComment ); //--
            put("*/",   TokenType.CloseComment );
        }
        
//        public TokenType put(String key, TokenType type)
//        {
//            return type;
//        }
    };
    
    protected static double getKey(String src)
    {
        double key = 0;

        int    length = src.length();

        return key;
    }
    
    protected int getLetterPos(char[] stmt, int index)
    {
        int pos = index;
        
        while(true)
        {
            if (!Character.isJavaIdentifierPart(stmt[pos]))
                break;
            
            pos++;
        }

        return pos;
    }
    
    protected int getCommentPos(char[] stmt, int index, boolean inline)
    {
        int       pos  = index;
        TokenType type = TokenType.None;
        
        while(true)
        {
            // 한 줄 주석인 경우 TokenType 이 NewLine 일때까지 주석으로 간주한다.
            if (inline)
            {
                type = _symbols.get(Character.toString(stmt[pos]));
                if (TokenType.NewLine.equals( type ))
                    break;

                pos++;
                continue;
            }
            
            // 문단 주석인 경우 '*/' 문자가 나올때까지 주석으로 간주한다.
            // 즉 2개 문자 단위로 검사한다.
            type = _symbols.get(( new char[] { stmt[pos], stmt[pos+1] }).toString());
            if (TokenType.CloseComment.equals( type ))
                break;
            
            pos++;
        }
        
        return pos;
    }
    
    protected void tokenize(String statement, int index)
    {
        _tokens.clear();
        
        char[] stmt = statement.toCharArray();
        int  length = stmt.length;

        StringBuilder src = new StringBuilder();

        Token     token   = null;
        TokenType preType = TokenType.None; 
        TokenType curType = TokenType.None; 
        String    key     = null;
        String    tmpChar = null;
        String    cmpChar = null;
        
        int       nextPos = 0;

        for(int idx=index; idx < length; idx++)
        {
            tmpChar = Character.toString(stmt[idx]);
            key = src.toString().toLowerCase();
            
            if (_symbols.containsKey(tmpChar))
            {   // 단자(글자 1개) 인 경우는 공백 및 주변 문자에 상관없이 구분되어져야 하므로,
                // 이전까지 등록된 글자(key) 와 현재의 값을 구분해준다.
                
                // 현재 Token 유형을 가져온다.
                curType = _symbols.get(tmpChar);
                
                // TokenType이 Space인 경우는 앞선 토큰 유형에 포함시켜 처리한다. 
                if (curType.equals( TokenType.Space ))
                {
                    src.append(stmt[idx]);
                    continue;
                }
                // TokenType이 Symbol, Param, Attach 인 경우
                // 다음 문자가 Letter 문자여야하며, Letter 문자만을 하나의 Token으로 처리한다.
                else if
                (
                    curType.equals( TokenType.Symbol  )
                 || curType.equals( TokenType.Param   )
                 || curType.equals( TokenType.Attach  )
                )
                {
                    token   = new Token(TokenType.None, src.toString());
                    _tokens.add(token);
                    src.setLength(0);
                    
                    nextPos = getLetterPos(stmt, idx+1);
                    src.append(new String(stmt, idx, nextPos - idx));
                    
                    token   = new Token(curType, src.toString());
                    _tokens.add(token);
                    src.setLength(0);

                    idx = nextPos;
                }
                // TokenType이 Symbol, Param, Attach, OperEQ, OperNOT, OperGT, OperLE 인 경우
                // 두번째 문자에 의해 TokenType이 결정될 수 있으므로, 다음문자를 검사한다.
                else if
                (
                    curType.equals( TokenType.OperEQ  )
                 || curType.equals( TokenType.OperNOT )
                 || curType.equals( TokenType.OperGT  )
                 || curType.equals( TokenType.OperLT  )
                )
                {
                    // 앞서 단문자의 경우에 대하여 두문자의 경우 문법상 
                    cmpChar = new String(new char[] { stmt[idx], stmt[idx++] });
                    if (_symbols.containsKey(cmpChar))
                    {
                        token   = new Token(TokenType.None, src.toString());
                        _tokens.add(token);
                        src.setLength(0);
                        
                        curType = _symbols.get(cmpChar);
                        token   = new Token(curType, cmpChar);
                        _tokens.add(token);
                        
                        continue;
                    }
                    
                }
                else if 
                (
                    curType.equals( TokenType.LineComment )
                 || curType.equals( TokenType.OpenComment )
                )
                {
                    token   = new Token(TokenType.None, src.toString());
                    _tokens.add(token);
                    src.setLength(0);
                    
                    nextPos = getCommentPos(stmt, idx+1, curType.equals( TokenType.LineComment ));
                    src.append(new String(stmt, idx, nextPos - idx));
                    
                    token   = new Token(curType, src.toString());
                    _tokens.add(token);
                    src.setLength(0);

                    idx = nextPos;
                }
                
                continue;
            }
            
            src.append(stmt[idx]);

            if (_symbols.containsKey(key))
            {
                curType = _symbols.get(key);
                token   = new Token(curType, key);
                _tokens.add(token);
                src.setLength(0);
            }
        }
        
        if (src.length() > 0)
        {
            token = new Token(TokenType.None, src.toString());
            _tokens.add(token);
            src.setLength(0);
        }
    }
    
    
    protected String __symbols = new StringBuilder()
    .append("[ \t]"    )
    .append("@"    )
    .append(":"    )
    .append("$"    )
    .append("'"    )
    .append("\""   )
    .append("\r"   )
    .append("\n"   )
    .append("if"   )
    .append("case" )
    .append("else" )
    .append("break")
    .append("{"    )
    .append("}"    )
    .append("="    )
    .append("=="   )
    .append("!"    )
    .append("!="   )
    .append("<>"   )
    .append("<"    )
    .append("<="   )
    .append("=<"   )
    .append(">"    )
    .append(">="   )
    .append("=>"   )
    .append("and"  )
    .append("or"   )
    .append("is"   )
    .append("not"  )
    .append("null" )
    .append("("    )
    .append(")"    )
    .append("-"    )
    .append("--"   )
    .append("/"    )
    .append("/*"   )
    .append("*"    )
    .append("*/"   )
    .toString();
    
    
    public Tokenizer(String statement)
    {
        _tokens = new ArrayList<Token>();
        
        long start = System.currentTimeMillis();
        
        //tokenize(statement, 0);
        
        Pattern p = Pattern.compile(":[::]"); 
        Matcher m = p.matcher(statement); 
        
        while(m.find())
        {
            String ttt = m.group();
            System.out.println(ttt);
        }
        long end = System.currentTimeMillis();
        System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
    }
}

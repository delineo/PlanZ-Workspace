package planz.test;

import planz.core.compress.*;
import planz.db.statement.parse.*;

public class Test
{
    public static void main(String args[])
    {

        StringBuilder sb = new StringBuilder();

        sb.append("select tbls.TABLE_NAME  --테이블명        \n");
        sb.append("     , tcmt.COMMENTS as TABLE_COMMENT     \n");
        sb.append("     , cols.COLUMN_NAME -- 컬럼 명        \n");
        sb.append("     , ccmt.COMMENTS as COLUMN_COMMENT    \n");
        sb.append("  from USER_TABLES       tbls             \n");
        sb.append("     , USER_TAB_COMMENTS tcmt             \n");
        sb.append("     , USER_TAB_COLUMNS  cols             \n");
        sb.append("     , USER_COL_COMMENTS ccmt             \n");
        sb.append(" where tbls.TABLE_NAME = tcmt.TABLE_NAME  \n");
        sb.append("   and tbls.TABLE_NAME = cols.TABLE_NAME  \n");
        sb.append("                                          \n");
        sb.append("   and cols.TABLE_NAME  = ccmt.TABLE_NAME \n");
        sb.append("   and cols.COLUMN_NAME = ccmt.COLUMN_NAME\n");
        sb.append("                                          \n");
        sb.append("   and tbls.TABLE_NAME = :TABLE_NAME      \n");
        sb.append("   --and tcmt.COMMENTS like '%:CMNT_TXT%' \n");
        sb.append("   --and cols.COLUMN_NAME = :COL_NM       \n");
        sb.append("                                          \n");
        sb.append(" order by tbls.TABLE_NAME, cols.COLUMN_ID \n");
        
        byte[] ret1 = QuickLZ.compress("뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?뭐가되도 되지않을까하는데....움화화화 확실히 잘되고 있어...영문자도 잘될거야.. 당근 숫자도 될것이고.... ㅋㅋㅋㅋ`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?".getBytes(), 1);
        byte[] ret2 = QuickLZ.decompress(ret1);
        
        String test = new String(ret2);
        //Tokenizer tk = new Tokenizer(sb.toString());
    }
}

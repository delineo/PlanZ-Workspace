package planz.core.session;

import java.lang.reflect.*;
import java.util.*;

import javax.servlet.http.*;
import org.apache.log4j.*;

import planz.util.HashMapEx.*;

public class SessionManager 
{
    static Logger log = Logger.getLogger(SessionManager.class);   

    /*
     * Session을 찾기 위한 Key 정보
     */
    protected class SessionKey
    {
        public String SessionID   = "";    // Session ID
        public String UserID      = "";    // 사용자 ID
        public String ConnectType = "";    // 접속유형
        
        public SessionKey() {}
        public SessionKey(String sessionId)
        {
            SessionID   = sessionId;
        }
        
        public SessionKey(String sessionId, String userId, String connectType)
        {
            SessionID   = sessionId;
            UserID      = userId;
            ConnectType = connectType;
        }
        
        public boolean equals(String sessionId)
        {
            return (SessionID == sessionId);
        }
        
        public boolean equals(String userId, String connectType)
        {
            return ((SessionID == userId) && (ConnectType == connectType));    
        }

        @Override
        public boolean equals(Object key)
        {
            if (this.getClass() != key.getClass())
                return false;
            
            SessionKey tmpKey = (SessionKey)key;

            return (
                       (SessionID   == tmpKey.SessionID  )
                     &&(UserID      == tmpKey.UserID     )
                     &&(ConnectType == tmpKey.ConnectType)
                   );
        }
    }

    static SessionManager _instance = new SessionManager();
    public static SessionManager getInstance() { return _instance; }

    // Session 정보 저장소
    // 접속한 사용자의 Session을 저장하기위한 저장소
    private HashMap<String,     Session> _sessions = new HashMap<String,     Session>();

    // 사용자 Session 정보 생성
    public Session createSession(HttpSession session)
    {
        // Session 생성시 Session Manager에 등록된다.
        return createSession(new Session(session));
    }
    
    public Session createSession(Session session)
    {
        // 그럴일은 없겠지만은...
        // 기존에 동일한 Session ID가 존재한다면, 해당 객체를 제거한다.
        destroySession(session);

        String sessionId = session.getId();
        log.info("CREATE SESSION : " + sessionId);

        // Session의 현재 상태를 기록한다.
        session.setAttribute("__SESSION_STATE__", "CREATED");

        // Session 저장소에 저장한다.
        _sessions.put(sessionId, session);
        return session;
    }

    // 사용자 Session 정보 제거
    public void destroySession(HttpSession session)
    {
        destroySession(session.getId());
    }
    public void destroySession(Session session)
    {
        destroySession(session.getId());
    }

    public void destroySession(String sessionId)
    {
        Session elem = null;

        if (_sessions.containsKey(sessionId))
        {   // Session 저장소에 기존에 동일한 Session ID가 존재할 경우
            // 기존 객체를 제거한다.
            log.info("DESTROY SESSION : " + sessionId);

            elem = (Session)_sessions.remove(sessionId);
            elem.close();
        }
    }

    // 현재 등록된 Session 갯수를 반환한다.
    public int getCount()
    {
        return _sessions.size();
    }
    

    // SessionManager에 현재 Session을 등록한다.
    // * 주:  Session은 ApplicationContext가 종료되더라도 Session은 종료되지 않고, 잔존해 있다(언제 종료되는지는 모름 ㅡㅡ?)
    //        다음번 ApplicationContext가 재 생성되더라도, 기존 Session은 존재할 수 있는 여지가 있으나, ApplicationContext가 종료되었으므로,
    //        관리를 위한 정보를 모두 제거된 상태이다. 따라서, 관리를 위해 현재 Session을 SessionManager에 등록한다.
    //        물론, 신규로 생성된 Session 또한 등록한다. 
    //        이런 현상은 프로젝트 Rebuild 또는 deploy 시 기존 연결되어 있는 Session들에 대해 발생한다.
    public Session bind(HttpSession session)
    {
        String sessionId = session.getId();

        Enumeration<String> attNames = session.getAttributeNames();
        
        String strLog = "# SESSION ATTRIBUTES >>\n";
        while(attNames.hasMoreElements())
        {
            String name = attNames.nextElement();
            strLog += String.format("%s : %s\n", name, session.getAttribute(name));
        }
        log.debug(strLog);
        
        Session bindSession = null;
        
        if (!_sessions.containsKey(sessionId))
        {
            // 현재 Session이 관리되고 있지 않다면, 신규로 생성된 Session이거나 잔류하는 Session이다.
            // 만약, Session에 이전 정보가 존재하고 있다면, 해당 정보로 Session Manager에 등록한다.
            bindSession = new Session(session);
            
            log.info("BIND SESSION : " + sessionId);

            // Session 저장소에 저장한다.
            _sessions.put(sessionId, bindSession);
        }
        else
        {
            bindSession = _sessions.get(sessionId);
        }
        
        return bindSession;
    }
    
    // 이거 쓸일이 있을까..
    // 현재 등록된 Session 모두를 반복 처리해야할 때 사용한다.
    public class Run {}
    public void each(Run run)
    {
        try
        {
            Class<?> clsRun = run.getClass();
            Method mtdRun = clsRun.getMethod("run", String.class, Object.class);
            if (mtdRun == null) return;

            if (!mtdRun.isAccessible()) mtdRun.setAccessible(true);
            
            Set<?> keys = _sessions.keySet();
            
            for (Iterator<?> itor = keys.iterator(); itor.hasNext();)
            {
                String key = (String)itor.next();            
                Object val = _sessions.get(key);
                
                Object isContinue = mtdRun.invoke(run, key, val);
                if (isContinue.equals(false))
                    break;
            }
        }
        catch(Exception ex) {}
    }
}

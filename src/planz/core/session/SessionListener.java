package planz.core.session;

import javax.servlet.http.*;

import org.apache.log4j.*;

import planz.core.*;

public class SessionListener implements HttpSessionListener
{
    static Logger log = Logger.getLogger(Service.class);

    /* 이 클래스가 동작하기 위해서는 web.xml에 아래 정보를 입력하여야 한다.

       <listener>
           <listener-class>planz.core.session.SessionListener</listener-class>
       </listener>
    */
    
    @Override
    public void sessionCreated(HttpSessionEvent se)
    {
        log.debug("=> Session 생성");
        
        // Session이 생성되는 시점
        SessionManager sm = SessionManager.getInstance();
        sm.createSession(se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se)
    {
        log.debug("=> Session 종료");
        // Session이 종료되는 시점
        SessionManager sm = SessionManager.getInstance();
        sm.destroySession(se.getSession());
    }
}

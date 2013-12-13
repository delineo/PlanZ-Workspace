package planz.core.session;

import java.sql.*;

import javax.servlet.*;

import org.apache.log4j.*;

import planz.db.*;

public class ContextListener implements ServletContextListener
{
    static Logger log = Logger.getLogger(ContextListener.class);
    
    /* 이 클래스가 동작하기 위해서는 web.xml에 아래 정보를 입력하여야 한다.

       <listener>
           <listener-class>planz.core.session.ContextListener</listener-class>
       </listener>
    */

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        // WAS가 처음으로 구동되어질때 발생함.
        // 전역적으로 사용할 DB Connection Pool 을 설정한다.
        log.debug(">> Initialize Context");
        
        Connection conn = DbManager.getInstance().getSysConnection();
        log.debug(">> Load PlanZ DataSource...");
        
        try
        {
            Statement stmt = conn.createStatement();
            stmt.execute("select * from TB_SYS_CONFIG");
            ResultSet rs = stmt.getResultSet();
            rs.beforeFirst();
            
            while(rs.next())
            {
                String userId = String.valueOf(rs.getObject("USER_ID"));
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        log.debug(">> Load Confiuration...");
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        // WAS가 종료될때 발생함.
        // 현재 수행되고 있는 또는 생성되어져 있는 DB Connection Pool 또는 Session 정보들을 모두 제거한다.
        // 테스트 해보니 Session의 종료 이벤트는 발생하지 않는다.
        // 신기하게도 다음 Context가 초기화 될때 Session은 그대로 살아 있다. ㅡㅡ???
        // 당연히 Context가 종료되었으므로 Session은 아무런 의미가 없을 것같은데....
        // 아무튼 문제는 다시 Context가 초기화될때 해당 Session 정보를 찾을 수 없다는 것이다.
        // Session이 살아 있으므로, Session 생성 이벤트는 당연히 발생하지 않는다.
        // 따라서 Context가 종료될때 모든 Session을 종료시킨다(어짜피 Session이 살아있어도 Context가 종료되었으므로
        // Client에서 정보를 받아 볼 가능성은 없는데다 설령 살려둔다하더라도, 정보를 찾을 수 없다).
        // 만약 이러한 과정에 따른 영향도는 아직 확인하지 못하였으므로, 영향도에 대한 검사가 필요한다.
        // 이룬 똥~~~!! ㅡㅡ^, Session을 강제 종료 했는데도, 정보가 남아 있고, 이후 Session이 재 생성되지 않는다.
        // 따라서, Context를 종료할때 Session을 종료해봐야 의미가 없다.
        // Client로부터 요청이 왔을때 SessionManager에서 관련 정보를 binding 할수 있도록 Session에다가 관련 정보를
        // 기록하여 찾을 수 있도록 해야겠다 ㅡㅡ*

        log.debug(">> Destroyed Context");
    }

}

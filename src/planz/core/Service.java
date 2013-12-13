package planz.core;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.log4j.*;

import planz.core.session.*;

/**
 * Servlet implementation class Service
 */
public class Service extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	static Logger log = Logger.getLogger(Service.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Service()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    private Session bindSession(HttpSession session)
    {
        SessionManager mgr = SessionManager.getInstance();
        return mgr.bind(session);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException 
	{
	    log.debug("Call Get Servlet");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest req, HttpServletResponse res)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
	    throws ServletException, IOException
	{
	    log.debug("Call Get Post");
	    
        // Session 생성 >> 초기 접속시 여기서 Session이 생성되어지며, SessionListener를 통한 속성들이 담겨지게 된다.
        // 주의 : Application Context가 종료되더라도 Session은 종료되지 않고 남아있다(WAS 전체 종료인 경우는 제외).
        //        따라서, 현재 Session의 상태를 조사(신규 생성여부 확인)하여 신규가 아닌경우 Session 정보를 복원한다.        
	    Session session = bindSession(req.getSession());

	    Parameters params = new Parameters(req, res);

	    
	    
	    log.debug("그람.. 이것도 되는 거냐????");
		// Session 종료
		//session.invalidate();
 	}
}

package planz.core.session;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import planz.util.*;

public class Session extends HashMapEx
{
	private static final long serialVersionUID = 1L;
	
	protected HttpSession _session = null;
	
	public Session() { super(); }
	public Session(HttpSession session)
	{
		super();

		// Session 설정
		_session = session;
	}
	
	// Session ID를 가져온다.
	public String getSessionId() 
	{
	    return _session.getId();
	}
	
	public ServletContext getContext()
	{
	    return _session.getServletContext();
	}

	// Session 상태를 반환한다.
    public String getSessionState()
    {
        Object value = getAttribute("__SESSION_STATE__");
        return (value==null)?"":String.valueOf(value);
    }

    // Session 유형을 설정한다.
    public void setSessionType(String type)
    {
        setAttribute("__SESSION_TYPE__", type);
    }

    // Session 유형을 반환한다.
    public String getSessionType()
    {
        Object value = getAttribute("__SESSION_TYPE__");
        return (value==null)?"":String.valueOf(value);
    }

    public String getContextParam(String name)
    {
        if (_session == null) return "";
        
        ServletContext context = _session.getServletContext();

        return context.getInitParameter(name);
    }

	// Session을 강제 종료한다.
	public void close()
	{
	    try
	    {
	        clear();
	        _session.invalidate();
	    }
	    // 만약, Session이 이미 종료되었다면, IllegalStateException 예외가 발생하는데, 이때는 무시한다.
	    catch(Exception e) {} 
	}
	
	// HttpSession Object Interface /////////////////////////////////////////////////////////
    public String getId()
    {
        return (_session == null)?"":_session.getId();
    }
    public boolean isNew()
    {
        return (_session == null)?null:_session.isNew();
    }
    public Enumeration<String> getAttributeNames()
    {
        return (_session == null)?null:_session.getAttributeNames();
    }
    public void setAttribute(String name, Object value)
    {
        if (_session != null)
            _session.setAttribute(name, value);
    }
    public Object getAttribute(String name)
    {
        return (_session == null)?null:_session.getAttribute(name);
    }
    // HttpSession Object Interface /////////////////////////////////////////////////////////
}

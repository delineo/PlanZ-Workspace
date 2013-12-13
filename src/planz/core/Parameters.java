package planz.core;

import javax.servlet.http.*;

import planz.util.*;

public class Parameters extends HashMapEx
{
	private static final long serialVersionUID = 1L;

	protected HttpServletRequest  _request;
	protected HttpServletResponse _response;

	public Parameters(HttpServletRequest request, HttpServletResponse response)
	{
	    super(request.getParameterMap());
	    
	    _request  = request;
	    _response = response;
	}
}

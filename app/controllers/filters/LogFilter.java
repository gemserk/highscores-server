package controllers.filters;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.junit.After;

import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;

public class LogFilter extends Controller{

	public static final String REQUEST_ID_MDC = "requestId";
	public static final String ADDRESS_MDC = "address";
	public static final String USERCASE = "usecase";

	static AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());
	
	static Logger logger = Logger.getLogger(LogFilter.class);
	
	@Before
	static void pre(){
	
		String remoteAddress = request.remoteAddress;
		MDC.put(ADDRESS_MDC, remoteAddress);
		
		long nid = atomicLong.incrementAndGet();
	    String requestId = Long.toString(nid, 26);
	    MDC.put(REQUEST_ID_MDC, requestId);
	}
	
	@After
	static void after(){
		MDC.remove(ADDRESS_MDC);
		MDC.remove(REQUEST_ID_MDC);
	}
}

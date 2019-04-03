package com.tsmms.skoop.exception;

import org.slf4j.MDC;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import java.util.UUID;

/**
 * This class will be notified when requests coming in and out of scope in a web component.
 * It adds a unique identifier to slf4j and we use it in logging.pattern to differentiate between all logs that belong to a specific request.
 *
 * @author hadi on 9/20/2018.
 */
@WebListener
public class Slf4jUniqueIdentifierRequestListener implements ServletRequestListener {

	@Override
	public void requestInitialized(ServletRequestEvent arg0) {
		MDC.put("RequestId", UUID.randomUUID().toString().replace("-", ""));
	}

	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		MDC.clear();
	}
}

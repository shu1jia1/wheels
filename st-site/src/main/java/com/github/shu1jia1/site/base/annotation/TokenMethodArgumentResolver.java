package com.github.shu1jia1.site.base.annotation;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.support.AbstractNamedValueMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

/**
 * <p>文件名称: TokenMethodArgumentResolver.java/p>
 * <p>文件描述: </p>
 * @version 1.0
 * @author  lov
 */
public class TokenMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

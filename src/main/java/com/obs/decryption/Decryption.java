package com.obs.decryption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.json.simple.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class Decryption extends RequestBodyAdviceAdapter {
 
	
	@Around("@annotation(Traceable)")
	public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {

		Object result = joinPoint.proceed();
		Object target = joinPoint.getTarget();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		@SuppressWarnings("unused")
		String json = ow.writeValueAsString(target);
		return result;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasParameterAnnotation(Decrypt.class);
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

		try (InputStream inputStream = inputMessage.getBody()) {

			byte[] body = StreamUtils.copyToByteArray(inputStream);
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Map<String, Object>> tr = new TypeReference<Map<String, Object>>() {
			};
			Map<String, Object> map = mapper.readValue(body, tr);
			JSONObject json = new JSONObject(map);
				body = json.toString().getBytes();
				log.info("raw: {}", new String(body));
			
			return new DecodeHttpInputMessage(inputMessage.getHeaders(), new ByteArrayInputStream(body));
		}
	}

	
}

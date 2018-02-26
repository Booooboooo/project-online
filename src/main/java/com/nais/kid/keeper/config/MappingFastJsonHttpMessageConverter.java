/**
 * 
 */
package com.nais.kid.keeper.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author zhujiawen
 * 
 */
public class MappingFastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

	private static String charset = "UTF-8";
	
	// fastjson特性参数
	private SerializerFeature[] serializerFeature;

	public SerializerFeature[] getSerializerFeature() {
		return serializerFeature;
	}

	public void setSerializerFeature(SerializerFeature[] serializerFeature) {
		this.serializerFeature = serializerFeature;
	}

	public MappingFastJsonHttpMessageConverter() {
		super(new MediaType("application", "json", Charset.forName(charset)));
	}
	
	public String getCharset() {
		return charset;
	}

	@SuppressWarnings("static-access")
	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		while ((i = inputMessage.getBody().read()) != -1) {
			baos.write(i);
		}
		return JSON.parseArray(baos.toString(), clazz);
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		
		String jsonpCallback = null;

		RequestAttributes reqAttrs = RequestContextHolder.currentRequestAttributes();
		if (reqAttrs instanceof ServletRequestAttributes) {
			jsonpCallback = ((ServletRequestAttributes) reqAttrs).getRequest().getParameter("jsonpCallback");
		}
		
		String jsonString = null;
				
		if(o instanceof String) {
			jsonString = (String)o;
		} else {
			jsonString = JSON.toJSONString(o, serializerFeature);
		}
		
		OutputStream out = outputMessage.getBody();
		if(jsonpCallback != null && !"".equals(jsonpCallback)) {
			out.write(String.format("%s(%s)", jsonpCallback, jsonString).getBytes(Charset.forName(charset)));
		} else {
			out.write(jsonString.getBytes(Charset.forName(charset)));
		}
		
		out.flush();
		
	}

}

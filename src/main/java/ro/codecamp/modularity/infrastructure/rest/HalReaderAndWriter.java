package ro.codecamp.modularity.infrastructure.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

@Provider
@Produces(Constants.HAL_JSON_TYPE_STRING)
public class HalReaderAndWriter implements
		MessageBodyReader<ReadableRepresentation>,
		MessageBodyWriter<ReadableRepresentation> {

	private JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return mediaType.isCompatible(Constants.HAL_JSON_TYPE)
				&& ReadableRepresentation.class.isAssignableFrom(type);
	}

	@Override
	public ReadableRepresentation readFrom(Class<ReadableRepresentation> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		return halFactory.readRepresentation(new InputStreamReader(entityStream));
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return mediaType.isCompatible(Constants.HAL_JSON_TYPE)
				&& (ReadableRepresentation.class.isAssignableFrom(type));
	}

	@Override
	public long getSize(ReadableRepresentation representation, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(ReadableRepresentation representation, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		representation.toString(mediaType.toString(), new OutputStreamWriter(
				entityStream));
	}
}

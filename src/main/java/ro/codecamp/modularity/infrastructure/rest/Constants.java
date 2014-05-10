package ro.codecamp.modularity.infrastructure.rest;

import javax.ws.rs.core.MediaType;

public final class Constants {

	public static final String HAL_JSON_TYPE_STRING = "application/hal+json";
	public static final MediaType HAL_JSON_TYPE = MediaType
			.valueOf(HAL_JSON_TYPE_STRING);

	private Constants() {
	}

}

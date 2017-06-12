package com.tanmay.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
/**
 * 
 * @author tanmay
 *
 */
@Path("/api")
public class MainController {

	@GET
	@Path("/test")
	public Response test() {

		String output = "Request received";

		return Response.status(200).entity(output).build();

	}

}
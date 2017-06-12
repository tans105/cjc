package com.tanmay.controller;

import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.tanmay.constants.Constants;

import au.com.bytecode.opencsv.CSVReader;

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

	@SuppressWarnings("resource")
	@GET
	@Path("/convert")
	public Response convert() {
		String csvFile = Constants.CSV_LOCATION;
		CSVReader reader = null;
		String output = "";
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] header = reader.readNext();
			String headers = header[0] + " " + header[1] + " " + header[2];
			System.out.println(headers);
			String[] line;
			while ((line = reader.readNext()) != null) {
				output += "id= " + line[0] + ", name= " + line[1] + " , country=" + line[2];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(output).build();
	}
}
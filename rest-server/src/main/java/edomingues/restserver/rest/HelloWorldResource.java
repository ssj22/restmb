package edomingues.restserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import edomingues.restserver.model.Saying;


@Path("/mb")
public class HelloWorldResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String sayHello(@PathParam("name") String name) {		
		return getMotherboardSN();
	}

	private String getMotherboardSN() {
	  String result = "";
	    try {
	      File file = File.createTempFile("realhowto",".vbs");
	      file.deleteOnExit();
	      FileWriter fw = new java.io.FileWriter(file);

	      String vbs =
	         "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
	        + "Set colItems = objWMIService.ExecQuery _ \n"
	        + "   (\"Select * from Win32_BaseBoard\") \n"
	        + "For Each objItem in colItems \n"
	        + "    Wscript.Echo objItem.SerialNumber \n"
	        + "    exit for  ' do the first cpu only! \n"
	        + "Next \n";

	      fw.write(vbs);
	      fw.close();
	      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
	      BufferedReader input =
	        new BufferedReader
	          (new InputStreamReader(p.getInputStream()));
	      String line;
	      while ((line = input.readLine()) != null) {
	         result += line;
	      }
	      input.close();
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }
	    return result.trim();
	  }
}

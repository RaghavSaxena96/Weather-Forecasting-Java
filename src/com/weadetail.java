package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;
import com.google.gson.JsonSyntaxException;


/**
 * Servlet implementation class weadetail
 */
public class weadetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public weadetail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// Getting Parameters 
		
		String cityname=request.getParameter("city");
		
		System.out.println("City Name Entered By User is = "+cityname);
		
		PrintWriter out=response.getWriter();
		out.print("<br><br> <h3><span style=\"color:red;\"> Weather Forecast for the  "+cityname+" City is provided below </span></h3><br/>");
		
		// Getting Location in lan and lat of City Entered
		
		String req="https://api.mapbox.com/geocoding/v5/mapbox.places/"+cityname+".json?access_token=pk.eyJ1IjoicmFnaGF2c2F4ZW5hOTYiLCJhIjoiY2tiNnJzMGM3MDFsdTJzbzYwY2pzODI5NSJ9.-lhtOu5vAQhYrF59FbEwxw";
		
		out.print("<div id=\"back\"><a href=\"index.html\">Back to Main Page</a></div>");
		
		URL url1=new URL(req);// For Finding Location with LAN And LAT
		
		RequestDispatcher rd=null;
		
		try 
		{
			
			BufferedReader br = new BufferedReader(new InputStreamReader(url1.openStream()));
			String str = "";
			while (null != (str = br.readLine())) 
			{
				System.out.println("Getting Info of Location");
				System.out.println(str);
			
                //-------------------Converting to JsonObject using Json--------------------------------------------------
				
				JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
				
				JsonArray feature=(JsonArray) jsonObject.get("features");
				
				//System.out.println(feature);
				//JsonObject arr=feature.getAsJsonObject();
				
				 ArrayList<String> list = new ArrayList<String>();
				 
				 //JSONArray jsonArray = (JSONArray)jsonObject;
			
				 //JsonArray properties = jsonProfile.getAsJsonArray("properties");
				  
				 
				 
                //----------------------------Storing Coordinates as String--------------------------------------
				 
				 if (feature != null) 
				 { 
				   int len = feature.size();
				   for (int i=0;i<len;i++){ 
				   String u=feature.get(i).toString();
				   int pre=0;
				   while(u.indexOf("center",pre)!=-1)
				   {
					   int y=u.indexOf("center",pre);
					   int t=u.indexOf("geometry",y+1);
					   list.add(u.substring(y+8,t-2));
					   pre=t+1;
				   }
				   }
				   
				   for(int i=0;i<list.size();i++)
				   {
					   System.out.println(list.get(i));
				   }
				 }
		         
		         
				
				/** Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
			      for(Map.Entry<String, JsonElement> entry: entries) {
			         System.out.println(entry.getKey());
			      }**/
				
				// Getting Weather using LAT and LAN
				 
				
					
				
				for(int i=0;i<list.size();i++)
				{	
					try 
					{
						String q=list.get(i);
						int comma=q.indexOf(",");
						System.out.println("Weather report");
						String lat=q.substring(1,comma);
						String lon=q.substring(comma+1,q.length()-1);
						
						String locreq="https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=9f401a76cebac7dfc71ec397046a1b20";
						URL uri=new URL(locreq);
						String wea="";
						BufferedReader br1 = new BufferedReader(new InputStreamReader(uri.openStream()));
						while(null != (wea= br1.readLine()))
						{
							System.out.println(wea);
							JsonObject jsonObject2 = new JsonParser().parse(wea).getAsJsonObject();
							JsonObject mainer=jsonObject2.getAsJsonObject("main");
							out.print("<br/><h2> Weather Report for "+cityname+" with center "+q+" </h2><br>");
							String weat=jsonObject2.get("weather").toString();
							int r=weat.indexOf("description");
							int s=weat.lastIndexOf(",");
							int x=weat.indexOf("main");
							out.print("<h3><span style=\"color:blue;\"> Weather for " +cityname+ " is "+weat.substring(x+7,r-2)+" with "+weat.substring(r+14,s-2)+" </span></h3><br/>");
							//out.print("<br/><h3><span style=\"color:blue;\"> Weather for "+cityname+" is "+jsonObject2.get("weather")+" </span></h3><br/>");
							//out.print("<h3><span style=\"color:blue;\"> Other Details are "+jsonObject2.get("main")+" </span></h3><br/>");
							out.print("<h3><span style=\"color:blue;\"> Temprature = "+mainer.get("temp")+" </span></h3><br/>");
							out.print("<h3><span style=\"color:blue;\"> Humidity = "+mainer.get("humidity")+" </span></h3><br/>");
							out.print("<h3><span style=\"color:blue;\"> Pressure = "+mainer.get("pressure")+" </span></h3><br/>");
							String win=jsonObject2.get("wind").toString();
							out.print("<h3><span style=\"color:blue;\"> Wind = "+win.substring(1,win.length()-1)+" </span></h3><br/>");
							System.out.println(jsonObject2.get("weather"));
						}
					}
					catch(Exception e)
					{
						System.out.println(e);
						e.printStackTrace(); 
					}
					
					if(list.size()==0)
					{
						out.print("<h3>Please Enter Correct Name</h3>");
						rd=request.getRequestDispatcher("index.html");
						rd.include(request, response);
						out.print("Invalid Name of the city");
					}
				}
			}
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			out.print("<h3>Please Enter Correct Name</h3>");
			rd=request.getRequestDispatcher("index.html");
			rd.include(request, response);
			out.print("Invalid Name of the city");
		}
		
		//out.print("<div id=\"back\"><a href=\"index.html\">Back to main Page</a></div>");
		
	}

}

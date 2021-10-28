package rooppin.video.rental;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;
import org.json.simple.*;
import org.json.simple.parser.*;

public class WebService
{

	static HashMap<String,String> getFilmData(String tconst) throws MalformedURLException
	{
		String urlString = "http://www.omdbapi.com/?i=" + tconst + "&apikey=82479121";
		 URL url;

		url = new URL(urlString);

		 HashMap<String,String> result =null;
		 try {
		 HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		 conn.setRequestMethod("GET");
		 conn.connect();
		 
		 int responsecode = conn.getResponseCode();
		 
		 if(responsecode != 200) throw new RuntimeException("HttpResponseCode: " +responsecode);
		 else
		 {
			 String inline="";
			 Scanner sc = new Scanner(url.openStream());
			 while(sc.hasNext())
			 {
				 inline += sc.nextLine();
			 }

			 sc.close();
			 
			 result = jsonParse(inline,"");
		 	}
		} catch (IOException |RuntimeException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return result;
	}

	public static BufferedImage getPoster(String uri) {
		URL url;
		BufferedImage image = null;
		try
		{
			if(uri.length()>10)
				{
					url= new URL(uri);
					image = ImageIO.read(url);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	@SuppressWarnings("rawtypes")
	private static HashMap<String,String> jsonParse(String data, String post) throws Exception
	{
		if(data.startsWith("False")) throw new Exception("No web data");
		 HashMap<String,String> set = new HashMap<>();
		 JSONParser parse = new JSONParser();
		 JSONObject jobj;
		try
		{
			jobj = (JSONObject)parse.parse(data);
			
			if (jobj.size() > 0) {
		          for (Iterator iterator = jobj.keySet().iterator(); iterator.hasNext(); )
		          {
		            String key = (String) iterator.next();
		            
		            if(jobj.get(key) instanceof JSONArray) continue;

		            String val = (String) jobj.get(key);
		            set.put(key + post,  val);
		            //System.out.println(val);
		          }
		     }
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		 return set;
	}
}
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;



public class Test 
{
	
	public static void main(String[] args) 
	{

		final Socket socket;
		try 
		{
			socket = IO.socket("https://floating-mesa-5555.herokuapp.com/");
		
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() 
			{
				
			  public void call(Object... args) 
			  {
				  JSONObject obj = new JSONObject();
				  try 
				  {
					obj.put("name", "Bobek");
					obj.put("title", "NNN");
				  } 
				  catch (JSONException e) 
				  {
					e.printStackTrace();
				  }
				  
				  socket.emit("foo", obj);
				  
				  //socket.disconnect();
			  }
	
			}).on("event", new Emitter.Listener() 
			{
			  public void call(Object... args) 
			  {
				 System.out.println("Cos dostalem : "+args[0]);
				 socket.disconnect();
			  }
			  
	
			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() 
			{
	
			  public void call(Object... args) 
			  {
				  System.out.println("Disconnect");
				  System.exit(0); //Konczy prace aplikacji
			  }
	
			});
			socket.connect();
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
	}
}

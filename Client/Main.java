import java.io.IOException;
import java.net.UnknownHostException;

public class Main {

   public static void main(String[] args) throws UnknownHostException, IOException {
      
      Check_GUI cgui = new Check_GUI();
      cgui.initNet();
      cgui.readMsg();
   }

}
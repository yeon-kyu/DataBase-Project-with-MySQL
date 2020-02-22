import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class WindowHandler extends WindowAdapter {

	Check_GUI check_GUI;

	public WindowHandler(Check_GUI check_GUI) {
		this.check_GUI = check_GUI;
	}

	public void windowClosing(WindowEvent e) {

		try {
			check_GUI.sendMsg("logout");
			System.out.println("close Client");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.exit(0);
	}
}
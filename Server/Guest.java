import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Guest extends Thread {

	private ControlDB cdb;
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;

	Guest(ControlDB cdb, Socket socket) throws IOException {
		this.cdb = cdb;
		this.socket = socket;

		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		br = new BufferedReader(isr);

		OutputStream os = socket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		bw = new BufferedWriter(osw);
	}

	public void run() {
		try {
			while(socket != null) {
				String line = br.readLine();
				System.out.println(line + "ÀÐÀ½");
				String array[] = line.split(":");
				//tag:<data>
				switch(array[0]) {
				case "query1":
					System.out.println("catch1");
					cdb.Query1(this, array[1]);
					break;
				case "query2":
					cdb.Query2(this, array[1]);
					break;
				case "logout":
					br.close();
					socket.close();
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) throws IOException {
		bw.write(msg + "\n");
		bw.flush();
	}
}

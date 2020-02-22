import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

public class ControlDB extends JFrame implements ActionListener {

	private Connection conn;
	private java.sql.Statement stmt;
	private java.sql.ResultSet rs;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private int tnum;
	private int lnum;
	private int hnum;
	private int rnum;

	Button b1, b2;
	Panel p;

	public ControlDB() {
		super("서버 구동 프로그램");
		p = new Panel();
		b1 = new Button("서버구동");
		b2 = new Button("데이터 추가");
		b1.addActionListener(this);
		b2.addActionListener(this);
		p.add(b1);
		p.add(b2);
		add(p);
		setSize(200, 100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("서버구동")) {
			System.out.println("서버구동");
			setVisible(false);
			dispose();
			try {
				initNet();
			} catch (Exception ee) {
				ee.printStackTrace();
			} finally {
				disconnectDB();
			}
		} else if (e.getActionCommand().equals("데이터 추가")) {
			System.out.println("데이터 추가");
			setVisible(false);
			dispose();
			connectDB();
			try {
				updateData();
			} catch (IOException | SQLException e1) {
				e1.printStackTrace();
			}
			disconnectDB();
		}
	}

	private void initNet() throws IOException {
		ServerSocket ss = new ServerSocket(5555);
		connectDB();
		while (true) {
			System.out.println("대기중");
			Socket s = ss.accept();
			System.out.println("연결성공!");
			Guest g = new Guest(this, s);
			g.start();
		}
	}

	public Connection connectDB() {
		conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dbproject?character" + "Encoding=UTF-8&serverTimezone=UTC", "root",
					"root");
			System.out.println("데이터베이스 연결 성공");
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버가 존재하지 않습니다" + e);
		} catch (SQLException e) {
			System.out.println("오류:" + e);
		}
		return conn;
	}

	public void setNum() throws SQLException {
		rs = stmt.executeQuery("select count(*) from travelplace");
		while (rs.next())
			tnum = rs.getInt(1);
		rs = stmt.executeQuery("select count(*) from landmark");
		while (rs.next())
			lnum = rs.getInt(1);
		rs = stmt.executeQuery("select count(*) from hotel");
		while (rs.next())
			hnum = rs.getInt(1);
		rs = stmt.executeQuery("select count(*) from restaurant");
		while (rs.next())
			rnum = rs.getInt(1);
		System.out.println(tnum);
		System.out.println(lnum);
		System.out.println(hnum);
		System.out.println(rnum);
	}

	public void disconnectDB() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	insert into

//	Hotel(hno, hname, url, tno)
//	values(30001, '석모도 자연휴양림', 'test', 1001);
	private String existLname(String lname) throws SQLException {
		StringBuffer query = new StringBuffer();
		query.append("select * from landmark where lname = '" + lname + "';");
		rs = stmt.executeQuery(query.toString());

		if (!rs.next())
			return "false";
		else
			return rs.getString(1);
	}

	public void updateData() throws IOException, SQLException {
		String update = "C:\\Users\\woory\\Desktop\\DBproject\\update.txt";
		String data = "C:\\Users\\woory\\Desktop\\DBproject\\data.txt";
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(update), "UTF-8"));
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(data, true), "UTF-8"));
		PrintWriter pw = new PrintWriter(bufferedWriter, true);

		String line = null;
		String[] bucket;
		String[] bucket2;
		StringBuffer query = new StringBuffer();
		int size, size2;
		int tno, lno, hno, rno;

		setNum();
		tno = tnum;
		lno = lnum;
		hno = hnum;
		rno = rnum;
		while ((line = bufferedReader.readLine()) != null) {
			pw.write(line + "\n");
			pw.flush();
			tno = 10000 + (++tnum);
			bucket = line.split(";");
			query.setLength(0);
			System.out.println(bucket[0] + " " + bucket[1]);
			query.append("insert into travelplace values(" + Integer.toString(tno) + ",'" + bucket[0] + "','"
					+ bucket[1] + "');");
			stmt.executeUpdate(query.toString());

			line = bufferedReader.readLine();
			pw.write(line + "\n");
			pw.flush();
			bucket = line.split(";");
			size = bucket.length;
			for (int i = 0; i < size; i++) {
				line = bufferedReader.readLine();
				pw.write(line + "\n");
				pw.flush();
				bucket2 = line.split(";");
				size2 = bucket2.length / 2;
				for (int j = 0; j < size2; j++) {
					String lnumber;
					if ((lnumber = existLname(bucket2[j * 2])).equals("false")) {
						lno = 20000 + (++lnum);
						lnumber = Integer.toString(lno);
						query.setLength(0);
						query.append("insert into landmark values(" + lnumber + ",'" + bucket2[j * 2] + "','"
								+ bucket2[j * 2 + 1] + "'," + Integer.toString(tno) + ");");
						stmt.executeUpdate(query.toString());
					}
					query.setLength(0);
					query.append("insert into category values(" + lnumber + ",'" + bucket[i] + "');");
					stmt.executeUpdate(query.toString());
				}
			}
			line = bufferedReader.readLine();
			pw.write(line + "\n");
			pw.flush();
			if (line.equals("x") == false) {
				bucket = line.split(";");
				size = bucket.length / 2;
				for (int i = 0; i < size; i++) {
					hno = 30000 + (++hnum);
					query.setLength(0);
					query.append("insert into hotel values(" + Integer.toString(hno) + ",'" + bucket[i * 2] + "','"
							+ bucket[i * 2 + 1] + "'," + tno + ");");
					stmt.executeUpdate(query.toString());
				}
			}
			line = bufferedReader.readLine();
			pw.write(line + "\n");
			pw.flush();
			if (line.equals("x") == false) {
				bucket = line.split(";");
				size = bucket.length / 3;
				for (int i = 0; i < size; i++) {
					rno = 40000 + (++rnum);
					query.setLength(0);
					query.append("insert into restaurant values(" + Integer.toString(rno) + ",'" + bucket[i * 3] + "','"
							+ bucket[i * 3 + 1] + "','" + bucket[i * 3 + 2] + "'," + tno + ");");
					stmt.executeUpdate(query.toString());
				}

			}
		}
		bufferedReader.close();
		pw.close();
	}

	private HashMap<String, String> Collecter(int flag, HashMap<String, String> list, String region, String feature)
			throws SQLException {
		StringBuffer query = new StringBuffer();
		String s;
		HashMap<String, String> ret = new HashMap<String, String>();
		if (region.equals("전국"))
			query.append("select tno, tname from travelplace where ");
		else
			query.append("select tno, tname from travelplace where region = '" + region + "' and ");
		query.append("tno in(select l.tno from category c, landmark l where c.lno = l.lno and feature = '" + feature
				+ "');");
		rs = stmt.executeQuery(query.toString());

		while (rs.next()) {
			s = rs.getString(1);
			if (flag == 0)
				ret.put(s, rs.getString(2));
			else if (list.containsKey(s))
				ret.put(s, rs.getString(2));
		}
		return ret;
	}

	// 지역/feature,feature,feature...
	synchronized public void Query1(Guest g, String msg) throws SQLException, IOException {
		HashMap<String, String> list = new HashMap<String, String>();
		System.out.println("catch2");
		String[] bucket = msg.split("/");
		String[] bucket2 = bucket[1].split(",");
		int size = bucket2.length;
		for (int i = 0; i < size; i++)
			list = Collecter(i, list, bucket[0], bucket2[i]);
		StringBuffer result = new StringBuffer();
		String tno;
		Set<String> set = list.keySet();
		Iterator<String> it = set.iterator();
		int count = 0;
		while (it.hasNext()) {
			++count;
			tno = it.next();
			result.append(tno + "/");
			result.append(list.get(tno) + "/");
		}
		if (count != 0) {
			g.sendMsg("q1:::" + result.toString());
			System.out.println(result.toString());
		} else
			g.sendMsg("q1nodata");
	}

	// tno/feature,feature,feature...
	synchronized public void Query2(Guest g, String msg) throws SQLException, IOException {
		String[] bucket = msg.split("/");
		String[] bucket2 = bucket[1].split(",");
		StringBuffer query = new StringBuffer();
		StringBuffer result = new StringBuffer();
		int size = bucket2.length;
		for (int i = 0; i < size; i++) {
			query.setLength(0);
			query.append("select lname, url from landmark l, category c where l.lno = c.lno and l.tno = " + bucket[0]
					+ " and c.feature = '" + bucket2[i] + "';");
			rs = stmt.executeQuery(query.toString());
			while (rs.next())
				result.append(bucket2[i] + "," + rs.getString(1) + "," + rs.getString(2) + ";;;");
		}
		query.setLength(0);
		query.append("select hname, url from hotel where tno = " + bucket[0] + ";");
		rs = stmt.executeQuery(query.toString());
		while (rs.next())
			result.append("hotel," + rs.getString(1) + "," + rs.getString(2) + ";;;");
		query.setLength(0);
		query.append("select rname, url, menu from restaurant where tno = " + bucket[0] + ";");
		rs = stmt.executeQuery(query.toString());
		while (rs.next())
			result.append("restaurant," + rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + ";;;");
		g.sendMsg("q2:::" + result.toString());
		System.out.println(result);
	}

	public static void main(String[] args) throws SQLException, IOException {
		ControlDB cdb = new ControlDB();
	}
}
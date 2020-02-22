import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

public class Check_GUI extends Frame implements ActionListener {

	String checkList1; // 카테고리 선택한 목록

	Panel p;

	Label categ, zone;
	JCheckBox cate1, cate2, cate3, cate4, cate5, cate6, cate7, cate8;
	JCheckBox cate9, cate10, cate11, cate12, cate13;
	JRadioButton z1, z2, z3, z4, z5, z6, z7;
	ButtonGroup zoneGroup;

	Button search;

	Socket s;
	BufferedWriter bw;
	BufferedReader br;

	boolean[] featureArr;
	ArrayList<String> flist;
	HashMap<String, String> pair;
	Show_GUI show_gui;

	public Check_GUI() {
		super();

		featureArr = new boolean[13];
		flist = new ArrayList<String>();
		flist.add("바다");
		flist.add("온천");
		flist.add("산");
		flist.add("수목원");
		flist.add("공원");
		flist.add("호수");
		flist.add("사찰");
		flist.add("액티비티");
		flist.add("놀이공원");
		flist.add("도심");
		flist.add("계곡");
		flist.add("문화유산");
		flist.add("섬");
		pair = new HashMap<String, String>();

		addWindowListener(new WindowHandler(this));

		checkList1 = "";
		p = new Panel();
		categ = new Label("Category");
		zone = new Label("Zone");
		search = new Button("search");
		cate1 = new JCheckBox("바다", false);
		cate2 = new JCheckBox("온천");
		cate3 = new JCheckBox("산");
		cate4 = new JCheckBox("수목원");
		cate5 = new JCheckBox("공원");
		cate6 = new JCheckBox("호수");
		cate7 = new JCheckBox("사찰");
		cate8 = new JCheckBox("액티비티"); // 동물원,아쿠아리움,쇼핑 ..
		cate9 = new JCheckBox("놀이공원");
		cate10 = new JCheckBox("도심");
		cate11 = new JCheckBox("계곡");
		cate12 = new JCheckBox("문화유산");
		cate13 = new JCheckBox("섬");

		z1 = new JRadioButton("경기도");
		z2 = new JRadioButton("강원도");
		z3 = new JRadioButton("경상도");
		z4 = new JRadioButton("전라도");
		z5 = new JRadioButton("충청도");
		z6 = new JRadioButton("제주도");
		z7 = new JRadioButton("전국");
		zoneGroup = new ButtonGroup();
		zoneGroup.add(z1);
		zoneGroup.add(z2);
		zoneGroup.add(z3);
		zoneGroup.add(z4);
		zoneGroup.add(z5);
		zoneGroup.add(z6);
		zoneGroup.add(z7);

		search.addActionListener(this);

		// cate1.addActionListener(l);
		p.setLayout(null);

		setLocation();

		p.add(categ);
		p.add(zone);
		p.add(search);
		p.add(cate1);
		p.add(cate2);
		p.add(cate3);
		p.add(cate4);
		p.add(cate5);
		p.add(cate6);
		p.add(cate7);
		p.add(cate8);
		p.add(cate9);
		p.add(cate10);
		p.add(cate11);
		p.add(cate12);
		p.add(cate13);
		p.add(z1);
		p.add(z2);
		p.add(z3);
		p.add(z4);
		p.add(z5);
		p.add(z6);
		p.add(z7);
		add(p);
		setSize(800, 600);
		setVisible(true);

	}

	public String getFeature() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 13; i++) {
			if (featureArr[i] == true)
				buffer.append(flist.get(i) + ",");
		}
		return buffer.toString();
	}

	public String getTno(String tname) {
		return pair.get(tname);
	}

	void initNet() throws UnknownHostException, IOException {
		s = new Socket("127.0.0.1", 5555);
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();

		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		br = new BufferedReader(isr);

		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		bw = new BufferedWriter(osw);

	}

	private void setLocation() {
		categ.setBounds(20, 60, 60, 30);
		zone.setBounds(20, 180, 50, 30);
		search.setBounds(160, 440, 500, 50);

		cate1.setBounds(100, 60, 60, 30); // 바다
		cate2.setBounds(180, 60, 60, 30); // 온천
		cate3.setBounds(260, 60, 40, 30); // 산
		cate4.setBounds(320, 60, 70, 30);// 수목원
		cate5.setBounds(410, 60, 60, 30); // 공원
		cate6.setBounds(490, 60, 60, 30);// 호수
		cate7.setBounds(570, 60, 60, 30);// 사찰
		cate8.setBounds(100, 100, 80, 30); // 액티비티
		cate9.setBounds(200, 100, 80, 30); // 놀이공원
		cate10.setBounds(300, 100, 60, 30); // 도심
		cate11.setBounds(380, 100, 60, 30); // 계곡
		cate12.setBounds(460, 100, 80, 30); // 문화유산
		cate13.setBounds(560, 100, 50, 30); // 섬

		z1.setBounds(100, 180, 70, 30);
		z2.setBounds(180, 180, 70, 30);
		z3.setBounds(260, 180, 70, 30);
		z4.setBounds(340, 180, 70, 30);
		z5.setBounds(420, 180, 70, 30);
		z6.setBounds(500, 180, 70, 30);
		z7.setBounds(580, 180, 60, 30);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name;
		name = e.getActionCommand();
		boolean flag = true;
		System.out.print(flag + " ");
		if (name.equals("search")) {
			checkList1 = "";
			if (z1.isSelected())
				checkList1 = checkList1 + "경기도/";
			else if (z2.isSelected())
				checkList1 = checkList1 + "강원도/";
			else if (z3.isSelected())
				checkList1 = checkList1 + "경상도/";
			else if (z4.isSelected())
				checkList1 = checkList1 + "전라도/";
			else if (z5.isSelected())
				checkList1 = checkList1 + "충청도/";
			else if (z6.isSelected())
				checkList1 = checkList1 + "제주도/";
			else if (z7.isSelected())
				checkList1 = checkList1 + "전국/";
			else { // zone을 아무것도 선택하지 않았을 때 예외 처리
				System.out.println("지역을 선택해주세요");
				flag = false;
			}
			int flag2 = 0;
			System.out.print(flag + " ");
			if (cate1.isSelected()) {
				checkList1 = checkList1 + "바다,";
				featureArr[0] = true;
				++flag2;
			}
			if (cate2.isSelected()) {
				checkList1 = checkList1 + "온천,";
				featureArr[1] = true;
				++flag2;
			}
			if (cate3.isSelected()) {
				checkList1 = checkList1 + "산,";
				featureArr[2] = true;
				++flag2;
			}
			if (cate4.isSelected()) {
				checkList1 = checkList1 + "수목원,";
				featureArr[3] = true;
				++flag2;
			}
			if (cate5.isSelected()) {
				checkList1 = checkList1 + "공원,";
				featureArr[4] = true;
				++flag2;
			}
			if (cate6.isSelected()) {
				checkList1 = checkList1 + "호수,";
				featureArr[5] = true;
				++flag2;
			}
			if (cate7.isSelected()) {
				checkList1 = checkList1 + "사찰,";
				featureArr[6] = true;
				++flag2;
			}
			if (cate8.isSelected()) {
				checkList1 = checkList1 + "액티비티,";
				featureArr[7] = true;
				++flag2;
			}
			if (cate9.isSelected()) {
				checkList1 = checkList1 + "놀이공원,";
				featureArr[8] = true;
				++flag2;
			}
			if (cate10.isSelected()) {
				checkList1 = checkList1 + "도심,";
				featureArr[9] = true;
				++flag2;
			}
			if (cate11.isSelected()) {
				checkList1 = checkList1 + "계곡,";
				featureArr[10] = true;
				++flag2;
			}
			if (cate12.isSelected()) {
				checkList1 = checkList1 + "문화유산,";
				featureArr[11] = true;
				++flag2;
			}
			if (cate13.isSelected()) {
				checkList1 = checkList1 + "섬,";
				featureArr[12] = true;
				++flag2;
			}
			System.out.println(flag);
			try {
				if (flag && flag2 != 0) {
					System.out.println("send ok");
					sendMsg("query1:" + checkList1);
				} else if(flag == false && flag2 == 0)
					JOptionPane.showMessageDialog(null, "check region & Category");
				else if(flag == false)
					JOptionPane.showMessageDialog(null, "check region");
				else
					JOptionPane.showMessageDialog(null, "check Category");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("체크한 category : " + checkList1);

		}

	}

	public void sendMsg(String msg) throws IOException {
		bw.write(msg + "\n");
		bw.flush();
	}

	public void readMsg() {
		try {
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line + "!읽음");
				String[] bucket = line.split(":::");
				String[] array;
				int size;
				switch (bucket[0]) {
				case "q1":
					array = bucket[1].split("/");
					size = array.length / 2;
					setVisible(false);
					dispose();
					System.out.println(size);
					show_gui = new Show_GUI(this);
					for (int i = 0; i < size; i++) {
						System.out.println(array[i * 2 + 1] + " " + array[i * 2]);
						pair.put(array[i * 2 + 1], array[i * 2]);
						show_gui.addButton(array[i * 2 + 1]);
						System.out.println("i: " + i);
					}
					break;

				case "q2":
					System.out.println(bucket[1]);
					array = bucket[1].split(";;;");
					HashMap<String, String> classify2 = new HashMap<>();
					size = array.length;
					for (int i = 0; i < size; i++) {

						String[] words = array[i].split(",");
						int words_size = words.length;
						String val = "    ";
						String val2 = val;
						if (words_size == 4) {
							String temp;
							words[3] = "Menu : " + words[3];
							temp = words[3];
							words[3] = words[2];
							words[2] = temp;
						}
						for (int j = 1; j < words_size - 1; j++) {
							val = val + " " + words[j];
						}
						val2 = val + "&&" + words[words_size - 1]; // 정보와 URL는 &&로 구분
						val = val + "\n" + words[words_size - 1];
						if (classify2.containsKey(words[0])) { // 있는 key값이면 덧붙임
							classify2.put(words[0], classify2.get(words[0]) + "##@" + val2);
						} else {
							classify2.put(words[0], val2);
						}
					}
					show_gui.removeList();
					for (String key : classify2.keySet()) {
						System.out.println("key!");
						show_gui.addList_key("<" + key + ">");
						String[] for_one_data = classify2.get(key).split("##@");
						int data_size = for_one_data.length;
						for (int i = 0; i < data_size; i++) {
							System.out.println("data!");
		            		show_gui.addList_value(for_one_data[i]);
						}
						show_gui.addList_key("");
					}

					break;

				case "q1nodata":
					JOptionPane.showMessageDialog(null, "no data");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("readMsg에러" + e.getMessage());
			e.printStackTrace();
		}
	}
}
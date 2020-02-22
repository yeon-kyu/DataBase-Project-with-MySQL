import java.awt.Button;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Frame;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class Show_GUI extends Frame implements ActionListener {

	Panel p;

	Button[] b;
	Button exit_button;
//   TextArea MyData;
	int button_size;

	ArrayList<String> blist;
	Check_GUI check_gui;
	List list;
	HashMap<String, String> travel_url;

	public Show_GUI(Check_GUI check_gui) {
		super();
		this.check_gui = check_gui;
		travel_url = new HashMap<>();
		blist = new ArrayList<String>();

		p = new Panel();
		p.setLayout(null);

		button_size = 0;
		b = new Button[10];
		exit_button = new Button("나가기");
		exit_button.setBounds(610, 20, 60, 40);
//      MyData = new TextArea();
//      MyData.setBounds(300, 20, 400, 500);
//      MyData.setEditable(false);
//      MyData.setFont(new Font("궁서",Font.BOLD,20));

		list = new List(20, false); // 두번째 인자는 멀티플 선택 가능한지 여부
		list.setBounds(300, 20, 300, 500);
		p.add(exit_button);
//      p.add(MyData);
		p.add(list);

		list.addMouseListener(new MyMouseListener());
		exit_button.addActionListener(this);
		add(p);
		setSize(700, 600);
		setVisible(true);
	}

	public void addButton(String btn) {
		b[button_size] = new Button("#. " + btn);
		b[button_size].setBounds(20, 20 + button_size * 40, 120, 30);
		b[button_size].addActionListener(this);
		p.add(b[button_size]);

		button_size++;

		blist.add("#. " + btn);
	}

	public void removeList() {
		list.removeAll();
	}
	public void addList_key(String str) {
		list.add(str);
	}

	public void addList_value(String str) {
		// list.add(str);
		String[] for_one_list = str.split("&&");
		travel_url.put(for_one_list[0], for_one_list[1]);
		list.add(for_one_list[0]);
	}

//   public void addText(String str) {
//      MyData.append(str);
//   }

	class MyMouseListener implements MouseListener {

		String url;

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				url = list.getSelectedItem();
				String u = travel_url.get(url);
				try {
					Desktop.getDesktop().browse(new URI(u));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
				System.out.println(u);
			}
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name;
		String bname;
		name = e.getActionCommand();

		if (name.equals("나가기")) {
			System.out.println("나감?");
			dispose();
			setVisible(false);
			check_gui.setVisible(true);
		} else if (name.charAt(0) == '#') {
			bname = name.substring(3);
			try {
				check_gui.sendMsg("query2:" + check_gui.getTno(bname) + "/" + check_gui.getFeature());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
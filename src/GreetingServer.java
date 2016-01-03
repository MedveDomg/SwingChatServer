package application;

import java.net.*;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GreetingServer extends Thread {
	public static String text;
	private static ServerSocket serverSocket;
	private static InetAddress IP;
	private static Socket server;
	private static DefaultListModel listModel;
	private static JList list;

	public GreetingServer(int port) throws IOException,java.lang.IllegalArgumentException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000000);
	}

	public void run() {
		while (true) {
			try {
				setGUI();
				IP = InetAddress.getLocalHost();
				System.out.println(IP);
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				server = serverSocket.accept();
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				while(true) {
					DataInputStream in = new DataInputStream(server.getInputStream());
					listModel.addElement("Client: " + in.readUTF());
					System.out.println(in.readUTF());
					
				}
				// out.writeUTF("Thank you for connecting to "
				// + server.getLocalSocketAddress() + "\nGoodbye!");?]
				
			} catch (SocketTimeoutException s) {
				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		int port = 2010;
		
		try {
			Thread t = new GreetingServer(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendMessageToClient(String message) {

		try {

			OutputStream outToServer = server.getOutputStream();

			DataOutputStream out = new DataOutputStream(outToServer);

			out.writeUTF(message);

		} catch (Exception e) {

		}

	}

	private static void setGUI() {
		JFrame frame = new JFrame();
		frame.setTitle("Server");
		frame.setSize(315, 515);

		JPanel p = new JPanel();

		// » TextArea
		JLabel lIP = new JLabel("IP: ");
		p.add(lIP);
		JTextField tfIP = new JTextField();
		
		tfIP.setPreferredSize(new Dimension(90, 20));
		p.add(tfIP);

		JLabel lPort = new JLabel("Port: ");
		p.add(lPort);

		JTextField tfPort = new JTextField();
		tfPort.setPreferredSize(new Dimension(50, 20));
		p.add(tfPort);

		JButton connect = new JButton("Con");
		connect.setPreferredSize(new Dimension(70, 20));
		connect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

			}
		});
		p.add(connect);
		
		;

		
		JTextArea ta = new JTextArea("Сервак");

		ta.setPreferredSize(new Dimension(150, 30));
		JButton b = new JButton("Send");
		b.setPreferredSize(new Dimension(100, 30));

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String getText = ta.getText();
				sendMessageToClient(getText);
				listModel.addElement("Me: " + ta.getText());


			}
		});
		listModel = new DefaultListModel<>();
		list = new JList(listModel);

		list.setPreferredSize(new Dimension(300, 400));
		p.add(list);
		

		ta.setPreferredSize(new Dimension(150, 30));
		p.add(ta);
		p.add(b);

		// » Butoon
//		JButton b = new JButton("Send");
//		b.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//
//				sendMessageToClient(ta.getText());
//			}
//		});
//
//		p.add(b);

		// » Label
		JLabel l = new JLabel();

		p.add(l);

		frame.add(p);

		frame.setVisible(true);
	}

}
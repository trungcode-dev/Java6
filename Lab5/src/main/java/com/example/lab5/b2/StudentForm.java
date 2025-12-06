package com.example.lab5.b2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.EventQueue;
import java.net.HttpURLConnection;
import java.util.Map;

public class StudentForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtid;
	private JTextField txtfullname;
	private JTextField txtmark;
	private JTable table;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;

	DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Mark", "Gender"}, 0);

	final String API = "http://localhost:8080/api/students";

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				StudentForm frame = new StudentForm();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public StudentForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 606, 523);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtid = new JTextField();
		txtid.setBounds(10, 51, 272, 36);
		contentPane.add(txtid);

		JLabel lblID = new JLabel("id");
		lblID.setBounds(10, 11, 102, 29);
		contentPane.add(lblID);

		txtfullname = new JTextField();
		txtfullname.setBounds(307, 51, 272, 36);
		contentPane.add(txtfullname);

		JLabel lblFullname = new JLabel("Full name");
		lblFullname.setBounds(307, 11, 102, 29);
		contentPane.add(lblFullname);

		JLabel lblMark = new JLabel("average mark");
		lblMark.setBounds(307, 101, 102, 29);
		contentPane.add(lblMark);

		txtmark = new JTextField();
		txtmark.setBounds(307, 141, 272, 36);
		contentPane.add(txtmark);

		JLabel lblGender = new JLabel("gender");
		lblGender.setBounds(10, 101, 102, 29);
		contentPane.add(lblGender);

		rdbtnMale = new JRadioButton("male");
		rdbtnMale.setBounds(10, 154, 59, 23);
		contentPane.add(rdbtnMale);

		rdbtnFemale = new JRadioButton("female");
		rdbtnFemale.setBounds(80, 154, 80, 23);
		contentPane.add(rdbtnFemale);

		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(rdbtnMale);
		genderGroup.add(rdbtnFemale);

		JButton btncreate = new JButton("create");
		btncreate.setBounds(107, 195, 89, 23);
		contentPane.add(btncreate);

		JButton btnupdate = new JButton("update");
		btnupdate.setBounds(206, 195, 89, 23);
		contentPane.add(btnupdate);

		JButton btndelete = new JButton("delete");
		btndelete.setBounds(307, 195, 89, 23);
		contentPane.add(btndelete);

		JButton btnreset = new JButton("reset");
		btnreset.setBounds(406, 195, 89, 23);
		contentPane.add(btnreset);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 240, 572, 223);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setModel(model);
		scrollPane.setViewportView(table);

		loadTable();

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
					int row = table.getSelectedRow();
					txtid.setText(model.getValueAt(row, 0).toString());
					txtid.setEditable(false);
					txtfullname.setText(model.getValueAt(row, 1).toString());
					txtmark.setText(model.getValueAt(row, 2).toString());
					if (model.getValueAt(row, 3).toString().equals("Nam")) {
						rdbtnMale.setSelected(true);
					} else {
						rdbtnFemale.setSelected(true);
					}
				}
			}
		});

		btncreate.addActionListener(e -> createStudent());
		btnupdate.addActionListener(e -> updateStudent());
		btndelete.addActionListener(e -> deleteStudent());
		btnreset.addActionListener(e -> clearForm());
	}

	void loadTable() {
		try {
			model.setRowCount(0);

			HttpURLConnection conn = HttpClient.connect("GET", API);
			String response = HttpClient.readData(conn);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Student> map = mapper.readValue(response, new TypeReference<>() {});

			for (Student s : map.values()) {
				model.addRow(new Object[] {
						s.getId(),
						s.getName(),
						s.getMark(),
						s.isGender() ? "Nam" : "Nữ"
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createStudent() {
		try {
			Student s = getForm();

			ObjectMapper mapper = new ObjectMapper();
			byte[] data = mapper.writeValueAsBytes(s);

			HttpURLConnection conn = HttpClient.connect("POST", API);
			HttpClient.writeData(conn, data);

			loadTable();
			clearForm();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void updateStudent() {
		try {
			Student s = getForm();

			ObjectMapper mapper = new ObjectMapper();
			byte[] data = mapper.writeValueAsBytes(s);

			HttpURLConnection conn = HttpClient.connect("PUT", API + "/" + s.getId());
			HttpClient.writeData(conn, data);

			loadTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void deleteStudent() {
		try {
			String id = txtid.getText();
			HttpURLConnection conn = HttpClient.connect("DELETE", API + "/" + id);
			conn.getInputStream().close();

			loadTable();
			clearForm();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void clearForm() {
		txtid.setText("");
		txtid.setEditable(true);
		txtfullname.setText("");
		txtmark.setText("");
		rdbtnMale.setSelected(false);
		rdbtnFemale.setSelected(false);
	}

	Student getForm() {
		return new Student(
				txtid.getText(),
				txtfullname.getText(),
				Double.parseDouble(txtmark.getText()),
				rdbtnMale.isSelected()
		);
	}
}

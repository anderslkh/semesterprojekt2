package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLayeredPane;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.Event;
import controller.EquipmentController;
import controller.OrderController;
import database.DBConnection;
import database.DataAccessException;
import model.Equipment;
import model.Employee;
import model.Order;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.Color;
import javax.swing.SwingConstants;

public class OrderGui extends JFrame {

	private JPanel contentPane;
	private JTextField txtIndtastAntal;
	private JTextField txtIndtastSgeord;
	private JTextField txtEventID;
	private JTable findUdstyrTable;
	private JTable valgtUdstyrTable;
	private JTextField txtEventStartDate;
	private JTextField txtEventEndDate;
	private JTextField txtIndtastID;
	private String eventID;
	private OrderController orderController;
	private EquipmentController equipmentController;
	private Order order;
	private JTextField txtEmployeeID;
	private JTextField txtAfspejlerValgtEvent;
	private String eqName;
	private String eqID;
	private LocalDate startDate;
	private LocalDate endDate;
	private String employeeID;
	private Event event;
	private String txtEqID;
	private String txtEqName;
	private List<Equipment> equipments;
	private String orderID;
	private int quantity;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderGui frame = new OrderGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws DataAccessException
	 */
	public OrderGui() throws DataAccessException {
		orderController = new OrderController();
		equipmentController = new EquipmentController();
		quantity = 0;
		event = new Event(null, null);
		txtEqID = "Indtast ID på udstyr";
		txtEqName = "Indtast navn på udstyr";
		equipments = new ArrayList<>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(new Color(41, 140, 240));
		setContentPane(contentPane);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setForeground(new Color(0, 0, 128));
		layeredPane.setBackground(new Color(0, 0, 128));
		contentPane.add(layeredPane, BorderLayout.CENTER);

		JButton idSgBtn = new JButton("Søg");
		idSgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) findUdstyrTable.getModel();
				model.setRowCount(0);
				eqID = txtIndtastID.getText();
				try {
					equipments = orderController.findEquipment(null, eqID, orderController.getOrder().getStartDate(),
							orderController.getOrder().getEndDate());
					for (Equipment equipment : equipments) {
						int count = equipmentController
								.getCopiesFromTemp(equipment.getEqID(), orderController.getOrder().getStartDate(),
										orderController.getOrder().getEndDate())
								.size() - orderController.getEquipmentCountByID(equipment.getEqID());
						DefaultTableModel model1 = (DefaultTableModel) findUdstyrTable.getModel();
						model1.addRow(new Object[] { equipment.getEqName(), equipment.getEqID(), count });
					}
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				}
				txtIndtastSgeord.setText(txtEqName);
			}
		});
		idSgBtn.setBackground(Color.GRAY);
		idSgBtn.setFont(new Font("Arial", Font.BOLD, 20));
		idSgBtn.setBounds(537, 499, 90, 40);
		layeredPane.add(idSgBtn);

		JButton navnSgBtn = new JButton("Søg");
		navnSgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) findUdstyrTable.getModel();
				model.setRowCount(0);
				eqName = txtIndtastSgeord.getText();
				try {
					equipments = orderController.findEquipment(eqName, null, orderController.getOrder().getStartDate(),
							orderController.getOrder().getEndDate());
					for (Equipment equipment : equipments) {
						int count = equipmentController
								.getCopiesFromTemp(equipment.getEqID(), orderController.getOrder().getStartDate(),
										orderController.getOrder().getEndDate())
								.size() - orderController.getEquipmentCountByID(equipment.getEqID());
						DefaultTableModel model1 = (DefaultTableModel) findUdstyrTable.getModel();
						model1.addRow(new Object[] { equipment.getEqName(), equipment.getEqID(), count });
					}
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				}
				txtIndtastID.setText(txtEqID);
			}
		});

		navnSgBtn.setBackground(Color.GRAY);
		navnSgBtn.setFont(new Font("Arial", Font.BOLD, 20));
		navnSgBtn.setBounds(537, 448, 90, 40);
		layeredPane.add(navnSgBtn);

		JButton eventBtn = new JButton("Events");
		eventBtn.setBackground(new Color(23, 39, 52));
		eventBtn.setForeground(new Color(255, 255, 255));
		eventBtn.setFont(new Font("Arial", Font.BOLD, 18));
		eventBtn.setBounds(567, 27, 160, 67);
		layeredPane.add(eventBtn);

		JButton opretOrdreBtn = new JButton("Opret ordre");
		opretOrdreBtn.setBackground(new Color(23, 39, 52));
		opretOrdreBtn.setForeground(new Color(255, 255, 255));
		opretOrdreBtn.setFont(new Font("Arial", Font.BOLD, 18));
		opretOrdreBtn.setBounds(27, 27, 160, 67);
		layeredPane.add(opretOrdreBtn);

		JButton lagerBtn = new JButton("Lager");
		lagerBtn.setBackground(new Color(23, 39, 52));
		lagerBtn.setForeground(new Color(255, 255, 255));
		lagerBtn.setFont(new Font("Arial", Font.BOLD, 18));
		lagerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		lagerBtn.setBounds(387, 27, 160, 67);
		layeredPane.add(lagerBtn);

		JButton annullerBtn = new JButton("Annuller ordre");
		annullerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtAfspejlerValgtEvent.setText("");
				DefaultTableModel resetModel1 = (DefaultTableModel) valgtUdstyrTable.getModel();
				resetModel1.setRowCount(0);
				DefaultTableModel resetModel2 = (DefaultTableModel) findUdstyrTable.getModel();
				resetModel2.setRowCount(0);
				txtIndtastAntal.setText("");
				txtIndtastID.setText("");
				txtIndtastSgeord.setText("");
				txtEventEndDate.setText("");
			}
		});
		annullerBtn.setBackground(new Color(212, 91, 61));
		annullerBtn.setFont(new Font("Arial", Font.BOLD, 18));
		annullerBtn.setBounds(1446, 934, 200, 67);
		layeredPane.add(annullerBtn);

		JButton opretBtn = new JButton("Opret ordre");
		opretBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					orderController.saveOrder();
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				}
			}
		});
		opretBtn.setBackground(new Color(51, 176, 118));
		opretBtn.setFont(new Font("Arial", Font.BOLD, 18));
		opretBtn.setBounds(1670, 934, 200, 67);
		layeredPane.add(opretBtn);

		JButton tilfjBtn = new JButton("Tilføj");
		tilfjBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quantity = Integer.parseInt(txtIndtastAntal.getText());
				DefaultTableModel model = (DefaultTableModel) findUdstyrTable.getModel();
				try {
					int row = findUdstyrTable.getSelectedRow();
					int availableCount = (Integer) ((DefaultTableModel) findUdstyrTable.getModel()).getValueAt(row, 2);
					if (quantity <= availableCount && quantity >= 0) {
						int column = 1;
						String value = ((DefaultTableModel) findUdstyrTable.getModel()).getValueAt(row, column)
								.toString();
						orderController.addEquipmentToOrder(value, quantity);
						try {
							int modelcolum = 0;
							int modelrow = findUdstyrTable.getSelectedRow();
							String amountvalue = findUdstyrTable.getModel().getValueAt(modelrow, modelcolum).toString();
							DefaultTableModel model1 = (DefaultTableModel) valgtUdstyrTable.getModel();
							model1.addRow(new Object[] { (amountvalue), (quantity) });
							model.setRowCount(0);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, ex);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Tilgængelige antal kopier er mindre end ønsket antal",
								"RequestedAmountError", JOptionPane.ERROR_MESSAGE);
					}
				} catch (DataAccessException e1) {
					e1.printStackTrace();
				}
			}
		});
		tilfjBtn.setBackground(Color.GRAY);
		tilfjBtn.setFont(new Font("Arial", Font.BOLD, 20));
		tilfjBtn.setBounds(537, 961, 90, 40);
		layeredPane.add(tilfjBtn);

		txtIndtastAntal = new JTextField();
		txtIndtastAntal.setBackground(new Color(135, 206, 250));
		txtIndtastAntal.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtIndtastAntal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtIndtastAntal.setText("");
			}
		});
		txtIndtastAntal.setText("Indtast antal");
		txtIndtastAntal.setBounds(27, 961, 503, 40);
		layeredPane.add(txtIndtastAntal);
		txtIndtastAntal.setColumns(10);

		txtIndtastSgeord = new JTextField();
		txtIndtastSgeord.setBackground(new Color(135, 206, 250));
		txtIndtastSgeord.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtIndtastSgeord.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtIndtastSgeord.setText("");
			}
		});
		txtIndtastSgeord.setText(txtEqName);
		txtIndtastSgeord.setBounds(27, 449, 503, 40);
		layeredPane.add(txtIndtastSgeord);
		txtIndtastSgeord.setColumns(10);

		JButton aktiveOrdrerBtn = new JButton("Aktive ordrer");
		aktiveOrdrerBtn.setBackground(new Color(23, 39, 52));
		aktiveOrdrerBtn.setForeground(new Color(255, 255, 255));
		aktiveOrdrerBtn.setFont(new Font("Arial", Font.BOLD, 18));
		aktiveOrdrerBtn.setBounds(207, 27, 160, 67);

		layeredPane.add(aktiveOrdrerBtn);

		JLabel lblDeadlight = new JLabel("DEADLIGHT");
		lblDeadlight.setForeground(new Color(255, 255, 255));
		lblDeadlight.setFont(new Font("Arial", Font.BOLD, 66));
		lblDeadlight.setBounds(1485, 11, 385, 67);
		layeredPane.add(lblDeadlight);

		JLabel lblLydisken = new JLabel("LYDIKSEN");
		lblLydisken.setForeground(Color.DARK_GRAY);
		lblLydisken.setFont(new Font("Arial", Font.BOLD, 76));
		lblLydisken.setBounds(1485, 67, 385, 67);
		layeredPane.add(lblLydisken);

		txtEventID = new JTextField();
		txtEventID.setBackground(new Color(135, 206, 250));
		txtEventID.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtEventID.setText("Søg efter event ID");
		txtEventID.setBounds(27, 158, 503, 40);
		layeredPane.add(txtEventID);
		txtEventID.setColumns(10);
		txtEventID.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtEventID.setText("");
			}
		});

		JScrollPane scrollPaneFindUdstyr = new JScrollPane();
		scrollPaneFindUdstyr.setBounds(27, 550, 600, 400);
		layeredPane.add(scrollPaneFindUdstyr);

		findUdstyrTable = new JTable();
		scrollPaneFindUdstyr.setViewportView(findUdstyrTable);
		findUdstyrTable.setFillsViewportHeight(true);
		findUdstyrTable.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "Udstyr", "eqID", "Tilg\u00E6ngelige" }) {
					Class[] columnTypes = new Class[] { Object.class, Object.class, String.class };

					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});

		findUdstyrTable.setFont(new Font("Sylfaen", Font.PLAIN, 14));

		JComboBox<String> comboBoxSkabeloner = new JComboBox<String>();
		comboBoxSkabeloner.setBackground(new Color(135, 206, 250));
		comboBoxSkabeloner.setFont(new Font("Calibri", Font.PLAIN, 20));
		comboBoxSkabeloner.setBounds(27, 231, 503, 40);
		layeredPane.add(comboBoxSkabeloner);

		comboBoxSkabeloner.setEditable(true);
		comboBoxSkabeloner.addItem("Nibe 2019");
		comboBoxSkabeloner.addItem("Samsung livestream event 2020");
		comboBoxSkabeloner.addItem("2 vogne, 4 ekstra kamera");
		comboBoxSkabeloner.addItem("1 vogn, 2 ekstra kamera");

		comboBoxSkabeloner.setSelectedItem("Skabeloner");

		txtEventStartDate = new JTextField();
		txtEventStartDate.setHorizontalAlignment(SwingConstants.CENTER);
		txtEventStartDate.setBackground(new Color(135, 206, 250));
		txtEventStartDate.setText("YYYY-MM-DD");
		txtEventStartDate.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtEventStartDate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtEventStartDate.setText("");
			}
		});
		txtEventStartDate.setColumns(10);
		txtEventStartDate.setBounds(27, 372, 240, 40);
		layeredPane.add(txtEventStartDate);

		txtEventEndDate = new JTextField();
		txtEventEndDate.setHorizontalAlignment(SwingConstants.CENTER);
		txtEventEndDate.setBackground(new Color(135, 206, 250));
		txtEventEndDate.setText("YYYY-MM-DD");
		txtEventEndDate.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtEventEndDate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtEventEndDate.setText("");
			}
		});
		txtEventEndDate.setColumns(10);
		txtEventEndDate.setBounds(290, 372, 240, 40);
		layeredPane.add(txtEventEndDate);

		JScrollPane scrollPaneValgtUdstyr = new JScrollPane();
		scrollPaneValgtUdstyr.setBounds(1270, 387, 600, 500);
		layeredPane.add(scrollPaneValgtUdstyr);

		valgtUdstyrTable = new JTable();
		scrollPaneValgtUdstyr.setViewportView(valgtUdstyrTable);
		valgtUdstyrTable.setFillsViewportHeight(true);
		valgtUdstyrTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Udstyr", "Valgt antal" }));
		valgtUdstyrTable.setFont(new Font("Sylfaen", Font.PLAIN, 14));

		txtIndtastID = new JTextField();
		txtIndtastID.setBackground(new Color(135, 206, 250));
		txtIndtastID.setText(txtEqID);
		txtIndtastID.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtIndtastID.setColumns(10);
		txtIndtastID.setBounds(27, 499, 503, 40);
		layeredPane.add(txtIndtastID);

		JButton btnVælgSkabelon = new JButton("Vælg");
		btnVælgSkabelon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnVælgSkabelon.setBackground(Color.GRAY);
		btnVælgSkabelon.setFont(new Font("Arial", Font.BOLD, 20));
		btnVælgSkabelon.setBounds(537, 231, 90, 40);
		layeredPane.add(btnVælgSkabelon);

		JButton btnSøgEvent = new JButton("Søg");
		btnSøgEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				eventID = txtEventID.getText();
				try {
					Event event = null;
					if (!txtEventID.getText().isEmpty()) {
						event = orderController.findEventByID(eventID);
						txtEventID.setText(event.getName());
						txtAfspejlerValgtEvent.setText(event.getName());
						txtEventID.setEnabled(false);
					} else {
						btnSøgEvent.setEnabled(false);
					}
				} catch (DataAccessException e) {
					e.printStackTrace();
				}

			}
		});
		btnSøgEvent.setFont(new Font("Arial", Font.BOLD, 20));
		btnSøgEvent.setBackground(Color.GRAY);
		btnSøgEvent.setBounds(537, 159, 90, 40);
		layeredPane.add(btnSøgEvent);

		JLabel lblStartdato = new JLabel("Startdato");
		lblStartdato.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblStartdato.setBounds(27, 352, 79, 25);
		layeredPane.add(lblStartdato);

		JLabel lblSlutdato = new JLabel("Slutdato");
		lblSlutdato.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblSlutdato.setBounds(290, 352, 71, 25);
		layeredPane.add(lblSlutdato);

		JLabel lblSgEfterUdstyr = new JLabel("Søg efter udstyr");
		lblSgEfterUdstyr.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblSgEfterUdstyr.setBounds(27, 423, 132, 25);
		layeredPane.add(lblSgEfterUdstyr);

		JLabel lblVlgSkabelon = new JLabel("Vælg skabelon");
		lblVlgSkabelon.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblVlgSkabelon.setBounds(27, 209, 132, 25);
		layeredPane.add(lblVlgSkabelon);

		JLabel lblSgEfterEvent = new JLabel("Søg efter event");
		lblSgEfterEvent.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblSgEfterEvent.setBounds(27, 136, 132, 25);
		layeredPane.add(lblSgEfterEvent);

		JButton btnOpretOrdre = new JButton("Opret");
		btnOpretOrdre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startDate = LocalDate.parse(txtEventStartDate.getText());
				endDate = LocalDate.parse(txtEventEndDate.getText());
				employeeID = txtEmployeeID.getText();
				Employee employee = null;
				orderID = orderController.generateOrderID(orderID);
				event.setEventID(txtEventID.getText());
				try {
					employee = orderController.findEmployeeByID(employeeID);
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
				orderController.createOrder(orderID, startDate, endDate, employee, eventID);
			}
		});
		btnOpretOrdre.setFont(new Font("Arial", Font.BOLD, 20));
		btnOpretOrdre.setBackground(Color.GRAY);
		btnOpretOrdre.setBounds(537, 372, 90, 40);
		layeredPane.add(btnOpretOrdre);

		txtEmployeeID = new JTextField();
		txtEmployeeID.setText("Indtast medarbejder ID");
		txtEmployeeID.setHorizontalAlignment(SwingConstants.LEFT);
		txtEmployeeID.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtEmployeeID.setColumns(10);
		txtEmployeeID.setBackground(new Color(135, 206, 250));
		txtEmployeeID.setBounds(27, 301, 503, 40);
		layeredPane.add(txtEmployeeID);

		JLabel lblMedarbejder = new JLabel("Medarbejder");
		lblMedarbejder.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblMedarbejder.setBounds(27, 282, 109, 25);
		layeredPane.add(lblMedarbejder);

		JLabel lblNavnPEvent = new JLabel("Navn på event");
		lblNavnPEvent.setFont(new Font("Calibri", Font.PLAIN, 20));
		lblNavnPEvent.setBounds(1270, 314, 132, 25);
		layeredPane.add(lblNavnPEvent);

		txtAfspejlerValgtEvent = new JTextField();
		txtAfspejlerValgtEvent.setEditable(false);
		txtAfspejlerValgtEvent.setFont(new Font("Calibri", Font.PLAIN, 20));
		txtAfspejlerValgtEvent.setColumns(10);
		txtAfspejlerValgtEvent.setBackground(new Color(135, 206, 250));
		txtAfspejlerValgtEvent.setBounds(1270, 336, 600, 40);
		layeredPane.add(txtAfspejlerValgtEvent);

	}

	public void init() throws DataAccessException {
		new Thread(() -> {
			try {
				DBConnection.getInstance();
			} catch (DataAccessException e) {
				JOptionPane.showMessageDialog(this, "Connection to DB could not be established", "DBConnectionError",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		});

	}

}

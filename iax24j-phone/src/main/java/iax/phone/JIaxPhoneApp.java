package iax.phone;
import iax.audio.AudioFactory;
import iax.protocol.peer.Peer;
import iax.protocol.peer.PeerListener;
import iax.protocol.user.command.UserCommandFacade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JIaxPhoneApp extends javax.swing.JFrame implements PeerListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4717101600994251289L;
	public final static int WAITING = 1;
	public final static int CALLACTIVE = 2;
	public final static int RINGING = 4;
	public final static int CALLING = 8;

	private JMenuItem settingsMenuItem;
	private JButton jButton6;
	private JButton jButton5;
	private JButton jButton4;
	private JButton jButton3;
	private JButton jButton2;
	private JButton jButton13;
	private JButton jButton12;
	private JButton jButton11;
	private JButton jButton10;
	private JButton jButton9;
	private JButton jButton8;
	private JButton jButton7;
	private JButton jButton1;
	private JPanel jPanel3;
	private JPanel jPanel2;
	private JPanel jPanel1;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;
	
	private JIaxPhoneApp instance;
	private Peer peer;
	private int state = WAITING;
	private String callParticipant;
	private Properties prop = new Properties();
	private String dialString = "";
	private JLabel jLabel1;
	private JButton jButton14;
	private String funcString = "Call";
	private String mainFunc = "";
	private String subFunc = "";

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JIaxPhoneApp inst = new JIaxPhoneApp();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setResizable(false);
			}
		});
	}
	
	public JIaxPhoneApp() {
		super();
		InputStream is = ClassLoader.getSystemResourceAsStream("account.properties");
		if (is == null){
			final SettingDialog dialog = new SettingDialog("","","","");
			dialog.getOkBtn().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					prop.setProperty("user", dialog.getUserNameTxt().getText());
					prop.setProperty("password" , String.copyValueOf(dialog.getPasswordTxt().getPassword()));
					prop.setProperty("domain",dialog.getDomainTxt().getText());
					prop.setProperty("audio.factory.name",dialog.getSelectAudioFactory());
					dialog.setVisible(false);
					dialog.dispose();
					saveProperties(prop);
				}
			});
			dialog.setVisible(true);
		}else{
			try {
				prop.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		instance = this;
		String audioFactoryName = prop.getProperty("audio.factory.name");
		AudioFactory audioFactory = null;
		try {
			audioFactory = (AudioFactory) Class.forName(audioFactoryName).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		peer = new Peer(this, prop.getProperty("user"), prop.getProperty("password"), prop.getProperty("domain"), true, 1,audioFactory);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						shutdown();
						System.exit(0);
					}
				});
			}
			{
				jPanel1 = new JPanel();
				BorderLayout jPanel1Layout = new BorderLayout();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				{
					jPanel2 = new JPanel();
					GridBagLayout jPanel2Layout = new GridBagLayout();
					jPanel2Layout.columnWidths = new int[] {67,67,67};
					jPanel2Layout.rowHeights = new int[] {7, 7, 7, 7};
					jPanel2Layout.columnWeights = new double[] {0.0, 0.0, 0.1};
					jPanel2Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanel1.add(jPanel2, BorderLayout.CENTER);
					jPanel2.setLayout(jPanel2Layout);
					jPanel2.setPreferredSize(new java.awt.Dimension(207, 300));
					{
						jButton2 = new JButton();
						jPanel2.add(jButton2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton2.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton2.setText("1");
						jButton2.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "1";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('1');
								}
								playTone("1");
							}
						});
					}
					{
						jButton3 = new JButton();
						jPanel2.add(jButton3, new GridBagConstraints(1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton3.setText("2");
						jButton3.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton3.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton3.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "2";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('2');
								}
								playTone("2");
							}
						});
					}
					{
						jButton4 = new JButton();
						jPanel2.add(jButton4, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton4.setText("3");
						jButton4.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton4.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton4.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "3";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('3');
								}
								playTone("3");
							}
						});
					}
					{
						jButton5 = new JButton();
						jPanel2.add(jButton5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton5.setText("4");
						jButton5.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton5.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton5.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "4";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('4');
								}
								playTone("4");
							}
						});
					}
					{
						jButton6 = new JButton();
						jPanel2.add(jButton6, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton6.setText("5");
						jButton6.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton6.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton6.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "5";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('5');
								}
								playTone("5");
							}
						});
					}
					{
						jButton7 = new JButton();
						jPanel2.add(jButton7, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton7.setText("6");
						jButton7.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton7.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton7.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "6";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('6');
								}
								playTone("6");
							}
						});
					}
					{
						jButton8 = new JButton();
						jPanel2.add(jButton8, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton8.setText("7");
						jButton8.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton8.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton8.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "7";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('7');
								}
								playTone("7");
							}
						});
					}
					{
						jButton9 = new JButton();
						jPanel2.add(jButton9, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton9.setText("8");
						jButton9.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton9.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton9.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "8";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('8');
								}
								playTone("8");
							}
						});
					}
					{
						jButton10 = new JButton();
						jPanel2.add(jButton10, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton10.setText("9");
						jButton10.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton10.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton10.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "9";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('9');
								}
								playTone("9");
							}
						});
					}
					{
						jButton11 = new JButton();
						jPanel2.add(jButton11, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton11.setText("*");
						jButton11.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton11.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton11.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "*";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('*');
								}
								playTone("*");
							}
						});
					}
					{
						jButton12 = new JButton();
						jPanel2.add(jButton12, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton12.setText("0");
						jButton12.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton12.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton12.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "0";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('0');
								}
								playTone("0");
							}
						});
					}
					{
						jButton13 = new JButton();
						jPanel2.add(jButton13, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jButton13.setText("#");
						jButton13.setFont(new java.awt.Font("MS UI Gothic",1,20));
						jButton13.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton13.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (state == WAITING){
									dialString += "#";
									dispFuncButton();
								}else if (state == CALLACTIVE){
									sendDTMF('#');
								}
								playTone("#");
							}
						});
					}
				}
				{
					jPanel3 = new JPanel();
					BorderLayout jPanel3Layout = new BorderLayout();
					jPanel3.setLayout(jPanel3Layout);
					jPanel1.add(jPanel3,BorderLayout.NORTH);
					jPanel3.setPreferredSize(new java.awt.Dimension(174, 31));
					{
						jButton1 = new JButton();
						jPanel3.add(jButton1, BorderLayout.CENTER);
						jButton1.setText(dialString + funcString);
						jButton1.setPreferredSize(new java.awt.Dimension(115, 21));
						jButton1.setFont(new java.awt.Font("MS UI Gothic",0,12));
						jButton1.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								switch(state){
								case RINGING:
									answerCall();
									break;
								case CALLACTIVE:
									hangup();
									break;
								case WAITING:
									makeCall(dialString);
									break;
								case CALLING:
									hangup();
									break;
								}
							}
						});
					}
					{
						jButton14 = new JButton();
						jPanel3.add(jButton14, BorderLayout.EAST);
						jButton14.setPreferredSize(new java.awt.Dimension(59, 21));
						jButton14.setFont(new java.awt.Font("MS UI Gothic",0,10));
						jButton14.setText("Clear");
						jButton14.addMouseListener(new MouseAdapter() {
							public void mouseExited(MouseEvent evt) {
								buttonMouseExited(evt);
							}
							public void mouseEntered(MouseEvent evt) {
								buttonMouseEntered(evt);
							}
						});
						jButton14.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								switch(state){
								case RINGING:
									hangup();
									break;
								case CALLACTIVE:
									hangup();
									break;
								case WAITING:
									if (dialString.length() > 0){
										dialString = dialString.substring(0, dialString.length()-1);
										dispFuncButton();
									}
									break;
								case CALLING:
									hangup();
									break;
								}
							}
						});
					}
				}
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, BorderLayout.SOUTH);
				jLabel1.setText("unregisterd");
			}
			this.setSize(209, 408);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Exit");
						exitMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								shutdown();
								System.exit(0);
							}
						});
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Edit");
					{
						settingsMenuItem = new JMenuItem();
						jMenu4.add(settingsMenuItem);
						settingsMenuItem.setText("Settings...");
						settingsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								final SettingDialog dialog = new SettingDialog(prop.getProperty("user"),prop.getProperty("password"),prop.getProperty("domain"),prop.getProperty("audio.factory.name"));
								dialog.getOkBtn().addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										final Properties prop = new Properties();
										prop.setProperty("user", dialog.getUserNameTxt().getText());
										prop.setProperty("password", String.copyValueOf(dialog.getPasswordTxt().getPassword()));
										prop.setProperty("domain", dialog.getDomainTxt().getText());
										prop.setProperty("audio.factory.name", dialog.getSelectAudioFactory());
										saveProperties(prop);
										dialog.setVisible(false);
										dialog.dispose();
										UserCommandFacade.exit(peer);
										AudioFactory audioFactory = null;
										try {
											audioFactory = (AudioFactory) Class.forName(prop.getProperty("audio.factory.name")).newInstance();
										} catch (InstantiationException e) {
											e.printStackTrace();
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
										peer = new Peer(instance, prop.getProperty("user"),prop.getProperty("password"),prop.getProperty("domain"), true, 1,audioFactory);
									}
								});
								dialog.setVisible(true);
							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void answered(String calledNumber) {
		System.out.println("IAXphone, answered: " + calledNumber);
		callParticipant = calledNumber;
		state = CALLACTIVE;
		dispFuncButton();
	}

	public void exited() {
	}

	public void hungup(String calledNumber) {
		System.out.println("IAXphone, hungup: " + calledNumber);
		callParticipant = null;
		dialString = "";
		state = WAITING;
		dispFuncButton();
	}

	public void playWaitTones(String calledNumber) {
		state = CALLING;
	}

	public void recvCall(String callingName, String callingNumber) {
		callParticipant = callingNumber;
		dialString = callingName;
		state = RINGING;
		dispFuncButton();
		playRing();
	}

	public void registered() {
		System.out.println("IAXphone, registered");
		dispStatus("registered");
	}

	public void unregistered() {
		System.out.println("IAXphone, unregistered");
		dispStatus("unregisterd");
	}

	public void waiting() {
	}

	public void makeCall (String calledNumber){
		System.out.println("IAXphone, Calling " + calledNumber);

		callParticipant = calledNumber;
		
		UserCommandFacade.newCall(peer, calledNumber);
		state = CALLING;
		dispFuncButton();
		playRing();
	}

	public void transferCall (String calledNumber){		
		System.out.println("IAXphone, Transfering call from " + callParticipant + " to " + calledNumber);

		UserCommandFacade.transferCall(peer, callParticipant, calledNumber);
	}

	public void sendDTMF(char tone){
		UserCommandFacade.sendDTMF(peer, callParticipant, tone);
	}
	
	public void answerCall (){
		System.out.println("IAXphone, Answered call from " + callParticipant);
		System.out.println("Type \"hangup\" to hangup call");
		System.out.println("Type \"transfer <address>\" to transfer call to address");

		UserCommandFacade.answerCall(peer, callParticipant);
		state = CALLACTIVE;
		dispFuncButton();
	}


	public void hangup(){		
		System.out.println("IAXphone, Hungup call from " + callParticipant);
		
		UserCommandFacade.hangupCall(peer, callParticipant);
		state = WAITING;
		dispFuncButton();
	}

	public void shutdown(){
		System.out.println("Shutting down...");

		UserCommandFacade.exit(peer);
	}

	public int getState(){
		return state;
	}
	
	private void dispFuncButton(){
		switch (state) {
		case WAITING:
			mainFunc = "Call";
			subFunc = "Clear";
			break;
		case CALLACTIVE:
			mainFunc = "Hangup";
			subFunc = "";
			break;
		case RINGING:
			mainFunc = "Answer";
			subFunc = "Ignore";
			break;
		case CALLING:
			mainFunc = "Hangup";
			subFunc = "";
		}
		jButton1.setText(dialString + " " + mainFunc);
		jButton14.setText(subFunc);
		jPanel1.repaint();
	}
	
	private void dispStatus(String status){
		if (jLabel1 != null){
			jLabel1.setText(status);
			jLabel1.repaint();
		}
	}
	
	private void playTone(final String tone){
		Thread toneThread = new Thread(){
			public void run() {
				try {
					DTMFGenerator generator = new DTMFGenerator();
					generator.init(8000.0F);
					AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 8, 1, 1, 8000.0F, false);
					DataLine.Info info = new DataLine.Info(SourceDataLine.class,pcmFormat);
					SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
					line.open(pcmFormat);
					line.start();
		            byte[] abData = new byte[160];
		            for(int i = 0;i < 10;++i) {
			        	int available = line.available();
			        	if (available < 1600){
			        		Thread.sleep(150);
			        	}else{
			        		for(int j = 0;j < 160;++j){
			        			abData[j] = (byte)((generator.getDTMF(tone) * 128.0F) * 0.1);
			        		}
			        		line.write(abData, 0, abData.length);
			        	}
		            }
		            line.drain();
		            line.close();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		toneThread.start();
	}
	
	private void playRing(){
		Thread ring = new Thread(){

			public void run() {
		            while(state == RINGING || state == CALLING){
						try {
							DTMFGenerator generator = new DTMFGenerator();
							generator.init(8000.0F);
							AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 8, 1, 1, 8000.0F, false);
							DataLine.Info info = new DataLine.Info(SourceDataLine.class,pcmFormat);
							SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
							line.open(pcmFormat);
							line.start();
				            byte[] abData = new byte[160];
				            for(int i = 0;i < 150 && (state == RINGING || state == CALLING);++i) {
					        	int available = line.available();
					        	if (available < 1600){
					        		Thread.sleep(150);
					        	}else{
					        		if (i < 50){
						        		for(int j = 0;j < 160;++j){
						        			if ((i % 2) == 0){
							        			abData[j] = (byte)((generator.getLow("Ring") * 128.0F) * 0.1);
						        			}else{
							        			abData[j] = (byte)((generator.getHigh("Ring") * 128.0F) * 0.1);
						        			}
						        		}
					        		}else{
						        		for(int j = 0;j < 160;++j){
						        			abData[j] = (byte)0;
						        		}
					        		}
					        		line.write(abData, 0, abData.length);
					        	}
				            }
				            line.drain();
				            line.close();
						} catch (LineUnavailableException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		            }
			}
		};
		ring.start();
	}
	
	private void saveProperties(final Properties prop){
		OutputStream os = null;
		try {
			os = new FileOutputStream("account.properties");
			prop.store(os, "JIaxPhone Account properties");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try{os.close();}catch(Exception ignore){}
		}
	}
	
	private void buttonMouseEntered(MouseEvent evt) {
		evt.getComponent().setForeground(Color.green);
	}
	
	private void buttonMouseExited(MouseEvent evt) {
		evt.getComponent().setForeground(Color.black);
	}
}

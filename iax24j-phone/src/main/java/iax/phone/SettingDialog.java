package iax.phone;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


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
public class SettingDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 814871212639136294L;
	private JPanel jPanel1;
	private JButton cancelBtn;
	private JButton okBtn;
	private JPanel jPanel2;
	private JTextField domainTxt;
	private JLabel jLabel3;
	private JPasswordField passwordTxt;
	private JLabel jLabel2;
	private JTextField userNameTxt;
	private JLabel jLabel1;
	private String userName;
	private String password;
	private JLabel jLabel4;
	private JComboBox selectCodecBox;
	private String domain;
	private String audioFactoryName;
	private SettingDialog instance;
	private AudioItem[] audioItems; 

	public SettingDialog(String userName,String password,String domain,String audioFactoryName) {
		this.userName = userName;
		this.password = password;
		this.domain = domain;
		this.audioFactoryName = audioFactoryName;
		instance = this;
		InputStream is = ClassLoader.getSystemResourceAsStream("phone.conf");
		if (is != null){
			Properties prop = new Properties();
			try {
				prop.load(is);
				String supportCodec = (String) prop.get("support.codec");
				String[] codecs = supportCodec.split(",");
				String afn = (String) prop.get("audio.facrory.name");
				String[] factories = afn.split(",");
				audioItems = new AudioItem[codecs.length];
				for(int i = 0;i < codecs.length;++i){
					audioItems[i] = new AudioItem(codecs[i],factories[i]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try{is.close();}catch(IOException ignore){}
			}
		}
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.setTitle("Account Settings");
				this.setModal(true);
				this.setResizable(false);
			}
			{
				jPanel1 = new JPanel();
				GridBagLayout jPanel1Layout = new GridBagLayout();
				jPanel1Layout.columnWidths = new int[] {88, 7};
				jPanel1Layout.rowHeights = new int[] {7, 7, 7, 7};
				jPanel1Layout.columnWeights = new double[] {0.0, 0.1};
				jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setPreferredSize(new java.awt.Dimension(289, 262));
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel1.setText("User Name");
				}
				{
					userNameTxt = new JTextField();
					jPanel1.add(userNameTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					userNameTxt.setText(userName);
				}
				{
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel2.setText("Password");
				}
				{
					passwordTxt = new JPasswordField();
					jPanel1.add(passwordTxt, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					passwordTxt.setText(password);
				}
				{
					jLabel3 = new JLabel();
					jPanel1.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel3.setText("Domain");
				}
				{
					domainTxt = new JTextField();
					jPanel1.add(domainTxt, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					domainTxt.setText(domain);
				}
				{
					jLabel4 = new JLabel();
					jPanel1.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel4.setText("Codec");
				}
				{
					ComboBoxModel selectCodecBoxModel = 
						new DefaultComboBoxModel(audioItems);
					selectCodecBox = new JComboBox();
					jPanel1.add(selectCodecBox, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					selectCodecBox.setModel(selectCodecBoxModel);
					AudioItem selectedItem = null;
					for(int i = 0;i < audioItems.length;++i){
						if (audioFactoryName.equals(audioItems[i].getAudioFactoryName())){
							selectedItem = audioItems[i];
							break;
						}
					}
					selectCodecBox.setSelectedItem(selectedItem);
				}
			}
			{
				jPanel2 = new JPanel();
				getContentPane().add(jPanel2, BorderLayout.SOUTH);
				jPanel2.setPreferredSize(new java.awt.Dimension(281, 35));
				{
					okBtn = new JButton();
					jPanel2.add(okBtn);
					okBtn.setText("OK");
				}
				{
					cancelBtn = new JButton();
					jPanel2.add(cancelBtn);
					cancelBtn.setText("Cancel");
					cancelBtn.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							instance.setVisible(false);
							instance.dispose();
						}
					});
				}
			}
			this.setSize(289, 229);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public JButton getOkBtn() {
		return okBtn;
	}

	public void setOkBtn(JButton okBtn) {
		this.okBtn = okBtn;
	}

	public JTextField getDomainTxt() {
		return domainTxt;
	}

	public void setDomainTxt(JTextField domainTxt) {
		this.domainTxt = domainTxt;
	}

	public JPasswordField getPasswordTxt() {
		return passwordTxt;
	}

	public void setPasswordTxt(JPasswordField passwordTxt) {
		this.passwordTxt = passwordTxt;
	}

	public JTextField getUserNameTxt() {
		return userNameTxt;
	}

	public void setUserNameTxt(JTextField userNameTxt) {
		this.userNameTxt = userNameTxt;
	}

	public JComboBox getSelectCodecBox() {
		return selectCodecBox;
	}

	public String getSelectAudioFactory() {
		int selectIdx = selectCodecBox.getSelectedIndex();
		if (selectIdx >= 0){
			return audioItems[selectIdx].getAudioFactoryName();
		}
		return null;
	}

}

class AudioItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6808986923311519987L;
	private String codecName;
	private String audioFactoryName;
	
	@SuppressWarnings("unused")
	private AudioItem(){
		
	}

	public AudioItem(String codecName,String audioFactoryName){
		this.codecName = codecName;
		this.audioFactoryName = audioFactoryName;
	}
	
	public String getCodecName() {
		return codecName;
	}

	public void setCodecName(String codecName) {
		this.codecName = codecName;
	}

	public String getAudioFactoryName() {
		return audioFactoryName;
	}

	public void setAudioFactoryName(String audioFactoryName) {
		this.audioFactoryName = audioFactoryName;
	}

	@Override
	public String toString(){
		return codecName;
	}
}


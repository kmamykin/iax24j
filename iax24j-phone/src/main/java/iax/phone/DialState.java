package iax.phone;

import java.util.Date;

public interface DialState {
	public String getDial();
	public void setDial(String dial);
	public int getSequence();
	public void setSequence(int seq);
	public int getDialRetryCount();
	public void incDialRetryCount();
	public Date getLastDialTime();
	public void setLastDialTime(Date lastDialTime);
}

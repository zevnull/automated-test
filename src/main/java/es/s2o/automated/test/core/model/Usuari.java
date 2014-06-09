package es.s2o.automated.test.core.model;

/**
 * @author s2o
 */
public class Usuari {
	private boolean modoDemo;
	private String username;
	private String usernameDC;
	private String passwordIMS;
	private String passwordAGS;
	private String oficinaContable;
	private String ims;

	public boolean isModoDemo() {
		return modoDemo;
	}

	public void setModoDemo(boolean modoDemo) {
		this.modoDemo = modoDemo;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the usernameDC
	 */
	public String getUsernameDC() {
		return usernameDC;
	}

	/**
	 * @param usernameDC
	 *            the usernameDC to set
	 */
	public void setUsernameDC(String usernameDC) {
		this.usernameDC = usernameDC;
	}

	/**
	 * @return the passwordIMS
	 */
	public String getPasswordIMS() {
		return passwordIMS;
	}

	/**
	 * @param passwordIMS
	 *            the passwordIMS to set
	 */
	public void setPasswordIMS(String passwordIMS) {
		this.passwordIMS = passwordIMS;
	}

	/**
	 * @return the passwordAGS
	 */
	public String getPasswordAGS() {
		return passwordAGS;
	}

	/**
	 * @param passwordAGS
	 *            the passwordAGS to set
	 */
	public void setPasswordAGS(String passwordAGS) {
		this.passwordAGS = passwordAGS;
	}

	/**
	 * @return the oficinaContable
	 */
	public String getOficinaContable() {
		return oficinaContable;
	}

	/**
	 * @param oficinaContable
	 *            the oficinaContable to set
	 */
	public void setOficinaContable(String oficinaContable) {
		this.oficinaContable = oficinaContable;
	}

	/**
	 * @return the ims
	 */
	public String getIms() {
		return ims;
	}

	/**
	 * @param ims
	 *            the ims to set
	 */
	public void setIms(String ims) {
		this.ims = ims;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Usuari [modoDemo=").append(modoDemo).append(", username=").append(username)
				.append(", usernameDC=").append(usernameDC).append(", passwordIMS=").append(passwordIMS)
				.append(", passwordAGS=").append(passwordAGS).append(", oficinaContable=").append(oficinaContable)
				.append(", ims=").append(ims).append("]");
		return builder.toString();
	}

}

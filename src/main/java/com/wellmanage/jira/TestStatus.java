/**
 * 
 */
package com.wellmanage.jira;

/**
 * @author chaudam
 *
 */
public enum TestStatus {

	passed(1), failed(2), undefined(3), pending(3), skipped(3);

	private int status;

	private TestStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

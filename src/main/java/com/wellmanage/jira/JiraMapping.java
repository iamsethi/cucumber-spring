package com.wellmanage.jira;

import java.util.List;

public class JiraMapping {
	
	private String scenarioName;
	private String storyId;
	private String subTaskId;
	private List<String> testID;
	public String getScenarioName() {
		return scenarioName;
	}
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
	public String getStoryId() {
		return storyId;
	}
	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}
	public String getSubTaskId() {
		return subTaskId;
	}
	public void setSubTaskId(String subTaskId) {
		this.subTaskId = subTaskId;
	}
	public List<String> getTestID() {
		return testID;
	}
	public void setTestID(List<String> testID) {
		this.testID = testID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scenarioName == null) ? 0 : scenarioName.hashCode());
		result = prime * result + ((storyId == null) ? 0 : storyId.hashCode());
		result = prime * result + ((subTaskId == null) ? 0 : subTaskId.hashCode());
		result = prime * result + ((testID == null) ? 0 : testID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiraMapping other = (JiraMapping) obj;
		if (scenarioName == null) {
			if (other.scenarioName != null)
				return false;
		} else if (!scenarioName.equals(other.scenarioName))
			return false;
		if (storyId == null) {
			if (other.storyId != null)
				return false;
		} else if (!storyId.equals(other.storyId))
			return false;
		if (subTaskId == null) {
			if (other.subTaskId != null)
				return false;
		} else if (!subTaskId.equals(other.subTaskId))
			return false;
		if (testID == null) {
			if (other.testID != null)
				return false;
		} else if (!testID.equals(other.testID))
			return false;
		return true;
	}
	
	
	

}

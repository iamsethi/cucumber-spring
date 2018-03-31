package com.wellmanage.jira;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Subtask;
import com.atlassian.jira.rest.client.api.domain.input.LinkIssuesInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import cucumber.runtime.model.CucumberScenario;
import gherkin.formatter.model.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Service
public class JiraIntegration {

	private static final Logger LOGGER = Logger.getLogger(JiraIntegration.class.getName());

	@Value("${jira.jira_url}")
	private String jira_url;

	@Value("${jira.store_path}")
	private String store_path;

	@Value("${jira.cycle_path}")
	private String cycle_path;

	@Value("${jira.story_key}")
	private String story_key;

	@Value("${jira.subtask_key}")
	private String subtask_key;

	@Value("${jira.project_key}")
	private String project_key;

	@Value("${jira.versionId}")
	private String versionId;

	@Value("${jira.projectId}")
	private String projectId;

	@Value("${jira.jiraintegrationmode}")
	private String jiraintegrationmode;

	@Value("${jira.environment}")
	private String environment;

	@Value("${jira.nextRelease}")
	private String nextRelease;

	@Value("${jira.currentcyclename}")
	private String currentCycleName;

	@Value("${jira.cycledesc}")
	private String cycledesc;

	@Value("${jira.username}")
	private String username;

	@Value("${jira.password}")
	private String password;

	@Value("${jira.startcycledate}")
	private String startCycleDate;

	@Value("${jira.endcycledate}")
	private String endCycleDate;

	private String testName = null;
	private String cycleId = null;
	private String executionId = null;
	private String testId = null;
	private String testScenDesc = null;
	private String storyId = null;
	private String subTaskId = null;
	private String scenarioName = null;
	private Collection<String> tags = null;
	private String storeScenaioDetail = null;
	private int testStatus = 3;

	JiraMapping jiraMapping = new JiraMapping();
	public static String newline = System.getProperty("line.separator");

	public void jiraMap(Collection<String> tags, CucumberScenario cubScenario, String scnStatus)
			throws IOException, URISyntaxException {
		if (jiraintegrationmode.equalsIgnoreCase("ON")) {
			this.tags = tags;
			this.testName = cubScenario.getGherkinModel().getName().toString().replaceAll("\\s+", "_") + "_"
					+ cubScenario.getVisualName().replaceAll("\\W", "");
			this.testStatus = (TestStatus.valueOf(scnStatus)).getStatus();
			StringBuilder sceDetails = new StringBuilder();
			String sceOutline = (cubScenario.getGherkinModel().getKeyword().toString() + ":" + " "
					+ cubScenario.getGherkinModel().getName());
			List<Step> step = cubScenario.getSteps();
			sceDetails.append(sceOutline);
			for (Step stp : step) {
				String tp = (stp.getKeyword().toString() + stp.getName().toString());
				sceDetails.append(newline);
				sceDetails.append(tp);
			}
			this.testScenDesc = sceDetails.toString();
			scenarioExecutionDetails();
		}
	}

	public void scenarioExecutionDetails() throws IOException, URISyntaxException {
		if (tags.stream().filter(s -> !s.startsWith(story_key)).findFirst().isPresent()) {
			this.scenarioName = tags.stream().filter(s -> !s.startsWith(story_key)).findFirst().get();
		}
		if (tags.stream().filter(s -> s.startsWith(story_key)).findFirst().isPresent()) {
			withStoryKey();
		} else {
			withoutStoryKey();
		}
	}

	private void withStoryKey() throws IOException, URISyntaxException {
		this.storyId = tags.stream().filter(s -> s.startsWith(story_key)).findFirst().get().substring(1);
		this.storeScenaioDetail = getJiraStore(this.scenarioName);
		if (StringUtils.isNotBlank(this.storeScenaioDetail)) {
			storyKeyUpdateStore();
		} else {
			storyKeyInsertStore();
		}
	}

	private void storyKeyInsertStore() throws IOException, URISyntaxException {
		if (StreamSupport.stream(getJiraIssueType(storyId).getSubtasks().spliterator(), true)
				.filter(s -> s.getSummary().contains(subtask_key)).findFirst().isPresent()) {
			Subtask subtask = StreamSupport.stream(getJiraIssueType(storyId).getSubtasks().spliterator(), true)
					.filter(s -> s.getSummary().contains(subtask_key)).findFirst().get();
			this.subTaskId = subtask.getIssueKey();
			if (subtask != null) {
				flowCycleCreationWithStory();
				List<String> idtests = new ArrayList<String>();
				idtests.add(this.testId);
				jiraMapping.setTestID(idtests);
				jiraMapping.setStoryId(this.storyId);
				jiraMapping.setScenarioName(this.scenarioName);
				jiraMapping.setSubTaskId(this.subTaskId);
				addJiraStore(createRowStore(jiraMapping));
			}
		} else {
			throw new IllegalStateException("SubTask does not exist for Given Story ID" + this.storyId);
		}
	}

	private void storyKeyUpdateStore() throws IOException, URISyntaxException {
		jiraMapping = jiraMapping(getJiraStore(this.scenarioName));
		if (StreamSupport.stream(getJiraIssueType(storyId).getSubtasks().spliterator(), true)
				.filter(s -> s.getSummary().contains(subtask_key)).findFirst().isPresent()) {
			Subtask subtask = StreamSupport.stream(getJiraIssueType(storyId).getSubtasks().spliterator(), true)
					.filter(s -> s.getSummary().contains(subtask_key)).findFirst().get();
			this.subTaskId = subtask.getIssueKey();
			if (subtask != null) {
				flowCycleUpdateWithStory();
				jiraMapping.setStoryId(this.storyId);
				jiraMapping.setScenarioName(this.scenarioName);
				jiraMapping.setSubTaskId(this.subTaskId);
				updateJiraStore(this.storeScenaioDetail, createRowStore(jiraMapping));
			}
		} else {
			throw new IllegalStateException("SubTask Does Not Exist for Given Story ID" + storyId);
		}
	}

	private void flowCycleCreationWithStory() throws IOException, URISyntaxException {
		createJiraTest(this.testName);
		linkStory(this.subTaskId, this.testId);
		creatCycle(this.environment, this.currentCycleName, this.cycledesc);
		addTestsToCycle(testId, this.cycleId);
		createExecutionId(getJiraIssueType(this.testId).getId().toString(), this.cycleId);
		updateExecution(executionId, this.testStatus);
	}

	private void flowCycleUpdateWithStory() throws IOException, URISyntaxException {
		if (updateTestId() == null) {
			createJiraTest(this.testName);
			jiraMapping.getTestID().add(this.testId);
		} else {
			this.testId = updateTestId();
		}
		linkStory(this.subTaskId, this.testId);
		creatCycle(this.environment, this.currentCycleName, this.cycledesc);
		addTestsToCycle(testId, this.cycleId);
		createExecutionId(getJiraIssueType(this.testId).getId().toString(), this.cycleId);
		updateExecution(executionId, this.testStatus);
	}

	private void withoutStoryKey() throws IOException, URISyntaxException {
		if (StringUtils.isBlank(getJiraStore(this.scenarioName))) {
			flowCycleCreationWithoutStory();
			List<String> idtests = new ArrayList<String>();
			idtests.add(this.testId);
			jiraMapping.setTestID(idtests);
			jiraMapping.setStoryId(null);
			jiraMapping.setSubTaskId(null);
			jiraMapping.setScenarioName(this.scenarioName);
			addJiraStore(createRowStore(jiraMapping));
		} else {
			this.storeScenaioDetail = getJiraStore(this.scenarioName);
			jiraMapping = jiraMapping(this.storeScenaioDetail);
			jiraMapping.setScenarioName(this.scenarioName);
			flowCycleUpdateWithoutStory();
			updateJiraStore(this.storeScenaioDetail, createRowStore(jiraMapping));
		}
	}

	private void flowCycleCreationWithoutStory() throws URISyntaxException, IOException {
		createJiraTest(this.testName);
		creatCycle(this.environment, this.currentCycleName, this.cycledesc);
		addTestsToCycle(testId, this.cycleId);
		createExecutionId(getJiraIssueType(this.testId).getId().toString(), this.cycleId);
		updateExecution(executionId, this.testStatus);
	}

	private void flowCycleUpdateWithoutStory() throws URISyntaxException, IOException {
		if (updateTestId() == null) {
			createJiraTest(this.testName);
			jiraMapping.getTestID().add(this.testId);
		} else {
			this.testId = updateTestId();
		}
		creatCycle(this.environment, this.currentCycleName, this.cycledesc);
		addTestsToCycle(testId, this.cycleId);
		createExecutionId(getJiraIssueType(this.testId).getId().toString(), this.cycleId);
		updateExecution(executionId, this.testStatus);
	}

	private Issue getJiraIssueType(String Id) throws URISyntaxException, IOException {
		final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		final URI uri = new URI(jira_url);
		final Issue issue;
		final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(uri, username,
				new String(Base64.getDecoder().decode(password)));
		try {
			final Promise<Issue> promiseIssue = restClient.getIssueClient().getIssue(Id);
			issue = promiseIssue.claim();
		} finally {
			restClient.close();
		}
		return issue;
	}

	private void linkStory(String issueId, String testId) throws IOException, URISyntaxException {
		final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		final URI uri = new URI(jira_url);
		final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(uri, username,
				new String(Base64.getDecoder().decode(password)));
		try {
			restClient.getIssueClient()
					.linkIssue(
							new LinkIssuesInput(issueId, testId, "Depends", Comment.valueOf(testId + "test Id added")))
					.claim();
		} finally {
			restClient.close();
		}
	}

	private String createRowStore(JiraMapping mp) {
		List<String> strFinal = new ArrayList<String>();
		for (String str : mp.getTestID()) {
			strFinal.add("@" + str);
		}
		String st = StringUtils.join(strFinal, ",");
		return (mp.getScenarioName() + ":" + "|" + (mp.getStoryId() != null ? ("@" + mp.getStoryId()) : " ") + "|"
				+ (mp.getSubTaskId() != null ? ("@" + mp.getSubTaskId()) : " ") + "|" + st);
	}

	private void updateJiraStore(String strOrginal, String strNew) throws IOException {
		List<String> replaced = Files.lines(Paths.get(store_path)).map(line -> line.replace(strOrginal, strNew))
				.collect(Collectors.toList());
		Files.write(Paths.get(store_path), replaced);
	}

	private void addJiraStore(String strNew) throws IOException {
		Path path = Paths.get(store_path);
		Charset charset = Charset.forName("UTF-8");
		ArrayList<String> lines = new ArrayList<>();
		lines.add(strNew);
		Files.write(path, lines, charset, StandardOpenOption.APPEND);
	}

	private String getJiraStore(String str) throws IOException {
		String scenarioDetail = null;
		Stream<String> lines = Files.lines(Paths.get(store_path));
		Optional<String> hasScenario = lines.parallel().filter(s -> s.startsWith(str + ":")).findFirst();
		if (hasScenario.isPresent()) {
			scenarioDetail = hasScenario.get();
		}
		lines.close();
		return scenarioDetail;
	}

	private JiraMapping jiraMapping(String str) {
		String[] string = str.split(Pattern.quote("|"));
		JiraMapping jm = new JiraMapping();
		jm.setScenarioName((string[0].trim().substring(1)).replaceAll("[:]+", ""));
		if (StringUtils.isNotBlank(string[1].trim())) {
			jm.setStoryId(string[1].trim().substring(1));
		}
		if (StringUtils.isNotBlank(string[2].trim())) {
			jm.setSubTaskId(string[2].trim().substring(1));
		}
		if (StringUtils.isNotBlank(string[3].trim())) {
			List<String> lsitStr = new ArrayList<String>();
			Stream.of(string[3].split(Pattern.quote(","))).forEach(id -> {
				lsitStr.add(id.trim().substring(1));
			});
			jm.setTestID(lsitStr);
		}
		return jm;
	}

	private String updateTestId() throws URISyntaxException, IOException {
		String testId = null;
		for (String str : jiraMapping.getTestID()) {
			Issue issue = getJiraIssueType(str);
			if (this.testName.equalsIgnoreCase(issue.getSummary())) {
				testId = issue.getKey();
			}
		}
		return testId;
	}

	private String getStoreCycleKey() throws IOException {
		String cycleKey = null;
		Stream<String> lines = Files.lines(Paths.get(cycle_path));
		Optional<String> hasScenario = lines.parallel().filter(s -> s.startsWith("cycleId" + "=")).findFirst();
		if (hasScenario.isPresent()) {
			cycleKey = StringUtils.substring(hasScenario.get(), 8, 15).trim();
		}
		lines.close();
		return cycleKey;
	}

	private void updateStoreCycleKey(String strNew) throws IOException {
		Stream<String> lines = Files.lines(Paths.get(cycle_path));
		List<String> replaced = Files.lines(Paths.get(cycle_path))
				.map(line -> line.replace(
						lines.parallel().filter(s -> s.startsWith("cycleId" + "=")).findFirst().get().toString(),
						strNew))
				.collect(Collectors.toList());
		Files.write(Paths.get(cycle_path), replaced);
		lines.close();
	}

	private void creatCycle(String env, String cycleName, String desc) throws IOException {
		this.cycleId = getStoreCycleKey();
		if (StringUtils.isNotBlank(this.cycleId)) {
			if (!currentCycleName.equalsIgnoreCase(getZephyrCycle())) {
				creatZephyrCycle(env, cycleName, desc);
			}
		} else if (StringUtils.isBlank(this.cycleId)) {
			creatZephyrCycle(env, cycleName, desc);
		}
	}

	private void creatZephyrCycle(String env, String cycleName, String desc) throws IOException {
		String url = this.jira_url + "rest/zapi/latest/cycle";
		String input = "{  \"clonedCycleId\": \"\",  \"name\": \"" + cycleName
				+ "\",  \"build\": \"\",  \"environment\": \"" + env + "\", " + " \"description\": \"" + desc
				+ "\",  \"startDate\":  \"" + startCycleDate + "\",  \"endDate\":  \"" + endCycleDate + "\",  "
				+ "\"projectId\": \"" + this.projectId + "\",\"versionId\": \"-1\"}";
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(username, new String(Base64.getDecoder().decode(password))));
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		// this.cycleId = JsonPath.read(response.getEntity(String.class), "id");
		// updateStoreCycleKey("cycleId=" + this.cycleId);
	}

	private String getZephyrCycle() {
		String url = this.jira_url + "rest/zapi/latest/cycle/" + this.cycleId;
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(username, new String(Base64.getDecoder().decode(password))));
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
		// return JsonPath.read(response.getEntity(String.class), "name");
		return "";
	}

	private void createJiraTest(String testCaseName) {
		String urlPost = this.jira_url + "rest/api/2/issue/";
		String description = "test Created";
		String issueType = "Bug";
		String urlPayload = "{\"fields\":{\"project\":{\"key\":\"" + project_key + "\"},\"summary\":\"" + testCaseName
				+ "\",\"description\":\"Creating of an issue using project keys and issue type names using the REST API\",\"issuetype\":{\"name\":\""
				+ issueType + "\"}}}";
		RestAssured.baseURI = this.jira_url;
		Response res = given().header("Content-Type", "application/json")
				.body("{ \"username\": \"tletuser\", \"password\": \"letuser\" }").when().post(urlPost).then()
				.statusCode(200).extract().response();
		String responseString = res.asString();
		JsonPath js = new JsonPath(responseString);
		String session = js.get("session.value");

		LOGGER.info("####" + session + "####");

		Response res2 = given().header("Content-Type", "application/json").header("Cookie", "JSESSIONID=" + session)
				.body(urlPayload).when().post("rest/api/2/issue/").then().extract().response();

		LOGGER.info("####" + res2.asString() + "####");
		String responseString2 = res2.asString();
		JsonPath js2 = new JsonPath(responseString2);
		this.testId = js2.get("key");

	}

	private void addTestsToCycle(String testCaseKey, String cycleId) {
		String urlPayload = "{ \"issues\": [\"" + testCaseKey + "\"], \"versionId\": \"" + this.versionId
				+ "\", \"cycleId\": \"" + cycleId + "\", \"projectId\": \"" + this.projectId
				+ "\", \"method\": \"1\" }";
		String urlString = this.jira_url + "rest/zapi/latest/execution/addTestsToCycle/";
		LOGGER.info("URL:->" + urlString + "\nPayload:->" + urlPayload);
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(username, new String(Base64.getDecoder().decode(password))));
		client.resource(urlString).type("application/json").post(ClientResponse.class, urlPayload);
	}

	private void createExecutionId(String testCaseId, String cycleId) {
		String urlPayload = "{ \"versionId\": \"" + this.versionId + "\", \"cycleId\": \"" + cycleId
				+ "\", \"projectId\": \"" + this.projectId + "\", \"issueId\": \"" + testCaseId + "\" }";
		String urlString = this.jira_url + "rest/zapi/latest/execution";
		LOGGER.info("URL:->" + urlString + "\nPayload:->" + urlPayload);
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(this.username, new String(Base64.getDecoder().decode(password))));
		WebResource webResource = client.resource(urlString);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, urlPayload);
		// List<Integer> issueIds = JsonPath.read(response.getEntity(String.class),
		// "$.[*].id");
		// this.executionId = issueIds.get(0).toString();
	}

	private void updateExecution(String executionId, int updateStatus) {
		String urlString = this.jira_url + "rest/zapi/latest/execution/" + executionId + "/execute";
		String urlPayload = ("{  \"status\":\"" + 3 + "\"}");
		String urlPayloadNew = ("{  \"status\":\"" + updateStatus + "\"}");
		LOGGER.info("URL:->" + urlString + "\nPayload:->" + urlPayload);
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter(this.username, new String(Base64.getDecoder().decode(password))));
		client.resource(urlString).type("application/json").put(ClientResponse.class, urlPayload);
		client.resource(urlString).type("application/json").put(ClientResponse.class, urlPayloadNew);
	}

}

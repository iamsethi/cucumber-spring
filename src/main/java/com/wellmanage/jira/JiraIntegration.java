package com.wellmanage.jira;

import com.google.common.base.Function;

/**
 * This is the Object repository for all the Locators.
 * 
 * @author ksethi
 * 
 */
public class JiraIntegration {

	public static final String xpthMenuFrame = "//*[@id='csw_menuspace']/frame[1]";
	public static final String xpthPortalLink = "//a[text()='IA Portal']";
	public static final String xpthTCSFrame = "//*[@id=\"csw_menuspace\"]/frame[2]";
	public static final String xpthAddNew = "//i[@class='fa fa-plus']";
	public static final String xpthIncludeSign = "//label[starts-with(text(), ' Include')]";
	public static final String xpthSaveBtn = "//button[contains(text(),'Save')]";
	public static final String xpthContactName = "//input[contains(@formcontrolname,'contactName')]";
	public static final String xpthId = "//input[@placeholder='Search...']";
	public static final String xpthAccSrch = "//input[@placeholder='Search...']";
	public static final String xpthCbx = "//input[contains(@type,'checkbox')]";
	public static final String xpthAdmin = "//select[contains(@formcontrolname,'admin')]";
	public static final String xpthAddNewSuccess = "//p[@class='text-success']";
	public static final String xpthSrchRsltErr = "//p[@class='text-danger']";
	public static final String xpthUnchkAll = "//a[contains(text(),' Uncheck all')]";
	public static final String xpthFrmDt = "//label[text()='Created From Date']//ancestor::div[1]//span";
	public static final String xpthDtTdy = "//span[text()='Today']";
	public static final String xpthToDt = "//label[text()='Created To Date']//ancestor::div[1]//span";
	public static final String xpthSrtTL = "//div[@class='ag-cell-label-container ag-header-cell-sorted-asc']";
	public static final String xpthSrtStatus = "//span[text()='Status']";
	public static final String xpthSrch = "//button[text()='Search']";
	public static final String xpthLblGridResult = "//span[contains(text(),'record')]";
	public static final String xpthLblTLId = "//span[@class='ag-group-value']";
	public static final String xpthRcntTL = "//div[@class='ag-body-container']//div[2]//span[@ref='eValue']";
	public static final String xpthExpndTL = "//div[@class='ag-body-container']/div[position()=last()]//div[2]//span[@ref='eContracted']";
	public static final String xpthUpdtBtn = "//button[contains(text(),'Update')]";
	public static final String xpthClr = "//button[contains(text(),'Clear')]";
	public static final String xpthEdtIcn = "//div[@class='ag-body-container']/div[position()=last()]//div[7]//a";
	public static final String xpthDltIcn = "//div[@class='ag-body-container']/div[position()=last()]//div[7]//a[2]";
	public static final String xpthCopyIcn = "//div[@class='ag-body-container']/div[position()=last()]//div[7]//a[3]";
	public static final String xpthDltBtn = "//h4[text()='Confirm Delete']/ancestor::div[1]/following-sibling::div[2]//button[contains(text(),'Delete')]";
	public static final String xpthTLcbx = "//div[@class='ag-body-container']/div[position()=last()]//div";
	public static final String xpthDlvrSlctd = "//button[contains(@data-target,'#deliverModal')]";
	public static final String xpthDlvrFrmDt = "(//div[@role='gridcell' and @col-id='createdFromDate'])[2]";
	public static final String xpthDlvrToDt = "//div[@role='gridcell' and @col-id='createdToDate']";
	public static final String xpthDlvrStatus = "//div[@role='gridcell' and @col-id='createdToDate']/ancestor::div[@class='modal-content']//button[@class='btn btn-danger']";
	public static final String xpthTLId = "//div[@class='ag-body-container']/div[position()=last()]//div[2]//span[@ref='eValue']";
	public static final String xpthDlvrDtTdy = "//td[@class=' ui-datepicker-days-cell-over  ui-datepicker-today']";
	public static final String xpthOnDmnd = "//a[text()='On Demand']";
	public static final String xpthDlvrScrn = "//h4[text()='Deliver']";
	public static final String xpthOnDmndFrmDt = "//button[@aria-label='Open Calendar']";
	public static final String xpthOnDmndToDt = "(//button[@aria-label='Open Calendar'])[2]";
	public static final String xpthOnDmndTLId = "//input[@placeholder='Enter Trade Letter ID..']";
	public static final String xpthCrtdFrmDt = "//label[text()='Created From Date']/following-sibling::div//input";
	public static final String xpthCrtToDt = "	//label[text()='Created To Date']/following-sibling::div//input";
	public static final String xpthDlvrBtn = "//button[contains(text(),'Deliver')]";
	public static final String xpthPreview = "//button[contains(text(),'Preview in ER')]";
	public static final String xpthERScrn = "//span[@class='nav-title']";
	public static final String xpthOvrrideSSL = "javascript:document.getElementById('overridelink').click()";

	public static Function<String, String> getXpthOptionType = (optionName) -> "//label[starts-with(text(), ' "
			+ optionName + "')]/following-sibling::div";

	public static Function<String, String> getXpthTxtInfo = (info) -> "//input[contains(@formcontrolname,'" + info
			+ "')]";

	public static Function<String, String> getXpthSrch = (input) -> "//input[contains(@placeholder,'Enter " + input
			+ "')]";

	public static Function<String, String> getXpthDrpDwn = (info) -> "//label[@for='" + info
			+ "']/following-sibling::div//button[@class='dropdown-toggle btn btn-default btn-block']";

	public static Function<String, String> getXpthTypeAhead = (
			suggestion) -> "//span[@class='completer-list-item match' and text()='" + suggestion + "']";

	public static Function<String, String> getGridCell = (
			colName) -> "//div[@class='ag-body-container']/div[position()=last()]//div[@role='gridcell' and contains(@col-id,'"
					+ colName + "')]";

	public static Function<String, String> getCrtdDt = (label) -> "//label[text()='" + label
			+ "']/following-sibling::div//input";

	public static Function<String, String> getOpenCalendar = (label) -> "//label[text()='" + label
			+ "']//ancestor::div[1]//span";

}

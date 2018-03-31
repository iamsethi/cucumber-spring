package com.wellmanage.database;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author sethiK
 *
 */
@ContextConfiguration(locations = { "classpath*:/spring-context/applicationContext-smoke-suite.xml" })
public class TletIdrDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(TletIdrDao.class);

	private JdbcTemplate jdbcTemplate;

	private DataSource dataSources;
	protected String PARTY_ID;

	/** The delete TL acct group. */
	@Value("${delete.tlAcctGroup.sql}")
	private String deleteTLAcctGroup;

	/** The delete TL schedule. */
	@Value("${delete.tlSchedule.sql}")
	private String deleteTLSchedule;

	/** The delete AE file recipient. */
	@Value("${delete.aeFileRecipient.sql}")
	private String deleteAEFileRecipient;

	/** The delete AE file. */
	@Value("${delete.aeFile.sql}")
	private String deleteAEFile;

	/** The delete TL delivery method. */
	@Value("${delete.tlDeliveryMethod.sql}")
	private String deleteTLDeliveryMethod;

	/** The delete TL delivery method for print. */
	@Value("${delete.tlDeliveryMethodForPrint.sql}")
	private String deleteTLDeliveryMethodForPrint;

	/** The delete TL exclude sec type. */
	@Value("${delete.tlExSecType.sql}")
	private String deleteTLExcludeSecType;

	/** The delete trade letter. */
	@Value("${delete.tradeLetter.sql}")
	private String deleteTradeLetter;

	/** The delete party address. */
	@Value("${delete.partyAddress.sql}")
	private String deletePartyAddress;

	/** The delete address. */
	@Value("${delete.address.sql}")
	private String deleteAddress;

	/** The delete party contact mech. */
	@Value("${delete.partyContactMech.sql}")
	private String deletePartyContactMech;

	/** The delete contact mech. */
	@Value("${delete.contactMech.sql}")
	private String deleteContactMech;

	/** The delete party. */
	@Value("${delete.party.sql}")
	private String deleteParty;

	/** The delete AE file recipient by file id. */
	@Value("${delete.aeFileRecipientByFileId.sql}")
	private String deleteAEFileRecipientByFileId;

	/** The select party id by TL id. */
	@Value("${select.partyIdByTLId.sql}")
	private String selectPartyIdByTLId;

	/** The select file id TL id. */
	@Value("${select.fileIdByTLId.sql}")
	private String selectFileIdTLId;

	/** The select address id by P id. */
	@Value("${select.addressIdByPId.sql}")
	private String selectAddressIdByPId;

	/** The select contact id by P id. */
	@Value("${select.contactIdByPId.sql}")
	private String selectContactIdByPId;

	/** The select admin id by TL id. */
	@Value("${select.adminIdByTLId.sql}")
	private String selectAdminIdByTLId;

	/** The select admin party id by A id. */
	@Value("${select.adminPartyIdByAId.sql}")
	private String selectAdminPartyIdByAId;

	public void setDataSource(DataSource dataSource) {
		this.dataSources = dataSource;
		jdbcTemplate = new JdbcTemplate(dataSources);

	}

<<<<<<< HEAD
	public void deleteTradeLetter(String tradeLetterId) {
=======
	public void deleteTradeLetter(List<String> tradeLetterIds) {
		
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue(Constants.APP_USER, impersonateUser);
		parameters.addValue(Constants.BRKR_INSTX_ID, brkrInstxId);
		jdbcTemplate.update(updtStgCAI, parameters);
		LOGGER.info("###Broker Cash Agent Information approved successfully from Live table for Broker Instx ID : "
				+ brkrInstxId);
	
>>>>>>> branch 'master' of https://github.com/iamsethi/cucumber-spring.git

		LOGGER.info("####" + selectPartyIdByTLId);
		jdbcTemplate.execute(selectPartyIdByTLId);

		// try {
		// List<String> partyIds = jdbcTemplate.queryForList(selectPartyIdByTLId,
		// paramMap, String.class);
		// List<String> fileIds = jdbcTemplate.query(selectFileIdTLId, paramMap, (rs,
		// idx) -> {
		// return rs.getString(Constants.FILE_ID);
		// });
		// List<String> adminIds = jdbcTemplate.queryForList(selectAdminIdByTLId,
		// paramMap, String.class);
		//
		// paramMap.addValue(Constants.ADMIN_ID, adminIds);
		// List<String> adminPartyIds =
		// jdbcTemplate.queryForList(selectAdminPartyIdByAId, paramMap, String.class);
		//
		// paramMap.addValue(Constants.PARTY_ID, partyIds);
		// paramMap.addValue(Constants.ADMIN_PARTY_ID, adminPartyIds);
		// List<String> addressIds = jdbcTemplate.queryForList(selectAddressIdByPId,
		// paramMap, String.class);
		// List<String> contactIds = jdbcTemplate.queryForList(selectContactIdByPId,
		// paramMap, String.class);
		//
		// paramMap.addValue(Constants.FILE_ID, fileIds);
		// paramMap.addValue(Constants.ADDRESS_ID, addressIds);
		// paramMap.addValue(Constants.CONTACT_ID, contactIds);
		//
		// jdbcTemplate.update(deleteTLAcctGroup, paramMap);
		// LOGGER.info("**** delete from TL_ACCT_GROUP SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteTLSchedule, paramMap);
		// LOGGER.info("**** delete from TL_SCHEDULE SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteAEFileRecipient, paramMap);
		// LOGGER.info("**** delete from AE_FILE_RECIPIENT SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteAEFile, paramMap);
		// LOGGER.info("**** delete from AE_FILE SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteTLDeliveryMethod, paramMap);
		// LOGGER.info("**** delete from TL_DELIVERY_METHOD SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteTLExcludeSecType, paramMap);
		// LOGGER.info("**** delete from TL_EXCLUDE_SEC_TYPE SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteTradeLetter, paramMap);
		// LOGGER.info("**** delete from TRADE_LETTER SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deletePartyAddress, paramMap);
		// LOGGER.info("**** delete from PARTY_ADDRESS SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteAddress, paramMap);
		// LOGGER.info("**** delete from ADDRESS SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deletePartyContactMech, paramMap);
		// LOGGER.info("**** delete from PARTY_CONTACT_MECHANISM SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteContactMech, paramMap);
		// LOGGER.info("**** delete from CONTACT_MECHANISM SUCCESSFULL ****");
		//
		// jdbcTemplate.update(deleteParty, paramMap);
		// LOGGER.info("**** delete from PARTY SUCCESSFULL ****");
		//
		// } catch (BadSqlGrammarException e) {
		// e.printStackTrace();
		// }
	}

}

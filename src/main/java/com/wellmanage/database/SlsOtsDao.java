package com.wellmanage.database;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath*:/spring-context/applicationContext-smoke-suite.xml" })

public class SlsOtsDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(SlsOtsDao.class);

	protected NamedParameterJdbcTemplate jdbcTemplate;

	protected DataSource dataSource;

	/** The search location SQL. */
	@Value("${sls.search.sql}")
	private String searchLocationSQL;

	/** The delete location SQL. */
	@Value("${sls.delete.sql}")
	private String deleteLocationSQL;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public String getSettlementLocationCount() {
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		return jdbcTemplate.queryForObject(searchLocationSQL, namedParams, String.class);
	}

	public void deleteSettlementLocation(String locCode) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource().addValue("LOC_CODE", locCode.toUpperCase());
		jdbcTemplate.update(deleteLocationSQL, paramMap);
		LOGGER.info("***Delete Successful***");

	}
}

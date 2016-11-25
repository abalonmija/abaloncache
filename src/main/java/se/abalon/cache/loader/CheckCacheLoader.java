package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CheckCacheLoader{//} extends AbstractCacheLoader {
/*
	private static MayflowerCacher cacher;
	private static KeyToValueEntityCacheIdentifier checkAccountCheckBarCodeModelIdentifier = new KeyToValueEntityCacheIdentifier("Check", "ACCOUNT", "Check", "BAR_CODE");

	public CheckCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
		setCacheIdentifiers();
	}

	public void load() throws Exception {
		setLoaded(false);
		try {
			BofPersistenceManager manager = null;
			try {
				manager = BofPersistenceManagerFactory.create();
				loadChecksCache(manager);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (manager != null && manager.currentTransaction().isActive()) {
					manager.currentTransaction().rollback();
				}
				if (manager != null) {
					manager.close();
					manager = null;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);

	}

	private void loadChecksCache(BofPersistenceManager manager) {
		loadChecksCache(null, null, manager);
	}

	public void updateCheckCacheForAccount(AbalonPrimaryKey accountPk, AbalonPrimaryKey checkTypePk, BofPersistenceManager manager) throws Exception {
		loadChecksCache(accountPk, checkTypePk, manager);
	}

	public void removeNonValidChecks() throws Exception {
		BofPersistenceManager manager = BofPersistenceManagerFactory.create();
		try {
			removeNonValidChecks(null, null, manager);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (manager != null && manager.currentTransaction().isActive()) {
				manager.currentTransaction().rollback();
			}
			if (manager != null) {
				manager.close();
				manager = null;
			}
		}
	}

	public void removeNonValidChecks(String conditionCheckBarCode, AbalonPrimaryKey conditionCheckTypePk, BofPersistenceManager manager) throws Exception {

		Boolean isStarted = manager.currentTransaction().isActive();
		try {
			SqlServerUtil sqlUtil;
			if (conditionCheckBarCode == null && conditionCheckTypePk == null) {
				sqlUtil = buildAndExecuteNonValidCheckSql(manager);
			} else {
				sqlUtil = buildAndExecuteNonValidCheckSql(conditionCheckBarCode, conditionCheckTypePk, manager);
			}
			HashMap<String, Object> sqlResultChecks = null;
			String checkBarCode = null;
			AbalonPrimaryKey accountPk = null;

			while ((sqlResultChecks = (HashMap) sqlUtil.next()) != null) {
				checkBarCode = sqlResultChecks.get("check_barcode").toString();
				if (sqlResultChecks.get("account_id") != null) {
					accountPk = new AbalonPrimaryKey(sqlResultChecks.get("account_id").toString());
				}
				if (cacher.keyExists(checkAccountCheckBarCodeModelIdentifier, accountPk)) {
					List<String> cachedCheckBarCodes = (List<String>) cacher.getValue(checkAccountCheckBarCodeModelIdentifier, accountPk);
					if (cachedCheckBarCodes.contains(checkBarCode)) {
						cachedCheckBarCodes.remove(checkBarCode);
					}
					if (cachedCheckBarCodes.size() == 0) {
						cacher.removeEntry(checkAccountCheckBarCodeModelIdentifier, accountPk);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isStarted && manager != null && manager.currentTransaction().isActive()) {
				manager.currentTransaction().rollback();
			}
		}
	}

	private SqlServerUtil buildAndExecuteFullCheckSql(AbalonPrimaryKey conditionAccountPk, AbalonPrimaryKey conditionCheckTypePk, BofPersistenceManager manager) throws ModelException, SQLException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		StringBuffer sql = new StringBuffer();
		SqlServerUtil sqlUtil;

		sql.append("SELECT c.barcode AS check_barcode, c.account AS account_id ");
		sql.append(" FROM  member_check c WITH (NOLOCK) ");
		sql.append(" LEFT JOIN check_type ct WITH (NOLOCK) ON ct.id = c.type ");
		sql.append(" LEFT JOIN tillstand t WITH (NOLOCK) ON t.id = c.checkstatus ");
		sql.append(" WHERE ct.listavailablechecks = 1 ");
		sql.append(" AND dbo.intToDatetime(c.validthru_date) >  '" + formatter.format(new Date()) + "'");
		sql.append(" AND (t.symbolen = 'active' OR t.symbolen = 'sent') ");
		if (conditionAccountPk != null) {
			sql.append(" AND c.account = " + conditionAccountPk.toString());
		} else {
			sql.append(" AND c.account is not null ");
		}
		if (conditionCheckTypePk != null) {
			sql.append(" AND ct.id = " + conditionCheckTypePk.toString());
		}
		sql.append(" ORDER BY c.account");
		sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "check_barcode", "account_id" });
		sqlUtil.execute();
		return sqlUtil;
	}

	private SqlServerUtil buildAndExecuteFullCheckSql(BofPersistenceManager manager) throws ModelException, SQLException {
		return buildAndExecuteFullCheckSql(null, null, manager);
	}

	private SqlServerUtil buildAndExecuteNonValidCheckSql(BofPersistenceManager manager) throws ModelException, SQLException {
		return buildAndExecuteFullCheckSql(null, null, manager);
	}

	private SqlServerUtil buildAndExecuteNonValidCheckSql(String conditionCheckBarCode, AbalonPrimaryKey conditionCheckTypePk, BofPersistenceManager manager) throws ModelException, SQLException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		StringBuffer sql = new StringBuffer();
		SqlServerUtil sqlUtil;

		sql.append("SELECT c.barcode AS check_barcode, c.account AS account_id ");
		sql.append(" FROM  member_check c WITH (NOLOCK) ");
		sql.append(" LEFT JOIN check_type ct WITH (NOLOCK) ON ct.id = c.type ");
		sql.append(" LEFT JOIN tillstand t WITH (NOLOCK) ON t.id = c.checkstatus ");
		sql.append(" WHERE (ct.listavailablechecks = 0 ");
		sql.append(" OR dbo.intToDatetime(c.validthru_date) < '" + formatter.format(new Date()) + "')");
		sql.append(" OR (t.symbolen = 'mak' OR t.symbolen = 'inactive' OR t.symbolen = 'expired') ");
		if (conditionCheckBarCode != null) {
			sql.append(" AND c.barcode = " + conditionCheckBarCode.toString());
		}
		if (conditionCheckTypePk != null) {
			sql.append(" AND ct.id = " + conditionCheckTypePk.toString());
		}
		sql.append(" ORDER BY c.account");
		sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "check_barcode", "account_id" });
		sqlUtil.execute();
		return sqlUtil;
	}

	@SuppressWarnings("unchecked")
	private void loadChecksCache(AbalonPrimaryKey conditionAccountPk, AbalonPrimaryKey conditionCheckTypePk, BofPersistenceManager manager) {
		Boolean isStarted = manager.currentTransaction().isActive();
		try {
			SqlServerUtil sqlUtil;
			if (conditionAccountPk == null && conditionCheckTypePk == null) {
				sqlUtil = buildAndExecuteFullCheckSql(manager);
			} else {
				sqlUtil = buildAndExecuteFullCheckSql(conditionAccountPk, conditionCheckTypePk, manager);
			}
			HashMap<String, Object> sqlResultChecks = null;
			String checkBarCode = null;
			AbalonPrimaryKey accountPk = null;
			while ((sqlResultChecks = (HashMap) sqlUtil.next()) != null) {
				checkBarCode = sqlResultChecks.get("check_barcode").toString();
				accountPk = new AbalonPrimaryKey(sqlResultChecks.get("account_id").toString());
				if (!cacher.keyExists(checkAccountCheckBarCodeModelIdentifier, accountPk)) {
					List<String> listToBeCached = new ArrayList<String>();
					listToBeCached.add(checkBarCode);
					cacher.put(checkAccountCheckBarCodeModelIdentifier, accountPk, listToBeCached);
				} else {
					List<String> cachedCheckBarCodes = (List<String>) cacher.getValue(checkAccountCheckBarCodeModelIdentifier, accountPk);
					if (!cachedCheckBarCodes.contains(checkBarCode)) {
						cacher.put(checkAccountCheckBarCodeModelIdentifier, accountPk, checkBarCode);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isStarted && manager != null && manager.currentTransaction().isActive()) {
				manager.currentTransaction().rollback();
			}
		}
	}

	public static void removeCheckEntryFromCaches(String checkBarCode, AbalonPrimaryKey accountPk) {
		if (cacher.keyExists(checkAccountCheckBarCodeModelIdentifier, accountPk)) {
			List<String> cachedCheckBarCodes = (List<String>) cacher.getValue(checkAccountCheckBarCodeModelIdentifier, accountPk);
			if (cachedCheckBarCodes.contains(checkBarCode)) {
				cachedCheckBarCodes.remove(checkBarCode);
			}
			if (cachedCheckBarCodes.size() == 0) {
				cacher.removeEntry(checkAccountCheckBarCodeModelIdentifier, accountPk);
			}
		}
	}

	private static void setCacheIdentifiers() {
		cacher.createCache(checkAccountCheckBarCodeModelIdentifier);

	}
*/
}
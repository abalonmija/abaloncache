package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.sql.SQLException;
import java.util.HashMap;

public class MemberAccountCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public MemberAccountCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		setLoaded(false);
		try {
			addMemberAccountCaches();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);

	}

	private static void addMemberAccountCaches() {
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			putCurrencies(manager);

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

	public static void putCurrencies(BofPersistenceManager manager) throws SQLException, ModelException {
		KeyToValueEntityCacheIdentifier pkCurrencyPkCacheIdentifier = new KeyToValueEntityCacheIdentifier("MemberAccount", "ID", "MemberAccount", "CURRENCY");
		cacher.createCache(pkCurrencyPkCacheIdentifier);

		StringBuffer sql = new StringBuffer();
		SqlServerUtil sqlUtil;
		sql.append("SELECT ma.currency AS currencyPk, ma.id AS pk");
		sql.append(" FROM member_account ma WITH (NOLOCK)");
		sql.append(" WHERE ma.currency IS NOT NULL");
		sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "currencyPk", "pk" });
		sqlUtil.execute();
		HashMap<String, Object> result;
		AbalonPrimaryKey accountPk;
		AbalonPrimaryKey currencyPk;
		while ((result = (HashMap) sqlUtil.next()) != null) {
			accountPk = new AbalonPrimaryKey(result.get("pk").toString());
			currencyPk = new AbalonPrimaryKey(result.get("currencyPk").toString());
			cacher.putKeyToValue(pkCurrencyPkCacheIdentifier, accountPk, currencyPk);
		}
		sqlUtil.close();
	}
}

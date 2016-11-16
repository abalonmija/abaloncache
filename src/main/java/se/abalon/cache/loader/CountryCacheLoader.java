package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;

import java.util.HashMap;

public class CountryCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public CountryCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		setLoaded(false);
		try {
			addCurrencyCache();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);
	}

	private static void addCurrencyCache() {
		BofPersistenceManager manager = null;
		try {
			KeyToValueEntityCacheIdentifier pkCurrencyPkIdentifier = new KeyToValueEntityCacheIdentifier("Country", "ID", "Country", "CURRENCY");
			cacher.createCache(pkCurrencyPkIdentifier);
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT c.valutaid AS currency_id, c.id AS country_id");
			sql.append(" FROM land c WITH (NOLOCK) ");
			sql.append(" WHERE c.valutaid IS NOT NULL ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "currency_id", "country_id" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey currencyPk = null;
			AbalonPrimaryKey countryPk = null;
			while ((result = (HashMap) sqlUtil.next()) != null) {
				currencyPk = new AbalonPrimaryKey(result.get("currency_id").toString());
				countryPk = new AbalonPrimaryKey(result.get("country_id").toString());
				cacher.putKeyToValue(pkCurrencyPkIdentifier, countryPk, currencyPk);
			}
			sqlUtil.close();
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
}

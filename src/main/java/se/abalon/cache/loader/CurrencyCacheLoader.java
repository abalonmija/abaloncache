package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class CurrencyCacheLoader{// extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public CurrencyCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		/*setLoaded(false);
		try {
			addCurrencyCaches();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);*/
	}
/*
	private static void addCurrencyCaches() {

		BofPersistenceManager manager = null;
		try {
			KeyToValueEntityCacheIdentifier pkCodeCacheIdentifier = new KeyToValueEntityCacheIdentifier("Currency", "ID", "Currency", "CODE");
			KeyToValueEntityCacheIdentifier codePkCacheIdentifier = new KeyToValueEntityCacheIdentifier("Currency", "CODE", "Currency", "ID");
			cacher.createCache(pkCodeCacheIdentifier);
			cacher.createCache(codePkCacheIdentifier);
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT c.id AS currency_id, c.kod AS currency_code");
			sql.append(" FROM valuta c WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "currency_id", "currency_code" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey currencyPk = null;
			String currencyCode = null;
			while ((result = (HashMap) sqlUtil.next()) != null) {
				currencyPk = new AbalonPrimaryKey(result.get("currency_id").toString());
				currencyCode = result.get("currency_code").toString();
				cacher.putKeyToValue(pkCodeCacheIdentifier, currencyPk, currencyCode);
				cacher.putKeyToValue(codePkCacheIdentifier, currencyCode, currencyPk);
			}
			sqlUtil.close();

			// addCurrencyMemberAccountCache(manager);

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
*/
}

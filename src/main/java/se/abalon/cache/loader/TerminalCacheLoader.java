package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class TerminalCacheLoader {//extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public TerminalCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		/*setLoaded(false);
		try {
			addNumberPkToCache();
			addPkNumberToCache();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);
*/
	}
/*
	private static void addNumberPkToCache() {
		// String keyModelName = "Terminal";
		// String keyFieldName = "TERMINAL_NUMBER";
		// String valueModelName = "Terminal";
		// String valueFieldName = "ID";
		//
		// try {
		// BofPersistenceManager manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
		// BofQuery q = (BofQuery) manager.newQuery();
		// q.addModel(keyModelName, "m");
		// q.addField("m", keyFieldName);
		// q.addField("m", valueFieldName);
		// Collection<Terminal> terminals = (Collection<Terminal>) q.execute();
		// MappedCacheIdentifier cacheIdentifier = new MappedCacheIdentifier(keyModelName, keyFieldName, valueModelName, valueFieldName);
		// cacher.createCache(cacheIdentifier);
		// for (Terminal terminal : terminals) {
		// cacher.putMapped(cacheIdentifier, terminal.getFieldTerminalNumber(), terminal.getFieldId());
		// }
		// manager.close();
		// manager = null;
		// } catch (ModelException e) {
		// e.printStackTrace();
		// }
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			// BofQuery q = (BofQuery) manager.newQuery();
			// q.addModel("Product", "m");
			// q.addField("m", "CODE");
			// q.addField("m", "DESC");
			// q.addField("m", "ID");
			// Collection<Product> products = (Collection<Product>) q.execute();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT t.terminal AS terminal_terminalnumber, t.id AS terminal_id");
			sql.append(" FROM terminal t WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "terminal_terminalnumber", "terminal_id" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey terminalId;
			String terminalNumber;
			KeyToValueEntityCacheIdentifier terminalNumberPkCacheIdentifier = new KeyToValueEntityCacheIdentifier("Terminal", "TERMINAL_NUMBER", "Terminal", "ID");
			cacher.createCache(terminalNumberPkCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				terminalId = new AbalonPrimaryKey(result.get("terminal_id").toString());
				terminalNumber = result.get("terminal_terminalnumber").toString();
				cacher.putKeyToValue(terminalNumberPkCacheIdentifier, terminalNumber, terminalId);
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

	private static void addPkNumberToCache() {
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT t.id AS terminal_id,t.terminal AS terminal_terminalnumber");
			sql.append(" FROM terminal t WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "terminal_id", "terminal_terminalnumber" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey terminalId;
			String terminalNumber;
			KeyToValueEntityCacheIdentifier terminalPkNumberCacheIdentifier = new KeyToValueEntityCacheIdentifier("Terminal", "ID", "Terminal", "TERMINAL_NUMBER");
			cacher.createCache(terminalPkNumberCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				terminalId = new AbalonPrimaryKey(result.get("terminal_id").toString());
				terminalNumber = result.get("terminal_terminalnumber").toString();
				cacher.putKeyToValue(terminalPkNumberCacheIdentifier, terminalId, terminalNumber);
			}
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

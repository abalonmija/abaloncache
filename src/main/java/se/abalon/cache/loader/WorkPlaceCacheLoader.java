package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkPlaceCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public WorkPlaceCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		setLoaded(false);
		try {
			addWorkPlaceCaches();
			// addNumberPkToCache();
			// addPkTerminalNumbersToCache();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);

	}

	private static void addWorkPlaceCaches() {
		BofPersistenceManager manager = null;
		try {
			// BofPersistenceManager manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			// BofQuery q = (BofQuery) manager.newQuery();
			// q.addModel(keyModelName, "m");
			// q.addField("m", keyFieldName);
			// q.addField("m", valueFieldName);
			// Collection<WorkPlace> workPlaces= (Collection<WorkPlace>) q.execute();
			KeyToValueEntityCacheIdentifier pkNumberCacheIdentifier = new KeyToValueEntityCacheIdentifier("WorkPlace", "ID", "WorkPlace", "NUMBER");
			KeyToValueEntityCacheIdentifier numberPkCacheIdentifier = new KeyToValueEntityCacheIdentifier("WorkPlace", "NUMBER", "WorkPlace", "ID");
			KeyToValueEntityCacheIdentifier pkTerminalNumbersCacheIdentifier = new KeyToValueEntityCacheIdentifier("WorkPlace", "ID", "Terminal", "TERMINAL_NUMBER");
			cacher.createCache(pkNumberCacheIdentifier);
			cacher.createCache(numberPkCacheIdentifier);
			cacher.createCache(pkTerminalNumbersCacheIdentifier);
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT w.id AS workplace_id, w.nummer AS workplace_number, t.terminal AS terminal_terminalnumber");
			sql.append(" FROM arbetsstalle w WITH (NOLOCK) ");
			sql.append(" LEFT JOIN terminal t WITH (NOLOCK) ON t.workplace = w.id");
			sql.append(" ORDER BY w.id");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "workplace_id", "workplace_number", "terminal_terminalnumber" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey workPlacePk = null;
			String lastWorkPlaceNumber = null;
			String workPlaceNumber = null;
			String terminalNumber = null;
			ArrayList<String> terminalNumbers = null;
			while ((result = (HashMap) sqlUtil.next()) != null) {
				workPlacePk = new AbalonPrimaryKey(result.get("workplace_id").toString());
				workPlaceNumber = result.get("workplace_number").toString();
				if (result.get("terminal_terminalnumber") != null) {
					terminalNumber = result.get("terminal_terminalnumber").toString();
				} else {
					terminalNumber = null;
				}
				cacher.putKeyToValue(numberPkCacheIdentifier, workPlaceNumber, workPlacePk);
				cacher.putKeyToValue(pkNumberCacheIdentifier, workPlacePk, workPlaceNumber);
				if (terminalNumber != null && (lastWorkPlaceNumber == null || !lastWorkPlaceNumber.equals(workPlaceNumber))) {
					lastWorkPlaceNumber = workPlaceNumber;
					terminalNumbers = new ArrayList<String>();
					terminalNumbers.add(terminalNumber);
					cacher.putKeyToValue(pkTerminalNumbersCacheIdentifier, workPlacePk, terminalNumbers);
				} else if (terminalNumber != null && lastWorkPlaceNumber.equals(workPlaceNumber)) {
					cacher.putKeyToValue(pkTerminalNumbersCacheIdentifier, workPlacePk, terminalNumber);
				}
			}
			// for (WorkPlace workPlace : workPlaces) {
			// cacher.putMapped(cacheIdentifier, workPlace.getFieldId(), workPlace.getFieldNumber());
			// }
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

	private static void addPkTerminalNumbersToCache() {
		String keyModelName = "WorkPlace";
		String keyFieldName = "ID";
		String valueModelName = "Terminal";
		String valueFieldName = "TERMINAL_NUMBER";

		try {
			BofPersistenceManager manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			BofQuery q = (BofQuery) manager.newQuery();
			q.addModel(keyModelName, "m");
			q.addField("m", keyFieldName);
			q.addModel(valueModelName, "t");
			q.addRelation("m", "t", "terminals");
			q.addField("t", valueFieldName);
			Collection<WorkPlace> workPlaces = (Collection<WorkPlace>) q.execute();
			KeyToValueEntityCacheIdentifier cacheIdentifier = new KeyToValueEntityCacheIdentifier(keyModelName, keyFieldName, valueModelName, valueFieldName);
			cacher.createCache(cacheIdentifier);
			for (WorkPlace workPlace : workPlaces) {
				ArrayList<String> terminalNumbers = new ArrayList<String>();
				for (Model terminal : workPlace.getRelation("terminals")) {
					terminalNumbers.add(terminal.getField("TERMINAL_NUMBER").toString());
				}
				cacher.putKeyToValue(cacheIdentifier, workPlace.getFieldId(), terminalNumbers);
			}
			manager.close();
			manager = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addNumberPkToCache() {
		String keyModelName = "WorkPlace";
		String keyFieldName = "NUMBER";
		String valueModelName = "WorkPlace";
		String valueFieldName = "ID";
		try {
			BofPersistenceManager manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			BofQuery q = (BofQuery) manager.newQuery();
			q.addModel(keyModelName, "m");
			q.addField("m", keyFieldName);
			q.addField("m", valueFieldName);
			Collection<WorkPlace> workPlaces = (Collection<WorkPlace>) q.execute();
			KeyToValueEntityCacheIdentifier cacheIdentifier = new KeyToValueEntityCacheIdentifier(keyModelName, keyFieldName, valueModelName, valueFieldName);
			cacher.createCache(cacheIdentifier);
			for (WorkPlace workPlace : workPlaces) {
				cacher.putKeyToValue(cacheIdentifier, workPlace.getFieldNumber(), workPlace.getFieldId());
			}
			manager.close();
			manager = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

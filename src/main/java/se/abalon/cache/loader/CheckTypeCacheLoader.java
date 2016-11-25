package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.sql.SQLException;
import java.util.HashMap;

public class CheckTypeCacheLoader {//extends AbstractCacheLoader {

	private static MayflowerCacher cacher;
	private static KeyToValueEntityCacheIdentifier checkTypePkModelCacheIdentifier = new KeyToValueEntityCacheIdentifier("CheckType", "ID", "CheckType", "Json");

	/*
	public CheckTypeCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
		createCache();
	}


	public void load() throws Exception {
		setLoaded(false);
		try {
			BofPersistenceManager manager = null;
			try {
				manager = BofPersistenceManagerFactory.create();
				loadCacheTypeCache(manager);
				setLoaded(true);
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
	}

	@SuppressWarnings("unchecked")
	public void loadCacheTypeCache(BofPersistenceManager manager) {
		Boolean isStarted = manager.currentTransaction().isActive();
		try {
			cacher.emptyCache(checkTypePkModelCacheIdentifier);
				
			SqlServerUtil sqlUtil;
			sqlUtil = buildAndExecuteCheckTypeIdSql(manager);
			HashMap<String, Object> sqlResultCheckTypes = null;
			AbalonPrimaryKey checkTypePk = null;
			while ((sqlResultCheckTypes = (HashMap) sqlUtil.next()) != null) {
				checkTypePk = new AbalonPrimaryKey(sqlResultCheckTypes.get("checktype_id").toString());
				CheckType checkType = (CheckType) manager.findBusinessObject("CheckType", checkTypePk);
				cacher.put(checkTypePkModelCacheIdentifier, checkTypePk, checkType.getAsJson());
				}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isStarted && manager != null && manager.currentTransaction().isActive()) {
				manager.currentTransaction().rollback();
			}
		}
	}

	private SqlServerUtil buildAndExecuteCheckTypeIdSql(BofPersistenceManager manager) throws ModelException, SQLException {
		StringBuffer sql = new StringBuffer();
		SqlServerUtil sqlUtil;
		sql.append("SELECT ct.id AS checktype_id");
		sql.append(" FROM check_type ct WITH (NOLOCK) ");
		sql.append(" WHERE ct.listavailablechecks = 1");
		sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "checktype_id" });
		sqlUtil.execute();
		return sqlUtil;
	}

	private static void createCache() {
		cacher.createLimitedCache(checkTypePkModelCacheIdentifier, 5);
	}
	*/
}
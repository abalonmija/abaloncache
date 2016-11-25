package se.abalon.cache.loader;

import se.abalon.cache.resource.ResourceMap;
import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class MobileNumberCacheLoader {//extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public MobileNumberCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
	/*	setLoaded(false);
		try {
			if (((String) getConfigurations().lookup("se.abalon.mfservice.MemberUtil.useMobileAsMemberId")).equalsIgnoreCase("true")) {
				addToCache();
			}
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);*/
	}
/*
	private static void addToCache() {
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT mobile,id FROM person WHERE mobile IS NOT NULL");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "mobile", "id" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			String mobile;
			AbalonPrimaryKey personPk;
			KeyToValueEntityCacheIdentifier mobilePersonPkCacheIdentifier = new KeyToValueEntityCacheIdentifier("Person", "MOBILE", "Person", "ID");
			KeyToValueEntityCacheIdentifier personPkMobileCacheIdentifier = new KeyToValueEntityCacheIdentifier("Person", "ID", "Person", "MOBILE");
			cacher.createCache(mobilePersonPkCacheIdentifier);
			cacher.createCache(personPkMobileCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				personPk = new AbalonPrimaryKey(result.get("id").toString());
				mobile = result.get("mobile").toString();
				cacher.putKeyToValue(mobilePersonPkCacheIdentifier, mobile, personPk);
				cacher.putKeyToValue(personPkMobileCacheIdentifier, personPk, mobile);
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

	public static ResourceMap getConfigurations() {
		ResourceMap map = new ResourceMap();

		map.put("se.abalon.mfservice.MemberUtil.useMobileAsMemberId", String.class, "If mobile is to be used as member id");
		return map;

	}
*/
}

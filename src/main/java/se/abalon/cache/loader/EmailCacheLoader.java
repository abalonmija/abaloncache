package se.abalon.cache.loader;

import se.abalon.cache.resource.ResourceMap;
import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class EmailCacheLoader{// extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public EmailCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		/*setLoaded(false);
		try {
			if (((String) getConfigurations().lookup("se.abalon.mfservice.MemberUtil.useEmailAsMemberId")).equalsIgnoreCase("true")) {
				addToCache();
			}
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);
*/
	}
/*
	private static void addToCache() {
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			// I wanted to use the EMailAddress/epostadress model/table for this cache, but since we use bitmaps and crap for case insensitivity i can't. I can't get the persistent
			// value, which makes it impossible to update the cache if the value changes.
			// sql.append("SELECT UPPER(e.text) AS epost, a.adress_agare AS adress_agare");
			// sql.append(" FROM epostadress e INNER JOIN adress a ON e.isa_instance = a.id and a.adress_agare IS NOT NULL WHERE e.text != '' AND e.text IS NOT NULL");
			sql.append("SELECT id,email AS epost FROM person WHERE email IS NOT NULL");

			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "epost", "id" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			String email;
			AbalonPrimaryKey personPk;
			KeyToValueEntityCacheIdentifier emailPersonPkCacheIdentifier = new KeyToValueEntityCacheIdentifier("Person", "EMAIL", "Person", "ID");
			cacher.createCache(emailPersonPkCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				personPk = new AbalonPrimaryKey(result.get("id").toString());
				email = result.get("epost").toString();
				cacher.putKeyToValue(emailPersonPkCacheIdentifier, email, personPk);
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

		map.put("se.abalon.mfservice.MemberUtil.useEmailAsMemberId", String.class, "If email is to be used as member id");
		return map;

	}
*/
}

package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class ProductMainGroupCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public ProductMainGroupCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		setLoaded(false);
		try {
			addCodePkAndPkDescToCache();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);

	}

	private static void addCodePkAndPkDescToCache() {
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();

			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT pmg.id AS productmaingroup_id, pmg.kod AS productmaingroup_code, pmg.beskr AS productmaingroup_desc");
			sql.append(" FROM prodhuvudgrupp pmg WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "productmaingroup_id", "productmaingroup_code", "productmaingroup_desc" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey productMainGroupId;
			String productMainGroupCode;
			String productMainGroupDesc;

			KeyToValueEntityCacheIdentifier codePkCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductMainGroup", "CODE", "ProductMainGroup", "ID");
			KeyToValueEntityCacheIdentifier pkDescCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductMainGroup", "ID", "ProductMainGroup", "DESC");
			cacher.createCache(codePkCacheIdentifier);
			cacher.createCache(pkDescCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				productMainGroupId = new AbalonPrimaryKey(result.get("productmaingroup_id").toString());
				productMainGroupCode = result.get("productmaingroup_code").toString();
				productMainGroupDesc = result.get("productmaingroup_desc").toString();
				cacher.putKeyToValue(codePkCacheIdentifier, productMainGroupCode, productMainGroupId);
				cacher.putKeyToValue(pkDescCacheIdentifier, productMainGroupId, productMainGroupDesc);
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
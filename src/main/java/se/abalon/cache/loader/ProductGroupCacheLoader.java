package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class ProductGroupCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public ProductGroupCacheLoader() throws Exception {
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
			// BofQuery q = (BofQuery) manager.newQuery();
			// q.addModel("ProductGroup", "m");
			// q.addField("m", "CODE");
			// q.addField("m", "DESC");
			// q.addField("m", "ID");
			// Collection<ProductGroup> productGroups = (Collection<ProductGroup>) q.execute();

			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT pg.id AS productgroup_id, pg.kod AS productgroup_code, pg.beskr AS productgroup_desc");
			sql.append(" FROM prodgrupp pg WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "productgroup_id", "productgroup_code", "productgroup_desc" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey productGroupId;
			String productGroupCode;
			String productGroupDesc;

			KeyToValueEntityCacheIdentifier codePkCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductGroup", "CODE", "ProductGroup", "ID");
			KeyToValueEntityCacheIdentifier pkDescCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductGroup", "ID", "ProductGroup", "DESC");
			cacher.createCache(codePkCacheIdentifier);
			cacher.createCache(pkDescCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				productGroupId = new AbalonPrimaryKey(result.get("productgroup_id").toString());
				productGroupCode = result.get("productgroup_code").toString();
				productGroupDesc = result.get("productgroup_desc").toString();
				cacher.putKeyToValue(codePkCacheIdentifier, productGroupCode, productGroupId);
				cacher.putKeyToValue(pkDescCacheIdentifier, productGroupId, productGroupDesc);
			}

			// for (ProductGroup productGroup : productGroups) {
			// cacher.putMapped(codePkCacheIdentifier, productGroup.getFieldCode(), productGroup.getFieldId());
			// cacher.putMapped(pkDescCacheIdentifier, productGroup.getFieldId(), productGroup.getFieldDesc());
			// }
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
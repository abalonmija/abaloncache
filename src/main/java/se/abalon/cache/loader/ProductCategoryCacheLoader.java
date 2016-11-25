package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;


public class ProductCategoryCacheLoader{// extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public ProductCategoryCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		/*setLoaded(false);
		try {
			addCodePkAndPkDescToCache();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);
*/
	}
/*
	private static void addCodePkAndPkDescToCache() {
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();

			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT pc.id AS productcategory_id, pc.kod AS productcategory_code, pc.beskr AS productcategory_desc");
			sql.append(" FROM prodkategori pc WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "productcategory_id", "productcategory_code", "productcategory_desc" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey productCategoryId;
			String productCategoryCode;
			String productCategoryDesc;

			KeyToValueEntityCacheIdentifier codePkCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductCategory", "CODE", "ProductCategory", "ID");
			KeyToValueEntityCacheIdentifier pkDescCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductCategory", "ID", "ProductCategory", "DESC");
			cacher.createCache(codePkCacheIdentifier);
			cacher.createCache(pkDescCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				productCategoryId = new AbalonPrimaryKey(result.get("productcategory_id").toString());
				productCategoryCode = result.get("productcategory_code").toString();
				productCategoryDesc = result.get("productcategory_desc").toString();
				cacher.putKeyToValue(codePkCacheIdentifier, productCategoryCode, productCategoryId);
				cacher.putKeyToValue(pkDescCacheIdentifier, productCategoryId, productCategoryDesc);
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
	*/
}
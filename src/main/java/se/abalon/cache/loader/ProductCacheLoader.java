package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class ProductCacheLoader{// extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public ProductCacheLoader() throws Exception {
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
		Object o;
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
			sql.append("SELECT p.id AS product_id, p.kod AS product_code, p.beskr AS product_desc");
			sql.append(" FROM produkt p WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "product_id", "product_code", "product_desc" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey productId;
			String productCode;
			String productDesc;
			KeyToValueEntityCacheIdentifier codePkCacheIdentifier = new KeyToValueEntityCacheIdentifier("Product", "CODE", "Product", "ID");
			KeyToValueEntityCacheIdentifier pkDescCacheIdentifier = new KeyToValueEntityCacheIdentifier("Product", "ID", "Product", "DESC");
			cacher.createCache(codePkCacheIdentifier);
			cacher.createCache(pkDescCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				productId = new AbalonPrimaryKey(result.get("product_id").toString());
				productCode = result.get("product_code").toString();
				o = result.get("product_desc");
				if(o != null){
					productDesc = o.toString();
				} else {
					productDesc = null;
				}
				cacher.putKeyToValue(codePkCacheIdentifier, productCode, productId);
				cacher.putKeyToValue(pkDescCacheIdentifier, productId, productDesc);
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

package se.abalon.cache.loader;


import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

import java.util.HashMap;

public class ProductGTINCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public ProductGTINCacheLoader() throws Exception {
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
		Object o;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			StringBuffer sql = new StringBuffer();
			SqlServerUtil sqlUtil;
			sql.append("SELECT p.id AS productgtin_id, p.gtin AS productgtin_gtin, p.product productgtin_product");
			sql.append(" FROM productgtin p WITH (NOLOCK) ");
			sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[] { "productgtin_id", "productgtin_gtin","productgtin_product" });
			sqlUtil.execute();
			HashMap<String, Object> result;
			AbalonPrimaryKey productGTINId;
			String productGTIN;
			AbalonPrimaryKey productGTIN_productId;
			KeyToValueEntityCacheIdentifier gtinPkCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductGTIN", "GTIN", "ProductGTIN", "ID");
			KeyToValueEntityCacheIdentifier gtinProductCacheIdentifier = new KeyToValueEntityCacheIdentifier("ProductGTIN", "ID", "Product", "ID");
			cacher.createCache(gtinPkCacheIdentifier);
			cacher.createCache(gtinProductCacheIdentifier);

			while ((result = (HashMap) sqlUtil.next()) != null) {
				productGTINId = new AbalonPrimaryKey(result.get("productgtin_id").toString());
				productGTIN = result.get("productgtin_gtin").toString();
				Object productgtinObject = result.get("productgtin_product");
				if (productgtinObject!= null){
					productGTIN_productId = new AbalonPrimaryKey(productgtinObject.toString());
				} else {
					productGTIN_productId = null;
				}



				cacher.putKeyToValue(gtinPkCacheIdentifier, productGTIN, productGTINId);
				cacher.putKeyToValue(gtinProductCacheIdentifier, productGTINId, productGTIN_productId);
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

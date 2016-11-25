package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;
import se.abalon.cache.type.AbalonPrimaryKey;

public class CurrencyPeriodCacheLoader{// extends AbstractCacheLoader {

	public static final AbalonPrimaryKey CURRENT_CURRENCY_PERIOD_KEY = new AbalonPrimaryKey(0);
	private static MayflowerCacher cacher;

	public CurrencyPeriodCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		/*setLoaded(false);
		try {
			addCurrencyPeriodCaches();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);*/
	}
/*
	private static void addCurrencyPeriodCaches() {
		BofPersistenceManager manager = null;
		try {
			manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			//putCurrencyPeriods(manager);
			putExchangeRates(manager);

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

	public static void putExchangeRates(BofPersistenceManager manager) throws ModelException {
		int nbrOfPosts = 10000;
		KeyToValueEntityCacheIdentifier currencyPeriodExchangeRatesIdentifier = new KeyToValueEntityCacheIdentifier("CurrencyPeriod", "ID", "ExchangeRate", null);
		cacher.createLimitedCache(currencyPeriodExchangeRatesIdentifier, nbrOfPosts);
		BofQuery query = (BofQuery) manager.newQuery();
		query.addModel("CurrencyPeriod", "cp");
		query.addModel("ExchangeRate", "er");
		query.addRelation("cp", "er", "exchangeRates");
		query.addField("cp", "ID");
		query.addAllFields("er");
		query.addOrderBy("cp", "END_DATE", Condition.DESCENDING);

		Collection<CurrencyPeriod> currencyPeriods = (Collection<CurrencyPeriod>) query.execute();
		for (CurrencyPeriod currencyPeriod : currencyPeriods) {
			cacher.put(currencyPeriodExchangeRatesIdentifier, currencyPeriod.getFieldId(), currencyPeriod.getRelationExchangeRate());
		}
	}

	public static void putCurrencyPeriods(BofPersistenceManager manager) throws ModelException {
		KeyToValueEntityCacheIdentifier pkModelCacheIdentifier = new KeyToValueEntityCacheIdentifier("CurrencyPeriod", "ID", "CurrencyPeriod", null);
		cacher.createCache(pkModelCacheIdentifier);

		AbalonPrimaryKey currentCurrencyPeriodPk = null;
		BofQuery query = (BofQuery) manager.newQuery();
		query.addModel("CurrencyPeriod", "cp");
		query.addAllFields("cp");
		Collection<CurrencyPeriod> currencyPeriods = (Collection<CurrencyPeriod>) query.execute();

		if (currencyPeriods != null) {
			for (CurrencyPeriod currencyPeriod : currencyPeriods) {
				cacher.put(pkModelCacheIdentifier, currencyPeriod.getFieldId(), currencyPeriod);
				if (currencyPeriod.getFieldEndDate() == null) {
					cacher.putKeyToValue(pkModelCacheIdentifier, CURRENT_CURRENCY_PERIOD_KEY, currencyPeriod);
				}
			}

		}
	}
	*/
}

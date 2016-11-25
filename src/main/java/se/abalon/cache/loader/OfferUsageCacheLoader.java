package se.abalon.cache.loader;

import se.abalon.cache.threading.CompositeKeyToValueObjectCacheIdentifier;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OfferUsageCacheLoader{// extends AbstractCacheLoader {

    private static MayflowerCacher cacher;
    private static CompositeKeyToValueObjectCacheIdentifier offerUsageAndAccountIdCacheIdentifier = new CompositeKeyToValueObjectCacheIdentifier("OfferUsage", "OFFER", "ACCOUNT", "ID");

    public OfferUsageCacheLoader() throws Exception {
        cacher = MayflowerCacher.getMayflowerCacher();
        createCaches();
    }

    public void load() throws Exception {
        /*setLoaded(false);
        try {
            BofPersistenceManager manager = null;
            try {
                manager = BofPersistenceManagerFactory.create();
                loadOfferUsagesCache(manager);
                setLoaded(true);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (manager != null && manager.currentTransaction().isActive()) {
                    manager.currentTransaction().rollback();
                }
                if (manager != null) {
                    manager.close();
                }
            }
        } catch (Exception e) {
            throw e;
        }*/
    }
/*
    private void loadOfferUsagesCache(BofPersistenceManager manager) {
        try {

            Set<Integer> noIndividualOfferIds = OfferUtil.getValidOfferIds(manager, null, false);
            Set<Integer> individualOfferIds = OfferUtil.getValidOfferIds(manager, null, true);

            for (Integer offerId : noIndividualOfferIds) {
                addOfferUsagesToCache(manager, offerId, false);
            }
            for (Integer offerId : individualOfferIds) {
                addOfferUsagesToCache(manager, offerId, true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (manager.currentTransaction().isActive()) {
                manager.currentTransaction().rollback();
            }
        }
    }

    private SqlServerUtil buildAndExecuteNoIndividualOfferUsagesQuerySql(BofPersistenceManager manager, Integer offerPk) throws ModelException, SQLException {
        StringBuffer sql = new StringBuffer();
        SqlServerUtil sqlUtil;
        sql.append("SELECT ou.id AS offerusage_id, ou.offer AS offer_id, ou.account AS account_id");
        sql.append(" FROM offerusage ou WITH (NOLOCK)");
        sql.append(" WHERE ou.offer = ");
        sql.append(offerPk);
        sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[]{"offer_id", "account_id", "offerusage_id"});
        sqlUtil.execute();
        return sqlUtil;
    }

    private SqlServerUtil buildAndExecuteIndividualOfferUsagesQuerySql(BofPersistenceManager manager, Integer offerPk) throws ModelException, SQLException {
        StringBuffer sql = new StringBuffer();
        SqlServerUtil sqlUtil;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sql.append("SELECT DISTINCT ou.id AS offerusage_id, ou.offer AS offer_id, ou.account AS account_id");
        sql.append(" FROM offerusage ou WITH (NOLOCK)");
        sql.append(" INNER JOIN offervalidity ov WITH (NOLOCK) ON ou.offer = ov.offer");
        sql.append(" WHERE ou.offer = ");
        sql.append(offerPk);
        sql.append(" AND ov.validitydate > '");
        sql.append(formatter.format(new Date()));
        sql.append("'");

        sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[]{"offer_id", "account_id", "offerusage_id"});
        sqlUtil.execute();
        return sqlUtil;
    }

    public void addOfferUsagesToCache(BofPersistenceManager manager, Integer offerConditionPk, Boolean individual) throws SQLException, ModelException, ParseException {
        SqlServerUtil sqlUtil;
        if (individual) {
            sqlUtil = buildAndExecuteIndividualOfferUsagesQuerySql(manager, offerConditionPk);
        } else {
            sqlUtil = buildAndExecuteNoIndividualOfferUsagesQuerySql(manager, offerConditionPk);
        }

        HashMap<String, Object> result;
        Integer offerPk;
        Integer accountPk;
        StringBuilder concatenatedKey = null;

        while ((result = (HashMap) sqlUtil.next()) != null) {
            offerPk = Integer.valueOf(result.get("offer_id").toString());
            accountPk = Integer.valueOf(result.get("account_id").toString());
            OfferUsage offerUsage = (OfferUsage) manager.findBusinessObject("OfferUsage", new AbalonPrimaryKey(result.get("offerusage_id").toString()));
            cacher.putCompositeKeyToValue(offerUsageAndAccountIdCacheIdentifier, offerPk, accountPk, Integer.valueOf(offerUsage.getFieldId().toString()));
        }
    }
*/
    private static void createCaches() {
        cacher.createCache(offerUsageAndAccountIdCacheIdentifier);
    }

}
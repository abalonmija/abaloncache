package se.abalon.cache.loader;

import se.abalon.cache.threading.CompositeKeyToValueObjectCacheIdentifier;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class OfferValidityCacheLoader {//extends AbstractCacheLoader {

    private static MayflowerCacher cacher;
    private static CompositeKeyToValueObjectCacheIdentifier offerValidityCacheIdentifier = new CompositeKeyToValueObjectCacheIdentifier("OfferValidity", "OFFER", "MEMBER_ACCOUNT", "ID");

    public OfferValidityCacheLoader() throws Exception {
        cacher = MayflowerCacher.getMayflowerCacher();
        createCaches();
    }

    private static void createCaches() {
        cacher.createCache(offerValidityCacheIdentifier);
    }

    public void load() throws Exception {
      /*  setLoaded(false);
        try {
            BofPersistenceManager manager = null;
            try {
                manager = BofPersistenceManagerFactory.create();
                loadOfferValidityCache(manager);
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
        }*/
    }
/*
    private void addOfferValiditiesToCache(BofPersistenceManager manager, Integer offerConditionPk) throws SQLException, ModelException, ParseException {
        SqlServerUtil sqlUtil = buildAndExecuteOfferValiditysQuerySql(manager, offerConditionPk);
        HashMap<String, Object> result = null;
        AbalonPrimaryKey accountPk = null;
        while ((result = (HashMap) sqlUtil.next()) != null) {
            accountPk = new AbalonPrimaryKey(result.get("memberaccount").toString());
            cacher.putCompositeKeyToValue(offerValidityCacheIdentifier, offerConditionPk, accountPk, Integer.valueOf(result.get("id").toString()));
        }
    }

    private SqlServerUtil buildAndExecuteOfferValiditysQuerySql(BofPersistenceManager manager, Integer offerPk) throws ModelException, SQLException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = formatter.format(new Date());
        StringBuilder sql = new StringBuilder();
        SqlServerUtil sqlUtil;
        sql.append("SELECT ov.id AS id, ov.memberaccount AS memberaccount");
        sql.append(" FROM offervalidity ov WITH (NOLOCK)");
        sql.append(" WHERE ov.offer = ");
        sql.append(offerPk);
        sql.append(" AND (CONVERT(nvarchar(10), dbo.adjustForAppTime(ov.validitystartdate), 121) <= '");
        sql.append(todayStr);
        sql.append("' OR ov.validitystartdate IS NULL)");
        sql.append(" AND CONVERT(nvarchar(10), dbo.adjustForAppTime(ov.validitydate), 121) >= '");
        sql.append(todayStr);
        sql.append("'");
        sqlUtil = new SqlServerUtil(manager, sql.toString(), new String[]{"id", "memberaccount"});
        sqlUtil.execute();
        return sqlUtil;
    }

    private void loadOfferValidityCache(BofPersistenceManager manager) {
        try {
            Set<Integer> offerIds = OfferUtil.getValidOfferIds(manager, null, true);
            for (Integer offerId : offerIds) {
                addOfferValiditiesToCache(manager, offerId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (manager.currentTransaction().isActive()) {
                manager.currentTransaction().rollback();
            }
        }
    }
    */
}
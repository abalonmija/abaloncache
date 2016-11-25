package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyEntityCacheIdentifier;

public class CommunicationMonitorCacheLoader {//extends AbstractCacheLoader {

    private static MayflowerCacher cacher;
/*
    public CommunicationMonitorCacheLoader() throws Exception {
        cacher = MayflowerCacher.getMayflowerCacher();
    }

    public void load() throws Exception {
        setLoaded(false);
        try {
            createMonitorCache();
        } catch (Exception e) {
            throw e;
        }
        setLoaded(true);

    }


    private static void createMonitorCache() {
        String modelName = "CommunicationMonitor";
        String fieldName = "MONITOR_NBR";
        CommunicationMonitor monitor = null;
        KeyEntityCacheIdentifier monitorCacheIdentifier;
        BofPersistenceManager manager = null;

        try {
            manager = BofPersistenceManagerFactory.create();

            BofQuery query = (BofQuery) manager.newQuery();
            query.addModel("CommunicationMonitor", "monitor");
            query.addAllFields("monitor");

            Collection<CommunicationMonitor> monitors = (Collection<CommunicationMonitor>) query.execute();

            if (monitors.size() > 0) {
                monitor = monitors.iterator().next();
                cacher = MayflowerCacher.getMayflowerCacher();
                monitorCacheIdentifier = new KeyEntityCacheIdentifier(modelName, fieldName);
                cacher.createLimitedCache(monitorCacheIdentifier, 1);
                cacher.putKey(monitorCacheIdentifier, monitor.getFieldId());
                cacher.put(monitorCacheIdentifier, monitor.getFieldId(), new Integer(monitor.getFieldManagedNbr().toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}



package se.abalon.cache.loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import se.abalon.cache.AbalonCacheApplication;
import se.abalon.cache.AbalonCacheTemplate;
import se.abalon.cache.dao.WorkPlaceDAO;

import java.util.List;

import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;

import java.util.ArrayList;

public class WorkPlaceCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	@Autowired
	private AbalonCacheTemplate abalonCacheTemplate;

	public WorkPlaceCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		//setLoaded(false);
		try {
			addWorkPlaceCaches();
			// addNumberPkToCache();
			// addPkTerminalNumbersToCache();
		} catch (Exception e) {
			throw e;
		}
//		setLoaded(true);

	}

	private  void addWorkPlaceCaches() {
		try {
			KeyToValueEntityCacheIdentifier pkNumberCacheIdentifier = new KeyToValueEntityCacheIdentifier("WorkPlace", "ID", "WorkPlace", "NUMBER");
			KeyToValueEntityCacheIdentifier numberPkCacheIdentifier = new KeyToValueEntityCacheIdentifier("WorkPlace", "NUMBER", "WorkPlace", "ID");
			KeyToValueEntityCacheIdentifier pkTerminalNumbersCacheIdentifier = new KeyToValueEntityCacheIdentifier("WorkPlace", "ID", "Terminal", "TERMINAL_NUMBER");
			cacher.createCache(pkNumberCacheIdentifier);
			cacher.createCache(numberPkCacheIdentifier);
			cacher.createCache(pkTerminalNumbersCacheIdentifier);

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT w.id AS workplace_id, w.nummer AS workplace_number, t.terminal AS terminal_terminalnumber");
			sql.append(" FROM arbetsstalle w WITH (NOLOCK) ");
			sql.append(" LEFT JOIN terminal t WITH (NOLOCK) ON t.workplace = w.id");
			sql.append(" ORDER BY w.id");


			List<WorkPlaceDAO> workPlaceDAOs  = abalonCacheTemplate.testwork(sql.toString());

			Long workPlacePk = null;
			String lastWorkPlaceNumber = null;
			String workPlaceNumber = null;
			String terminalNumber = null;
			ArrayList<String> terminalNumbers = null;

			for (WorkPlaceDAO workPlaceDAO : workPlaceDAOs) {
				System.out.println("ID      : " + workPlaceDAO.getWorkplace_id());
				System.out.println("NUMMBER : " + workPlaceDAO.getWorkplace_number());
				System.out.println("TERMINAL: " + workPlaceDAO.getTerminal_terminalnumber());

				workPlacePk = workPlaceDAO.getWorkplace_id();
				workPlaceNumber = workPlaceDAO.getWorkplace_number();
				terminalNumber = workPlaceDAO.getTerminal_terminalnumber();

				cacher.putKeyToValue(numberPkCacheIdentifier, workPlaceNumber, workPlacePk);
				cacher.putKeyToValue(pkNumberCacheIdentifier, workPlacePk, workPlaceNumber);
				if (terminalNumber != null && (lastWorkPlaceNumber == null || !lastWorkPlaceNumber.equals(workPlaceNumber))) {
					lastWorkPlaceNumber = workPlaceNumber;
					terminalNumbers = new ArrayList<String>();
					terminalNumbers.add(terminalNumber);
					cacher.putKeyToValue(pkTerminalNumbersCacheIdentifier, workPlacePk, terminalNumbers);
				} else if (terminalNumber != null && lastWorkPlaceNumber.equals(workPlaceNumber)) {
					cacher.putKeyToValue(pkTerminalNumbersCacheIdentifier, workPlacePk, terminalNumber);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	private static void addPkTerminalNumbersToCache() {
		/*
		String keyModelName = "WorkPlace";
		String keyFieldName = "ID";
		String valueModelName = "Terminal";
		String valueFieldName = "TERMINAL_NUMBER";

		try {
			BofPersistenceManager manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			BofQuery q = (BofQuery) manager.newQuery();
			q.addModel(keyModelName, "m");
			q.addField("m", keyFieldName);
			q.addModel(valueModelName, "t");
			q.addRelation("m", "t", "terminals");
			q.addField("t", valueFieldName);
			Collection<WorkPlace> workPlaces = (Collection<WorkPlace>) q.execute();
			KeyToValueEntityCacheIdentifier cacheIdentifier = new KeyToValueEntityCacheIdentifier(keyModelName, keyFieldName, valueModelName, valueFieldName);
			cacher.createCache(cacheIdentifier);
			for (WorkPlace workPlace : workPlaces) {
				ArrayList<String> terminalNumbers = new ArrayList<String>();
				for (Model terminal : workPlace.getRelation("terminals")) {
					terminalNumbers.add(terminal.getField("TERMINAL_NUMBER").toString());
				}
				cacher.putKeyToValue(cacheIdentifier, workPlace.getFieldId(), terminalNumbers);
			}
			manager.close();
			manager = null;
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
/*
	private static void addNumberPkToCache() {
		String keyModelName = "WorkPlace";
		String keyFieldName = "NUMBER";
		String valueModelName = "WorkPlace";
		String valueFieldName = "ID";
		try {
			BofPersistenceManager manager = (BofPersistenceManager) BofPersistenceManagerFactory.create();
			BofQuery q = (BofQuery) manager.newQuery();
			q.addModel(keyModelName, "m");
			q.addField("m", keyFieldName);
			q.addField("m", valueFieldName);
			Collection<WorkPlace> workPlaces = (Collection<WorkPlace>) q.execute();
			KeyToValueEntityCacheIdentifier cacheIdentifier = new KeyToValueEntityCacheIdentifier(keyModelName, keyFieldName, valueModelName, valueFieldName);
			cacher.createCache(cacheIdentifier);
			for (WorkPlace workPlace : workPlaces) {
				cacher.putKeyToValue(cacheIdentifier, workPlace.getFieldNumber(), workPlace.getFieldId());
			}
			manager.close();
			manager = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
}

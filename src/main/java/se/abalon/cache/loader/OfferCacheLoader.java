package se.abalon.cache.loader;
import se.abalon.cache.threading.KeyToValueEntityCacheIdentifier;

import java.util.*;

public class OfferCacheLoader {//extends AbstractCacheLoader {

    private static MayflowerCacher cacher;
    private static KeyToValueEntityCacheIdentifier offerPkMemberAccountPkIdentifier = new KeyToValueEntityCacheIdentifier("Offer", "ID", "MemberAccount", "ID");

    public OfferCacheLoader() throws Exception {
        cacher = MayflowerCacher.getMayflowerCacher();
        setCacheIdentifiers();
    }

    private static void setCacheIdentifiers() {
        cacher.createCache(offerPkMemberAccountPkIdentifier);
    }


    public void load() throws Exception {
/*        setLoaded(false);
        try {
            try {
                loadOffersCache();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw e;
        }
        setLoaded(true);
*/
    }
/*
    public void updateCacheForOfferAndMemberAccount(Integer offerId, Integer memberAccountId) throws Exception {
        loadOffersCache(offerId, memberAccountId);
    }

    public void updateCacheForMemberAccount(Integer memberAccountId) throws Exception {
        loadOffersCache(null, memberAccountId);
    }

    public void updateCacheForOffer(Integer offerId) throws Exception {
        loadOffersCache(offerId, null);
    }


    private void loadCache(BofPersistenceManager manager, Set<Integer> noIndividualOfferIds, Set<Integer> individualOfferIds, Integer offerId, Integer memberAccountId) throws ModelException, ActionException {
        // If we're updating a specific offer, and the whole offer not just a specific member, clear it first (otherwise we'll add duplicate member ids to that offer)
        if (offerId != null && memberAccountId == null) {
            cacher.removeEntry(offerPkMemberAccountPkIdentifier, offerId);
        }
        // If we're updating all offers we want to empty the whole offer cache to put everything in fresh again
        else if (offerId == null && memberAccountId == null) {
            cacher.emptyCache(offerPkMemberAccountPkIdentifier);
        }

        // Hämta medlems-IDn via selection method (query, Complex query, complex selection eller data set)
        for (Integer thisOfferId : noIndividualOfferIds) {
            Set<Integer> memberAccountIds = OfferUtil.getMembers(manager, memberAccountId, thisOfferId);
            handleCache(memberAccountId, thisOfferId, memberAccountIds);
        }

        for (Integer thisOfferId : individualOfferIds) {
            Set<Integer> memberAccountIds = OfferUtil.getMembersFromIndividualOffers(manager, memberAccountId, thisOfferId);
            handleCache(memberAccountId, thisOfferId, memberAccountIds);
        }
    }



    private void handleCache(Integer memberAccountId, Integer thisOfferId, Set<Integer> memberAccountIds) {
        // If the offer cache value doesn't exist, we need to instantiate a new hashset for that offer, to put the member ids in
        if (!cacher.keyExists(offerPkMemberAccountPkIdentifier, thisOfferId)) {
            cacher.put(offerPkMemberAccountPkIdentifier, thisOfferId, new HashSet<Integer>());
        }

        // If memberAccountId is specified and the OfferUtil.getMemberAccountIds is empty it means the memberAccountId does not have this offer. It should not be in the cache.
        // Since the Offer could have been valid for the memberAccountId before it could already be in the cache. We should then remove the memberAccountId from that Offer
        if ((memberAccountId != null) && (memberAccountIds.size() == 0)) {
            Set<Integer> cachedMemberAccountIds = (Set<Integer>) cacher.getValue(offerPkMemberAccountPkIdentifier, thisOfferId);
            if (cachedMemberAccountIds.contains(memberAccountId)) {
                cachedMemberAccountIds.remove(memberAccountId);
                cacher.removeEntry(offerPkMemberAccountPkIdentifier, thisOfferId);
                cacher.put(offerPkMemberAccountPkIdentifier, thisOfferId, cachedMemberAccountIds);
            }
        } else {
            // Add all memberIds to the cache
            for (Integer thisMemberAccountId : memberAccountIds) {
                cacher.put(offerPkMemberAccountPkIdentifier, thisOfferId, thisMemberAccountId);
            }
        }
    }

    private void loadOffersCache(Integer offerId, Integer memberAccountId) {
        BofPersistenceManager manager = null;
        try {
            manager = BofPersistenceManagerFactory.create();


            // Alla publicerade erbjudanden
            // - Med individuell giltihet
            // - ELLER start och stopp och inte individuell giltighet

            Set<Integer> noIndividualOfferIds;
            Set<Integer> individualOfferIds;

            noIndividualOfferIds = OfferUtil.getValidOfferIds(manager, offerId, false);
            individualOfferIds = OfferUtil.getValidOfferIds(manager, offerId, true);

            //Load valid Offers found with offer start/stop dates
            loadCache(manager, noIndividualOfferIds, individualOfferIds, offerId, memberAccountId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    private void loadOffersCache() {
        loadOffersCache(null, null);
    }
*/
}
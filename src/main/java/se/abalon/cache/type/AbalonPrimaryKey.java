package se.abalon.cache.type;


/**
 * An implementation of PrimaryKey for the Abalon CRM system. The Abalon system uses a system generated java.lang.Long value for primary key and this
 * class is a wrapper for that value. All Model ID values (and relation references) must be of this type in an Mayflower BOF application that
 * co-exists with an Abalon CRM system. An AbalonPrimaryKey is comparable to an Abalon CRM "PON" (Physical Object Number).
 *
 * @author Fredrik Hellström [f.hellstrom@abalon.se]
 */
public class AbalonPrimaryKey implements PrimaryKey, java.io.Serializable {

    private Long key = null;

    /**
     * Constructor for AbalonPrimaryKey.
     *
     * @param key
     *            A Long that identifies a model.
     */
    public AbalonPrimaryKey(Long key) {
        this.key = key;
    }

    public AbalonPrimaryKey(long key) {
        this.key = Long.valueOf(key);
    }

    public AbalonPrimaryKey(String key) {
        this.key = Long.valueOf(key);
    }

    public Object getKey() {
        return key;
    }

    @Override
    public String toString() {
        if (key != null) {
            return key.toString();
        }

        return "";
    }

    @Override
    public boolean equals(Object pk) {
        AbalonPrimaryKey apk;

        if (pk == null) {
            return false;
        }

        try {
            apk = (AbalonPrimaryKey) pk;
        } catch (ClassCastException e) {
            return false;
        }

        if ((apk.key != null) && (key != null)) {
            if (key.equals(apk.key)) {
                return true;
            }
        }

        if ((apk.key == null) && (key == null)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Takes an object and checks the instance and returns a AbalonPrimaryKey based on the instance.
     *
     * @param object
     * @return AbalonPrimaryKey
     * @throws OperationException
     */
    public static AbalonPrimaryKey fromObject(Object object) {

        if (object instanceof String) {
            return new AbalonPrimaryKey((String) object);
        } else if (object instanceof Long) {
            return new AbalonPrimaryKey((Long) object);
        } else if (object instanceof Integer) {
            return new AbalonPrimaryKey((Integer) object);
        } else if (object instanceof AbalonPrimaryKey) {
            return (AbalonPrimaryKey) object;
        } else {
            throw new IllegalArgumentException(object.getClass().toString());
        }
    }
}

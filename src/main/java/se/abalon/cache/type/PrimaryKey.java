package se.abalon.cache.type;

/**
 * Interface for different primary key implementations.
 * The type of the primary key may vary between different systems. If a
 * Mayflower BOF application spans over tables in several different systems
 * several PrimaryKey implementations must be provided.
 * @author Fredrik Hellström [f.hellstrom@abalon.se]
 */
public interface PrimaryKey {
    public String toString();
    public Object getKey();
    public boolean equals(Object pk);

}


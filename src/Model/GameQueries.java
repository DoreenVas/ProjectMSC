package Model;

import java.sql.Statement;

/**
 * A class holding all of the artist table queries, also in charge of executing them.
 */
class GameQueries {
    // members
    private static GameQueries ourInstance = new GameQueries();
    private static Statement myStatement;
    private static String[] allArtistFields = {/*"artist.artist_id", */"artist.artist_name", "artist.hotness"};

    /****
     * Constructor of singleton
     * @param statement a statement
     * @return his instance of artistQueries
     */
    static GameQueries getInstance(Statement statement) {
        myStatement = statement;
        return ourInstance;
    }

}
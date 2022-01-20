package fr.bookin.bookin_back.database.splitters;

import java.util.Arrays;

/**
 * This is the default text splitter, it's used during tests
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DefaultSplitter implements ISplitter {
    @Override
    public String[] split(String text) {
        String[] res = text.split("[^a-zA-Z]+");
        return Arrays.stream(res).filter(x -> x.length() > 2).toArray(String[]::new);
    }
}

package fr.bookin.bookin_back.database.splitters;

import java.util.Arrays;

/**
 * This class is a splitter for the frenches texts
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class FrenchSplitter implements ISplitter {
    /** @see ISplitter#split(String) */
    @Override
    public String[] split(String text) {
        text = text.replaceAll("[\\[\\]\"_=+*/.|{}()~#&%<>`]", " ");
        return Arrays.stream(text.split(" ")).filter(x -> x.length() > 2).toArray(String[]::new);
    }
}

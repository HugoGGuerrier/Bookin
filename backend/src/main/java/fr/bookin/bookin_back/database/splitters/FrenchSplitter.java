package fr.bookin.bookin_back.database.splitters;

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
        return text.split("\\s+");
    }
}

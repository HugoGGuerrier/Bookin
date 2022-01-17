package fr.bookin.bookin_back.database.splitters;

public class DefaultSplitter implements ISplitter {
    @Override
    public String[] split(String text) {
        text = text.replaceAll("[\\[\\]\"'_=+*/.|{}()~#&%<>`]", " ");
        return text.split("\\s+");
    }
}

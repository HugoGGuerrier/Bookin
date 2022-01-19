package fr.bookin.bookin_back.database.splitters;

import java.util.Arrays;

public class DefaultSplitter implements ISplitter {
    @Override
    public String[] split(String text) {
        String[] res = text.split("[^a-zA-Z]+");
        return Arrays.stream(res).filter(x -> x.length() > 2).toArray(String[]::new);
    }
}

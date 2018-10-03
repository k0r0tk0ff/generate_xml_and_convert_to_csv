package ru.k0r0tk0ff.utils;

import ru.k0r0tk0ff.domain.RawEntry;
import java.util.ArrayList;
import java.util.List;

public class DataCreator {
    public List<RawEntry> getDataForInsertToDb(int maxCountOfRawEntry) {
        ArrayList<RawEntry> dataForInsertToDb = new ArrayList<>(150000);
        for (int i=0; i <= maxCountOfRawEntry; i++) {
            RawEntry rawEntry = new RawEntry();
            rawEntry.setName("AWS");
            rawEntry.setCode(1000000+i);
            rawEntry.setGuid(String.format("asdf-yhju-%s-rffgh",i));
            rawEntry.setUserName("Bobby");
            dataForInsertToDb.add(rawEntry);
        }
        return dataForInsertToDb;
    }

    /*  private String getRandomString() {
        byte[] array = new byte[8];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }*/
}

package ru.k0r0tk0ff.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.k0r0tk0ff.domain.RawEntry;
import ru.k0r0tk0ff.storage.DbDataSource;
import ru.k0r0tk0ff.storage.StorageException;
import ru.k0r0tk0ff.utils.DataCreator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbDao {
    private static final Logger LOGGER  = LoggerFactory.getLogger(DbDao.class);

    private DbDataSource dbStorage;

    public DbDao(DbDataSource dbStorage) {
        this.dbStorage = dbStorage;
    }

    public void doCreateAndFillDb(int maxCountOfEntries) {
        try {
            dbStorage.initializeResources();
            doCreateDb();
            doInsertDataToDb(maxCountOfEntries);
        } catch (StorageException e) {
            LOGGER.error("DbDao Error!", e);
        }
    }

    private void doCreateDb() throws StorageException {
        try (Statement statement = dbStorage.getConnection().createStatement()) {
            String createDbTable = "CREATE TABLE IF NOT EXISTS ARTICLE " +
                    "(ID_ART INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                    "NAME VARCHAR(35), " +
                    "CODE INTEGER (10), " +
                    "GUID VARCHAR(40) NOT NULL, " +
                    "USERNAME VARCHAR(10))";
            statement.execute(createDbTable);

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Create table success");
            }

            String clearTableInDb = "TRUNCATE TABLE ARTICLE";
            statement.execute(clearTableInDb);

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Truncate table success");
            }
            statement.close();
        } catch (SQLException e) {
            throw new StorageException("Cannot create db!", e);
        }
    }

    private void doInsertDataToDb(int maxCountOfRawEntry) throws StorageException {
        DataCreator dataCreator = new DataCreator();
        List<RawEntry> dataForInsertToDb = dataCreator.getDataForInsertToDb(maxCountOfRawEntry);

        final String queryForPutData = "INSERT into ARTICLE (NAME, CODE, GUID, USERNAME) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement =
                     dbStorage.getConnection().prepareStatement(queryForPutData)) {
            for (int i = 1; i < maxCountOfRawEntry + 1; i++) {
                preparedStatement.setString(1, dataForInsertToDb.get(i).getName());
                preparedStatement.setInt(2, dataForInsertToDb.get(i).getCode());
                preparedStatement.setString(3, dataForInsertToDb.get(i).getGuid());
                preparedStatement.setString(4, dataForInsertToDb.get(i).getUserName());
                preparedStatement.addBatch();
                if (i % 5000 == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            LOGGER.debug("Insert data success");
        } catch (SQLException e) {
            throw new StorageException("Cannot insert data to DB !", e);
        }
    }

    public List<RawEntry> getDataFromDb() throws StorageException {
        List<RawEntry> data = new ArrayList<>();
        String sqlQueryForGetData = "SELECT ID_ART, NAME, CODE, GUID, USERNAME FROM ARTICLE";
        try (Statement statement =
                     dbStorage.getConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sqlQueryForGetData)) {
                while (resultSet.next()) {
                    RawEntry rawEntry = new RawEntry();
                    rawEntry.setName(resultSet.getString("NAME"));
                    rawEntry.setId_art(resultSet.getInt("ID_ART"));
                    rawEntry.setCode(resultSet.getInt("CODE"));
                    rawEntry.setGuid(resultSet.getString("GUID"));
                    rawEntry.setUserName(resultSet.getString("USERNAME"));
                    data.add(rawEntry);
                }
            }
        } catch (SQLException e) {
            throw new StorageException("Cannot get data from DB !", e);
        }
        return data;
    }
}




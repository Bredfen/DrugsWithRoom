package Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import Model.Drug;

@Database(entities = {Drug.class}, version = 1)
public abstract class DrugsAppDatabase extends RoomDatabase {

    public abstract DrugDAO getDrugDAO();

}

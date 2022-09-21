package Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Model.Drug;

@Dao
public interface DrugDAO {

    @Insert
    public long addDrug(Drug drug);

    @Update
    public void updateDrug(Drug drug);

    @Delete
    public void deleteDrug(Drug drug);

    @Query("select * from drugs")
    public List<Drug> getAllDrugs();

    @Query("select * from drugs where drug_id ==:drugId")
    public Drug getDrug(long drugId);

}

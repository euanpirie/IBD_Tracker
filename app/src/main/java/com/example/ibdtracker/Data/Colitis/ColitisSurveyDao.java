package com.example.ibdtracker.Data.Colitis;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;

import java.util.List;

@Dao
public interface ColitisSurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ColitisSurveyResponse response); //insert response into room db

    @Update
    void update(ColitisSurveyResponse response); //update existing response in room db

    @Query("DELETE FROM ColitisSurveyResponse WHERE date LIKE :date")
    void delete(String date); //delete response in db

    @Query("SELECT * from ColitisSurveyResponse")
    List<ColitisSurveyResponse> getAllResponses(); //get all entries in db

    @Query("SELECT COUNT(*) from ColitisSurveyResponse WHERE date LIKE :date")
    int checkIfExists(String date); //count all occurences of a response

    @Query("DELETE FROM ColitisSurveyResponse")
    void deleteAll(); //delete all entries in the db

    @Query("SELECT * FROM ColitisSurveyResponse WHERE date LIKE :date")
    ColitisSurveyResponse getFromDate(String date); //get the survey with the matching date

    @Query("SELECT * FROM ColitisSurveyResponse ORDER BY date ASC")
    List<ColitisSurveyResponse> getAllResponsesSorted();
}

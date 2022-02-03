package com.example.ibdtracker.Data.Crohns;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CrohnsSurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CrohnsSurveyResponse response); //insert response into room db

    @Update
    void update(CrohnsSurveyResponse response); //update existing response in room db

    @Query("DELETE FROM CrohnsSurveyResponse WHERE date LIKE :date")
    void delete(String date); //delete response in db

    @Query("SELECT * from CrohnsSurveyResponse")
    List<CrohnsSurveyResponse> getAllResponses(); //get all entries in db

    @Query("SELECT COUNT(*) from CrohnsSurveyResponse WHERE date LIKE :date")
    int checkIfExists(String date); //count all occurences of a response

    @Query("DELETE FROM CrohnsSurveyResponse")
    void deleteAll(); //delete all entries in the db

    @Query("SELECT * FROM CrohnsSurveyResponse WHERE date LIKE :date")
    CrohnsSurveyResponse getFromDate(String date); //get the survey with the matching date

    @Query("SELECT * FROM CrohnsSurveyResponse ORDER BY date ASC")
    List<CrohnsSurveyResponse> getAllResponsesSorted();

}

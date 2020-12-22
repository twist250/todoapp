package com.codinginflow.mvvmtodo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(
        searchQuery: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean
    ): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTaskSortedByDateCreated(searchQuery, hideCompleted)
            SortOrder.BY_NAME -> getTaskSortedByName(searchQuery, hideCompleted)
        }

    @Query("select * from task_table where (completed != :hideCompleted or completed = 0) and name like '%' || :searchQuery || '%' order by important desc, name")
    fun getTaskSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("select * from task_table where (completed != :hideCompleted or completed = 0) and name like '%' || :searchQuery || '%' order by important desc, created")
    fun getTaskSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}
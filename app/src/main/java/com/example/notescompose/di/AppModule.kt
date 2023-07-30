package com.example.notescompose.di

import android.app.Application
import androidx.room.Room
import com.example.notescompose.feature_note.data.repository.RepositoryImpl
import com.example.notescompose.feature_note.data.source.NoteDatabase
import com.example.notescompose.feature_note.domain.repository.NoteRepository
import com.example.notescompose.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun  provideNoteDatabase(app: Application): NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(database: NoteDatabase): NoteRepository {
        return RepositoryImpl(database.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNoteById = GetNoteById(repository)
        )
    }
}
package com.example.mynotes.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.core.data.Note
import com.example.core.repository.NoteRepository
import com.example.core.usecase.AddNote
import com.example.core.usecase.GetAllNotes
import com.example.core.usecase.GetNote
import com.example.core.usecase.RemoveNotes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application):AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val repository = NoteRepository(RoomNoteDataSource(application))

    val useCases = UseCases(
        AddNote(repository),
        GetAllNotes(repository),
        GetNote(repository),
        RemoveNotes(repository)
    )

    val saved = MutableLiveData<Boolean>()
    val currentNote = MutableLiveData<Note?>()

    fun saveNote(note:Note){
        coroutineScope.launch {
            useCases.addNote(note)
            saved.postValue(true)
        }
    }
    fun getNote(id:Long){
        coroutineScope.launch {
            val note:Note? =useCases.getNote(id)
            currentNote.postValue(note)
        }
    }

    fun deleteNote(note: Note){
        coroutineScope.launch {
            useCases.removeNotes(note)
            saved.postValue(true)
        }
    }}
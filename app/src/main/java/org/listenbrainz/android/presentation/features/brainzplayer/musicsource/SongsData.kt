package org.listenbrainz.android.presentation.features.brainzplayer.musicsource

import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import org.listenbrainz.android.App.Companion.context
import org.listenbrainz.android.data.sources.brainzplayer.Song

class SongData {
    fun fetchSongs(): List<Song> {
        val songs = mutableListOf<Song>()
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        val songProjection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.YEAR,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.TRACK,
            MediaStore.Audio.AudioColumns.DISC_NUMBER,  // @RequiresApi(Build.VERSION_CODES.R) (or SDK 30)
            MediaStore.Audio.AudioColumns.ARTIST_ID,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED
        )
        val sortOrder = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"
        val isMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val songQuery = context?.contentResolver?.query(
            collection,
            songProjection,
            isMusic,
            null,
            sortOrder
        )
        songQuery?.use { cursor ->
            val id = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val name = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val album = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)
            val albumId = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)
            val yearInt = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)
            val durationLong = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val track = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK)
            val discNo = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISC_NUMBER)
            val artistId = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)
            val dateModifiedLong =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED)
            while (cursor.moveToNext()) {
                val songId = cursor.getLong(id)
                val albumID = cursor.getLong(albumId)
                val songName = cursor.getString(name)
                val artistName = cursor.getString(artist)
                val albumName = cursor.getString(album) ?: ""
                val albumArt = "content://media/external/audio/albumart/$albumID"
                val year = cursor.getInt(yearInt)
                val duration = cursor.getLong(durationLong)
                val trackNumber = cursor.getInt(track)
                val discNumber = cursor.getLong(discNo)
                val artistID = cursor.getLong(artistId)
                val dateModified = cursor.getLong(dateModifiedLong)
                val contentUri = ContentUris.withAppendedId(
                    collection,
                    songId
                ).toString()
                songs += Song(
                    mediaID = songId,
                    title = songName,
                    artist = artistName,
                    albumID = albumID,
                    album = albumName,
                    uri = contentUri,
                    albumArt = albumArt,
                    year = year,
                    duration = duration,
                    trackNumber = trackNumber,
                    discNumber = discNumber,
                    artistId = artistID,
                    dateModified = dateModified
                )
            }
        }
        return songs
    }
}
package com.maubis.scarlet.base.core.database

import com.maubis.scarlet.base.core.database.room.tag.Tag
import com.maubis.scarlet.base.core.database.room.tag.TagDao
import java.util.concurrent.ConcurrentHashMap

abstract class TagsProvider {

  val tags = ConcurrentHashMap<String, Tag>()

  fun notifyInsertTag(tag: Tag) {
    maybeLoadFromDB()
    tags[tag.uuid] = tag
  }

  fun notifyDelete(tag: Tag) {
    maybeLoadFromDB()
    tags.remove(tag.uuid)
  }

  fun getCount(): Int {
    maybeLoadFromDB()
    return tags.size
  }

  fun getAll(): List<Tag> {
    maybeLoadFromDB()
    return tags.values.toList()
  }

  fun getByID(uid: Int): Tag? {
    maybeLoadFromDB()
    return tags.values.firstOrNull { it.uid == uid }
  }

  fun getByUUID(uuid: String): Tag? {
    maybeLoadFromDB()
    return tags[uuid]
  }

  fun getByTitle(title: String): Tag? {
    maybeLoadFromDB()
    return tags.values.firstOrNull { it.title == title }
  }

  fun search(string: String): List<Tag> {
    maybeLoadFromDB()
    return tags.values
        .filter { it.title.contains(string, true) }
  }

  @Synchronized
  fun maybeLoadFromDB() {
    if (tags.isNotEmpty()) {
      return
    }
    database().all.forEach {
      tags[it.uuid] = it
    }
  }

  fun clear() {
    tags.clear()
  }

  abstract fun database(): TagDao
}
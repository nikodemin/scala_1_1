package ru.otus.sc.dao.impl

import ru.otus.sc.dao.GreetingDao

class GreetingDaoImpl extends GreetingDao {
  override val greetingPrefix: String = "Hi"
  override val greetingPostfix: String = "!"
}

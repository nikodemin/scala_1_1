package ru.otus.sc.service.impl

import ru.otus.sc.service.EchoService

class EchoServiceImpl extends EchoService {
  override def echo(input: String): String = input
}

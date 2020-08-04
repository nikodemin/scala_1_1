package ru.otus.sc.service.impl

import ru.otus.sc.dao.GreetingDao
import ru.otus.sc.model.dto.{GreetRequest, GreetResponse}
import ru.otus.sc.service.GreetingService

class GreetingServiceImpl(dao: GreetingDao) extends GreetingService {
  override def greet(request: GreetRequest): GreetResponse =
    if (request.isHuman)
      GreetResponse(s"${dao.greetingPrefix} ${request.name} ${dao.greetingPostfix}")
    else GreetResponse("AAAAAAAAAA!!!!!!")
}

package ru.otus.sc.service

import ru.otus.sc.model.dto.{GreetRequest, GreetResponse}

/**
 * Service to greet user
 */
trait GreetingService {
  /**
   * Greet user by name. Panic if user is not a human
   *
   * @param request GreetRequest
   * @return GreetResponse
   */
  def greet(request: GreetRequest): GreetResponse
}

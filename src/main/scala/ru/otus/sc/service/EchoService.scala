package ru.otus.sc.service

/**
 * Service for echo operation
 */
trait EchoService {
  /**
   * Sends the request back in response
   *
   * @param input String
   * @return String
   */
  def echo(input: String): String
}

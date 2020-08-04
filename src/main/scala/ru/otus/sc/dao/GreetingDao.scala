package ru.otus.sc.dao

trait GreetingDao {
  /**
   * Get greeting prefix
   *
   * @return String
   */
  def greetingPrefix: String

  /**
   * Get greeting postfix
   *
   * @return String
   */
  def greetingPostfix: String
}

package ru.otus.sc.route.util

sealed trait HttpException extends Exception

case class NotFound(msg: String = "Entity not found") extends HttpException

case class InternalServerError(msg: String = "Internal server error") extends HttpException

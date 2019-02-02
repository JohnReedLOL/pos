/*
* Copyright 2018 John Michael Reed
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.github.johnreedlol.logging

import com.github.johnreedlol.implicits.Pos
@SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter", "org.wartremover.warts.Overloading"))
trait TraceLogging {
  private final val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger("")

  final def getUnderlyingLogger(): org.slf4j.Logger = {
    logger
  }

  // trace

  final def trace(message: String)(implicit position: Pos): Unit = {
    logger.trace(message + position.text)
  }

  final def trace(message: String, t: Throwable)(implicit position: Pos): Unit = {
    logger.trace(message + position.text, t)
  }

  final def trace(message: String, args: AnyRef*)(implicit position: Pos): Unit = {
    logger.trace(message + position.text, args)
  }

  // debug

  final def debug(message: String)(implicit position: Pos): Unit = {
    logger.debug(message + position.text)
  }

  final def debug(message: String, t: Throwable)(implicit position: Pos): Unit = {
    logger.debug(message + position.text, t)
  }

  final def debug(message: String, args: AnyRef*)(implicit position: Pos): Unit = {
    logger.debug(message + position.text, args)
  }

  // info

  final def info(message: String)(implicit position: Pos): Unit = {
    logger.info(message + position.text)
  }

  final def info(message: String, t: Throwable)(implicit position: Pos): Unit = {
    logger.info(message + position.text, t)
  }

  final def info(message: String, args: AnyRef*)(implicit position: Pos): Unit = {
    logger.info(message + position.text, args)
  }

  // warn

  final def warn(message: String)(implicit position: Pos): Unit = {
    logger.warn(message + position.text)
  }

  final def warn(message: String, t: Throwable)(implicit position: Pos): Unit = {
    logger.warn(message + position.text, t)
  }

  final def warn(message: String, args: AnyRef*)(implicit position: Pos): Unit = {
    logger.warn(message + position.text, args)
  }

  // error

  final def error(message: String)(implicit position: Pos): Unit = {
    logger.error(message + position.text)
  }

  final def error(message: String, t: Throwable)(implicit position: Pos): Unit = {
    logger.error(message + position.text, t)
  }

  final def error(message: String, args: AnyRef*)(implicit position: Pos): Unit = {
    logger.error(message + position.text, args)
  }

}

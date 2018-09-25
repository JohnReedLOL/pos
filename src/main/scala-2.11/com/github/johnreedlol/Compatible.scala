package com.github.johnreedlol

protected[johnreedlol] object Compatible {
  type Context = scala.reflect.macros.blackbox.Context

  def enclosingOwner(c: Context): c.Symbol = c.internal.enclosingOwner
}
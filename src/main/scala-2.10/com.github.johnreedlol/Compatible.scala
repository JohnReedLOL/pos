package com.github.johnreedlol

protected[johnreedlol] object Compatible {
  type Context = scala.reflect.macros.Context

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def enclosingOwner(c: Context): c.Symbol = {
    c.asInstanceOf[scala.reflect.macros.runtime.Context]
      .callsiteTyper
      .context
      .owner
      .asInstanceOf[c.Symbol]
  }
}
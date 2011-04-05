package com.intellij.flex.uiDesigner.mxml;

import com.intellij.flex.uiDesigner.io.PrimitiveAmfOutputStream;

class ClassStringValueWriter extends AbstractPrimitiveValueWriter {
  private final String className;

  public ClassStringValueWriter(String className) {
    this.className = className;
  }

  @Override
  protected void write(PrimitiveAmfOutputStream out, BaseWriter writer) {
    writer.writeClass(className);
  }
}

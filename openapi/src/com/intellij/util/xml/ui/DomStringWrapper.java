/*
 * Copyright (c) 2000-2006 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.util.xml.ui;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericDomValue;

import java.lang.reflect.InvocationTargetException;

/**
 * @author peter
 */
public class DomStringWrapper implements DomWrapper<String>{
  private final GenericDomValue myDomElement;

  public DomStringWrapper(final GenericDomValue domElement) {
    myDomElement = domElement;
  }

  public DomElement getDomElement() {
    return myDomElement;
  }

  public void setValue(final String value) throws IllegalAccessException, InvocationTargetException {
    myDomElement.setStringValue(value);
  }

  public String getValue() throws IllegalAccessException, InvocationTargetException {
    return myDomElement.isValid() ? myDomElement.getStringValue() : null;
  }

}

/**
 *  Copyright (c) 2011-2016 Terracotta, Inc.
 *  Copyright (c) 2011-2016 Oracle and/or its affiliates.
 *
 *  All rights reserved. Use is subject to license terms.
 */
package org.jsr107.tck.testutil;

import java.util.Set;

/**
 * For the TCK we need to have an exclude list of bad tests so that disabling tests
 * can be done without changing code.
 * <p>
 * This class creates a rule for the class provided
 * </p>
 * The exclude list is created by {@link ExcludeList} by creating a file in the root of your classpath called
 * "ExcludeList". There is an example in the testRI module for testing the RI.
 *
 * @author Yannis Cosmadopoulos
 * @since 1.0
 */
public class ExcludeListExcluder extends AbstractTestExcluder {


  private final Set<String> excludes;

  /**
   * Constructor for ExcludeListExcluder.
   * Uses the supplied class name and {@link ExcludeList#getExcludes(String)} to
   * determine the methods to be excluded.
   *
   * @param c the class for which tests should be excluded
   */
  public ExcludeListExcluder(Class c) {
    excludes = ExcludeList.INSTANCE.getExcludes(c.getName());
  }


  protected boolean isExcluded(String methodName) {
    return excludes != null && excludes.contains(methodName);
  }

}

/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.osgi.authentication.ri.tests;

import org.everit.authentication.context.AuthenticationContext;
import org.everit.authentication.context.AuthenticationPropagator;
import org.everit.authentication.context.ri.AuthenticationContextImpl;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.props.PropertyManager;
import org.junit.Assert;
import org.junit.Test;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Test for Authentication Context Component.
 */
@Component(componentId = "AuthenticationTest", configurationPolicy = ConfigurationPolicy.OPTIONAL)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE,
        defaultValue = "junit4"),
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TEST_ID,
        defaultValue = "AuthenticationTest") })
@Service(value = AuthenticationContextComponentTest.class)
public class AuthenticationContextComponentTest {

  private static final String MESSAGE =
      "the exception that tests the finally block in the runAs() method";

  private AuthenticationContext authenticationContext;

  private AuthenticationPropagator authenticationPropagator;

  private PropertyManager propertyManager;

  @ServiceRef(defaultValue = "")
  public void setAuthenticationContext(final AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;
  }

  @ServiceRef(defaultValue = "")
  public void setAuthenticationPropagator(final AuthenticationPropagator authenticationPropagator) {
    this.authenticationPropagator = authenticationPropagator;
  }

  @ServiceRef(defaultValue = "")
  public void setPropertyManager(final PropertyManager propertyManager) {
    this.propertyManager = propertyManager;
  }

  @Test
  public void testArgumentValidations() {
    try {
      authenticationPropagator.runAs(0, null);
      Assert.fail();
    } catch (NullPointerException e) {
      Assert.assertEquals("authenticatedAction cannot be null", e.getMessage());
    }
  }

  @Test
  public void testComplex() {
    String defaultResourceIdString =
        propertyManager.getProperty(AuthenticationContextImpl.PROP_DEFAULT_RESOURCE_ID);
    long defaultResourceId = Long.parseLong(defaultResourceIdString);

    Assert.assertEquals(defaultResourceId, authenticationContext.getCurrentResourceId());

    authenticationPropagator.runAs(1, () -> {
      Assert.assertEquals(1, authenticationContext.getCurrentResourceId());
      return null;
    });
    Assert.assertEquals(defaultResourceId, authenticationContext.getCurrentResourceId());

    try {
      authenticationPropagator.runAs(1, () -> {
        throw new NullPointerException(MESSAGE);
      });
      Assert.fail();
    } catch (NullPointerException e) {
      Assert.assertEquals(MESSAGE, e.getMessage());
    }
    Assert.assertEquals(defaultResourceId, authenticationContext.getCurrentResourceId());

    authenticationPropagator.runAs(1, () -> {
      Assert.assertEquals(1, authenticationContext.getCurrentResourceId());
      authenticationPropagator.runAs(2, () -> {
        Assert.assertEquals(2, authenticationContext.getCurrentResourceId());
        return null;
      });
      Assert.assertEquals(1, authenticationContext.getCurrentResourceId());
      return null;
    });
    Assert.assertEquals(defaultResourceId, authenticationContext.getCurrentResourceId());
  }

}

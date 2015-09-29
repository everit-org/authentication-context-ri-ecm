/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
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
package org.everit.authentication.context.ri.ecm;

/**
 * Constants of the
 * {@link org.everit.authentication.context.ri.ecm.internal.AuthenticationContextComponent}.
 */
public final class AuthenticationContextConstants {

  /**
   * The property name of the OSGi filter expression defining which
   * {@link org.everit.osgi.props.PropertyManager} should be used by
   * {@link org.everit.authentication.context.ri.ecm.internal.AuthenticationContextComponent}.
   */
  public static final String ATTR_PROPERTY_MANAGER_TARGET = "propertyManager.target";

  /**
   * The property name of the OSGi filter expression defining which
   * {@link org.everit.osgi.resource.ResourceService} should be used by
   * {@link org.everit.authentication.context.ri.ecm.internal.AuthenticationContextComponent}.
   */
  public static final String ATTR_RESOURCE_SERVICE_TARGET = "resourceService.target";

  public static final String DEFAULT_SERVICE_DESCRIPTION =
      "Default Authentication Context Component";

  /**
   * The service factory PID of the authentication component.
   */
  public static final String SERVICE_FACTORYPID_AUTHENTICATION_CONTEXT =
      "org.everit.authentication.context.ri.ecm.AuthenticationContext";

  private AuthenticationContextConstants() {
  }

}

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
package org.everit.authentication.context.ri.ecm.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.everit.authentication.context.AuthenticationContext;
import org.everit.authentication.context.AuthenticationPropagator;
import org.everit.authentication.context.ri.AuthenticationContextImpl;
import org.everit.authentication.context.ri.ecm.AuthenticationContextConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.props.PropertyManager;
import org.everit.resource.ResourceService;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * The reference implementation of the {@link AuthenticationContext} and
 * {@link AuthenticationPropagator} interfaces.
 */
@Component(componentId = AuthenticationContextConstants.SERVICE_FACTORYPID_AUTHENTICATION_CONTEXT,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit Authentication Context RI",
    description = "Component for executing authenticated tasks and querying the actual owner "
        + "of the running task.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = AuthenticationContextConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = AuthenticationContextComponent.P01_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualService({ AuthenticationContext.class, AuthenticationPropagator.class })
public class AuthenticationContextComponent {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_PROPERTY_MANAGER = 2;

  public static final int P03_RESOURCE_SERVICE = 3;

  /**
   * The {@link PropertyManager} used to load/store the value of the
   * {@link AuthenticationContext#PROP_DEFAULT_RESOURCE_ID}.
   */
  private PropertyManager propertyManager;

  /**
   * The {@link ResourceService} used to initialize the resource of the default subject.
   */
  private ResourceService resourceService;

  private ServiceRegistration<?> serviceRegistration;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<AuthenticationContextComponent> componentContext) {
    AuthenticationContextImpl authenticationContextImpl =
        new AuthenticationContextImpl(propertyManager, resourceService);

    Dictionary<String, Object> serviceProperties =
        new Hashtable<>(componentContext.getProperties());
    serviceRegistration =
        componentContext.registerService(new String[] { AuthenticationContext.class.getName(),
            AuthenticationPropagator.class.getName() },
            authenticationContextImpl, serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  @ServiceRef(attributeId = AuthenticationContextConstants.ATTR_PROPERTY_MANAGER_TARGET,
      defaultValue = "", attributePriority = P02_PROPERTY_MANAGER,
      label = "Property Manager OSGi filter",
      description = "OSGi Service filter expression for PropertyManager instance.")
  public void setPropertyManager(final PropertyManager propertyManager) {
    this.propertyManager = propertyManager;
  }

  @ServiceRef(attributeId = AuthenticationContextConstants.ATTR_RESOURCE_SERVICE_TARGET,
      defaultValue = "", attributePriority = P03_RESOURCE_SERVICE,
      label = "Resource Service OSGi filter",
      description = "OSGi Service filter expression for ResourceService instance.")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

}

authentication-context-ri-ecm
=============================

ECM based components for [authentication-context-ri][1].

The module contains one ECM component. The component can be
instantiated multiple times via Configuration Admin. The component registers
two OSGi services: the [AuthenticationPropagator][5] and the
[AuthenticationContext][6] provided by the [authentication-context-api][2].

[1]: https://github.com/everit-org/authentication-context-ri
[2]: https://github.com/everit-org/authentication-context-api
[5]: http://attilakissit.wordpress.com/2014/07/09/everit-authentication/#authentication_propagator
[6]: http://attilakissit.wordpress.com/2014/07/09/everit-authentication/#authentication_context

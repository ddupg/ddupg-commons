package io.ddupg.garlic.rpc.server;

import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.common.collect.Maps;
import io.ddupg.garlic.shaded.com.google.protobuf.Service;

import java.util.Map;

public class Services {

  private Map<String, Service> services = Maps.newConcurrentMap();

  public void register(Service service) {
    services.put(service.getDescriptorForType().getFullName(), service);
  }

  public boolean unregister(Service service) {
    return services.remove(service.getDescriptorForType().getFullName(), service);
  }

  public Service get(String name) {
    Service service = services.get(name);
    Preconditions.checkArgument(service != null, "not found service %s", name);
    return service;
  }
}

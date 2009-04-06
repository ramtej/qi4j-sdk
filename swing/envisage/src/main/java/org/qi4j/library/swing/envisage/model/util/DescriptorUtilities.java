/*  Copyright 2009 Tonny Kohar.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied.
*
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.qi4j.library.swing.envisage.model.util;

import org.qi4j.library.swing.envisage.model.descriptor.*;
import org.qi4j.library.swing.envisage.util.TableRow;

import java.util.List;

/**
 * Collection of Desciptor Utilities
 *
 * @author Tonny Kohar (tonny.kohar@gmail.com)
 */
public class DescriptorUtilities {
    private DescriptorUtilities() {
        throw new Error("This is a utility class for static methods");
    }

    /**
     * Return Descriptor Detail
     *
     * @param descriptor ServiceDetailDescriptor
     * @return list of Descritpor Detail (never return null)
     */
    public static List<CompositeMethodDetailDescriptor> findMethod(CompositeDetailDescriptor descriptor) {
        return new MethodFinder().findMethod(descriptor);
    }

    /**
     * Return Descriptor Detail
     *
     * @param descriptor ServiceDetailDescriptor
     * @return list of Descritpor Detail (never return null)
     */
    public static List<CompositeMethodDetailDescriptor> findState(CompositeDetailDescriptor descriptor) {
        return new StateFinder().findState(descriptor);
    }

    /**
     * Return Descriptor Detail
     *
     * @param descriptor ServiceDetailDescriptor
     * @return Descritpor Detail or null
     */
    public static Object findServiceConfiguration(ServiceDetailDescriptor descriptor) {
        return new ServiceConfigurationFinder().findConfigurationDescriptor(descriptor);
    }

    /**
     * Return list of Descriptor Detail for particular Service Usage
     *
     * @param descriptor ServiceDetailDescriptor
     * @return list of service usage (never return null)
     */
    public static List<TableRow> findServiceUsage(ServiceDetailDescriptor descriptor) {
        return new ServiceUsageFinder().findServiceUsage(descriptor);
    }

    /**
     * Return list of Descriptor Detail for All service interfaces which are visible for the module
     *
     * @param descriptor ModuleDetailDescriptor
     * @return list of ServiceDetailDescriptor (never return null)
     */
    public static List<ServiceDetailDescriptor> findModuleAPI(ModuleDetailDescriptor descriptor) {
        return new APIFinder().findModuleAPI(descriptor);
    }

    /**
     * Return list of Descriptor Detail for All service interfaces which are visible for the layer
     *
     * @param descriptor ModuleDetailDescriptor
     * @return list of ServiceDetailDescriptor (never return null)
     */
    public static List<ServiceDetailDescriptor> findLayerAPI(LayerDetailDescriptor descriptor) {
        return new APIFinder().findLayerAPI(descriptor);
    }


    /**
     * Return list of Descriptor Detail for all service dependencies which
     * are not satisfied from within the module 
     *
     * @param descriptor ModuleDetailDescriptor
     * @return list of ServiceDetailDescriptor (never return null)
     */
    public static List<ServiceDetailDescriptor> findModuleSPI(ModuleDetailDescriptor descriptor) {
        return new SPIFinder().findModuleSPI(descriptor);
    }

    /**
     * Return list of Descriptor Detail for all service dependencies which
     * are not satisfied from within the layer 
     *
     * @param descriptor ModuleDetailDescriptor
     * @return list of ServiceDetailDescriptor (never return null)
     */
    public static List<ServiceDetailDescriptor> findLayerSPI(LayerDetailDescriptor descriptor) {
        return new SPIFinder().findLayerSPI(descriptor);
    }
}

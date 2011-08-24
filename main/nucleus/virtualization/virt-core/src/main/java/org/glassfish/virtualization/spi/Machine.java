/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.virtualization.spi;

import org.glassfish.virtualization.config.MachineConfig;
import org.glassfish.virtualization.config.Template;
import org.glassfish.virtualization.config.VirtUser;
import org.glassfish.virtualization.os.FileOperations;
import org.glassfish.virtualization.runtime.VirtualCluster;
import org.glassfish.virtualization.util.EventSource;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Represents a machine
 * @author Jerome Dochez
 */
public interface Machine {

    public enum State { SUSPENDING, SUSPENDED, RESUMING, READY}

    MachineConfig getConfig();

    String getName();

    String getIpAddress();

    PhysicalServerPool getServerPool();

    State getState();

    VirtUser getUser();

    FileOperations getFileOperations();

    boolean isUp();

    void sleep() throws IOException, InterruptedException;

    Collection<? extends VirtualMachine> getVMs() throws VirtException;

    StoragePool addStoragePool(String name, long capacity) throws VirtException;

    Map<String, ? extends StoragePool> getStoragePools() throws VirtException;

    VirtualMachine byName(String name) throws VirtException;

    /**
     * Allocate a new Virtual Machine on this machine.
     *
     * @param template the template to use for the virtual machine
     * @param cluster the virtual cluster in which  the virtual machine must be added
     * using the {@link VirtualCluster#add(TemplateInstance, VirtualMachine)}  method
     * @param source the event notification mechanism
     * @return a {@link ListenableFuture} to obtain events and the virtual machine once ready
     * @throws VirtException if the virtualization layer cannot spawn the virtual machine
     * @throws IOException with any file handling.
     */
    ListenableFuture<AllocationPhase, VirtualMachine> create(
            TemplateInstance template, VirtualCluster cluster, EventSource<AllocationPhase> source)
            throws VirtException, IOException;

    void install(Template template) throws IOException;
}
